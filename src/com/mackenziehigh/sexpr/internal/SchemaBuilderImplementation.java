package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.schema.MatchNode;
import com.mackenziehigh.sexpr.schema.MatchResult;
import com.mackenziehigh.sexpr.schema.Schema;
import com.mackenziehigh.sexpr.schema.SchemaBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Implementation of SchemaBuilder.
 */
final class SchemaBuilderImplementation
        implements SchemaBuilder
{
    /**
     * Implementation of RuleBuilder.
     */
    private final class RuleBuilderImp
            implements RuleBuilder
    {
        public final String name;

        public Optional<AbstractRule> rule = Optional.empty();

        public RuleBuilderImp (final String name)
        {
            this.name = Objects.requireNonNull(name);
        }

        @Override
        public Conjunction all ()
        {
            final ConjunctionImp result = new ConjunctionImp();
            result.name = name;
            rule = Optional.of(result);
            return result;
        }

        @Override
        public Any any ()
        {
            final AnyImp result = new AnyImp();
            result.name = name;
            rule = Optional.of(result);
            return result;
        }

        @Override
        public Leaf leaf ()
        {
            final LeafImp result = new LeafImp();
            result.name = name;
            rule = Optional.of(result);
            return result;
        }

        @Override
        public SequencePrefix list ()
        {
            final SequencePrefixImp result = new SequencePrefixImp();
            result.name = name;
            rule = Optional.of(result);
            return result;
        }

        @Override
        public Negation none ()
        {
            final NegationImp result = new NegationImp();
            result.name = name;
            rule = Optional.of(result);
            return result;
        }

        @Override
        public Disjunction choice ()
        {
            final DisjunctionImp result = new DisjunctionImp();
            result.name = name;
            rule = Optional.of(result);
            return result;
        }

        @Override
        public RuleBuilder root ()
        {
            root = name;
            return this;
        }

    }

    /**
     * Implementation of Rule.
     */
    private abstract class AbstractRule<T extends Rule<T>>
            implements Rule<T>
    {
        public String name;

        public final List<Consumer<? extends Sexpr>> before = new LinkedList<>();

        public final List<Consumer<? extends Sexpr>> after = new LinkedList<>();

        public abstract MatchNodeImp match (AtomicReference<Sexpr> last,
                                            Sexpr node);

        @Override
        @SuppressWarnings ("unchecked")
        public T after (Consumer<? extends Sexpr> action)
        {
            after.add(Objects.requireNonNull(action));
            return (T) this;
        }

        @Override
        @SuppressWarnings ("unchecked")
        public T before (Consumer<? extends Sexpr> action)
        {
            before.add(Objects.requireNonNull(action));
            return (T) this;
        }

    }

    /**
     * Implementation of Leaf.
     */
    private final class LeafImp
            extends AbstractRule<Leaf>
            implements Leaf
    {
        public final List<Predicate<SAtom>> predicates = new LinkedList<>();

        @Override
        public Leaf require (Predicate<SAtom> condition)
        {
            predicates.add(Objects.requireNonNull(condition));
            return this;
        }

        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            if (node.isAtom() == false)
            {
                return null;
            }

            for (Predicate<SAtom> p : predicates)
            {
                if (p.test((SAtom) node) == false)
                {
                    return null;
                }
            }

            final MatchNodeImp result = new MatchNodeImp(name, node, Collections.emptyList());
            last.set(node);
            return result;
        }
    }

    /**
     * Implementation of Conjunction.
     */
    private final class ConjunctionImp
            extends AbstractRule<Conjunction>
            implements Conjunction
    {
        public final List<String> elements = new LinkedList<>();

        @Override
        public Conjunction of (String... names)
        {
            Arrays.asList(names).forEach(x -> Objects.requireNonNull(x));
            elements.addAll(Arrays.asList(names));
            return this;
        }

        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            final List<MatchNodeImp> children = new ArrayList<>(elements.size());

            for (String element : elements)
            {
                final MatchNodeImp result = lookup(element).match(last, node);
                children.add(result);

                if (result == null)
                {
                    return null;
                }
            }

            final MatchNodeImp result = new MatchNodeImp(name, node, children);
            last.set(node);
            return result;
        }
    }

    /**
     * Implementation of Disjunction.
     */
    private final class DisjunctionImp
            extends AbstractRule<Disjunction>
            implements Disjunction
    {
        public final List<String> elements = new LinkedList<>();

        @Override
        public Disjunction of (String... names)
        {
            Arrays.asList(names).forEach(x -> Objects.requireNonNull(x));
            elements.addAll(Arrays.asList(names));
            return this;
        }

        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            final List<MatchNodeImp> children = new ArrayList<>(elements.size());

            for (String element : elements)
            {
                final MatchNodeImp result = lookup(element).match(last, node);

                if (result != null)
                {
                    children.add(result);
                }
            }

            if (children.isEmpty())
            {
                return null;
            }
            else
            {
                final MatchNodeImp result = new MatchNodeImp(name, node, children);
                last.set(node);
                return result;
            }
        }
    }

    /**
     * Implementation of Negation.
     */
    private final class NegationImp
            extends AbstractRule<Negation>
            implements Negation
    {
        public final List<String> elements = new LinkedList<>();

        @Override
        public Negation of (String... names)
        {
            Arrays.asList(names).forEach(x -> Objects.requireNonNull(x));
            elements.addAll(Arrays.asList(names));
            return this;
        }

        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            for (String element : elements)
            {
                final MatchNodeImp result = lookup(element).match(last, node);

                if (result != null)
                {
                    return null;
                }
            }

            final MatchNodeImp result = new MatchNodeImp(name, node, Collections.emptyList());
            last.set(node);
            return result;
        }

    }

    /**
     * Implementation of SequencePrefix.
     */
    private final class SequencePrefixImp
            extends AbstractRule<SequencePrefix>
            implements SequencePrefix
    {
        public final ArrayList<String> elements = new ArrayList<>();

        public final SequenceSuffixImp suffix = new SequenceSuffixImp();

        @Override
        public SequenceSuffix of (String... names)
        {
            Arrays.asList(names).forEach(x -> Objects.requireNonNull(x));
            elements.addAll(Arrays.asList(names));
            elements.trimToSize();
            return suffix;
        }

        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            if (node.isList() == false)
            {
                return null;
            }

            final SList list = (SList) node;

            final List<MatchNodeImp> children = new ArrayList<>(elements.size());

            for (int i = 0; i < elements.size() && i < list.size(); i++)
            {
                final MatchNodeImp match = lookup(elements.get(i)).match(last, node);
                children.add(match);

                if (match == null)
                {
                    return null;
                }
            }

            final MatchNodeImp suffixMatch = suffix.match(last, node);

            children.addAll(suffixMatch.children);

            final MatchNodeImp result = new MatchNodeImp(name, node, children);
            last.set(node);
            return result;
        }
    }

    /**
     * Implementation of SequenceSuffix.
     */
    private final class SequenceSuffixImp
            extends AbstractRule<SequenceSuffix>
            implements SequenceSuffix
    {
        public Optional<String> rule = Optional.empty();

        public int minimum = 0;

        public int maximum = Integer.MAX_VALUE;

        @Override
        public SequenceSuffix followedBy (String name)
        {
            rule = Optional.of(name);
            return this;
        }

        @Override
        public SequenceSuffix max (int count)
        {
            if (count >= 0)
            {
                maximum = count;
                return this;
            }
            else
            {
                throw new IllegalArgumentException("maximum < 0");
            }
        }

        @Override
        public SequenceSuffix min (int count)
        {
            if (count >= 0)
            {
                minimum = count;
                return this;
            }
            else
            {
                throw new IllegalArgumentException("minimum < 0");
            }
        }

        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            // TODO
            final MatchNodeImp result = new MatchNodeImp(name, node, Collections.emptyList());
            last.set(node);
            return result;
        }
    }

    /**
     * Implementation of Any.
     */
    private final class AnyImp
            extends AbstractRule<Any>
            implements Any
    {
        @Override
        public MatchNodeImp match (final AtomicReference<Sexpr> last,
                                   final Sexpr node)
        {
            final MatchNodeImp result = new MatchNodeImp(name, node, Collections.emptyList());
            last.set(node);
            return result;
        }
    }

    /**
     * Implementation of MatchNode.
     */
    private final class MatchNodeImp
            implements MatchNode
    {
        public final String rule;

        public final Sexpr value;

        public final ArrayList<MatchNodeImp> children;

        public MatchNodeImp (final String rule,
                             final Sexpr value,
                             final List<MatchNodeImp> children)
        {
            this.rule = Objects.requireNonNull(rule);
            this.value = Objects.requireNonNull(value);
            this.children = new ArrayList<>(children);
            this.children.trimToSize();
        }

        @Override
        public Collection<Consumer<Sexpr>> after ()
        {
            return Collections.unmodifiableList(rules.get(rule()).rule.get().after);
        }

        @Override
        public Collection<Consumer<Sexpr>> before ()
        {
            return Collections.unmodifiableList(rules.get(rule()).rule.get().before);
        }

        @Override
        public List<MatchNode> children ()
        {
            return Collections.unmodifiableList(children);
        }

        @Override
        public String rule ()
        {
            return rule;
        }

        @Override
        public Sexpr value ()
        {
            return value;
        }

    }

    private String root = "root";

    private final Map<String, RuleBuilderImp> rules = new TreeMap<>();

    private boolean built = false;

    @Override
    public Schema build ()
    {
        if (built)
        {
            throw new IllegalStateException("build() was already called.");
        }
        else
        {
            built = true;
        }

        final Schema schema = new Schema()
        {
            @Override
            public MatchResult match (final Sexpr node)
            {

                final AtomicReference<Sexpr> last = new AtomicReference<>();
                final MatchNode matchTree = lookup(root).match(last, node);

                return new MatchResult()
                {
                    @Override
                    public Optional<Sexpr> lastSuccess ()
                    {
                        return Optional.ofNullable(last.get());
                    }

                    @Override
                    public Optional<MatchNode> matchTree ()
                    {
                        return Optional.ofNullable(matchTree);
                    }
                };
            }
        };

        return schema;
    }

    @Override
    public RuleBuilder define (String name)
    {
        if (rules.containsKey(name))
        {
            throw new IllegalArgumentException("Duplicate Rule: " + name);
        }
        else
        {
            rules.put(name, new RuleBuilderImp(name));
            return rules.get(name);
        }
    }

    private AbstractRule lookup (final String name)
    {
        if (rules.containsKey(name))
        {
            return rules.get(name).rule.get();
        }
        else
        {
            throw new IllegalStateException("No Such Rule: " + name);
        }
    }

    public static void main (String[] args)
    {
        final SchemaBuilder b = new SchemaBuilderImplementation();
        b.define("x").leaf().require("5").before(x -> System.out.println("X = " + x));
        b.define("y").leaf().require("7").before(x -> System.out.println("Y = " + x));
        b.define("z").root().choice().of("x", "y");

        final Sexpr sexpr = new SAtom(5);

        b.build().match(sexpr).execute().errorMessage().ifPresent(x -> System.out.println(x));
    }
}
