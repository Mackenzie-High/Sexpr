package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.schema.ChoiceSchema;
import com.mackenziehigh.sexpr.schema.SAtomSchema;
import com.mackenziehigh.sexpr.schema.SListSchema;
import com.mackenziehigh.sexpr.schema.SexprSchema;
import com.mackenziehigh.sexpr.schema.SexprSchemaFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 *
 */
public final class SchemaFactory
        implements SexprSchemaFactory
{
    private static abstract class AbstractSchema<T extends SexprSchema<T>>
            implements SexprSchema<T>
    {
        private final List<Predicate<Sexpr>> predicates;

        private final List<Consumer<T>> before;

        private final List<Consumer<T>> after;

        public AbstractSchema (final List<Predicate<Sexpr>> predicates,
                               final List<Consumer<T>> before,
                               final List<Consumer<T>> after)
        {
            this.predicates = new LinkedList<>(predicates);
            this.before = new LinkedList<>(before);
            this.after = new LinkedList<>(after);
        }

        public abstract T modify (final List<Predicate<Sexpr>> predicates,
                                  final List<Consumer<T>> before,
                                  final List<Consumer<T>> after);

        @Override
        public T after (final Consumer<T> action)
        {
            final List<Consumer<T>> copy = new LinkedList<>(after);
            copy.add(Objects.requireNonNull(action));
            return modify(predicates, before, copy);
        }

        @Override
        public List<Consumer<T>> afterActions ()
        {
            return Collections.unmodifiableList(after);
        }

        @Override
        public T before (final Consumer<T> action)
        {
            final List<Consumer<T>> copy = new LinkedList<>(before);
            copy.add(Objects.requireNonNull(action));
            return modify(predicates, copy, after);
        }

        @Override
        public List<Consumer<T>> beforeActions ()
        {
            return Collections.unmodifiableList(before);
        }

        @Override
        public List<Predicate<Sexpr>> predicates ()
        {
            return Collections.unmodifiableList(predicates);
        }

        @Override
        public T require (Predicate<Sexpr> condition)
        {
            final List<Predicate<Sexpr>> copy = new LinkedList<>(predicates);
            copy.add(Objects.requireNonNull(condition));
            return modify(copy, before, after);
        }
    }

    private static final class AtomSchema
            extends AbstractSchema<SAtomSchema>
            implements SAtomSchema
    {
        public AtomSchema (final List<Predicate<Sexpr>> predicates,
                           final List<Consumer<SAtomSchema>> before,
                           final List<Consumer<SAtomSchema>> after)
        {
            super(predicates, before, after);
        }

        @Override
        public SAtomSchema modify (final List<Predicate<Sexpr>> predicates,
                                   final List<Consumer<SAtomSchema>> before,
                                   final List<Consumer<SAtomSchema>> after)
        {
            return new AtomSchema(predicates, before, after);
        }
    };

    private static final class ListSchema
            extends AbstractSchema<SListSchema>
            implements SListSchema
    {
        private final List<SexprSchema> prefix;

        private final SexprSchema suffix;

        public ListSchema (final List<Predicate<Sexpr>> predicates,
                           final List<Consumer<SListSchema>> before,
                           final List<Consumer<SListSchema>> after,
                           final List<SexprSchema> prefix,
                           final SexprSchema suffix)
        {
            super(predicates, before, after);
            this.prefix = Objects.requireNonNull(prefix);
            this.suffix = Objects.requireNonNull(suffix);
        }

        @Override
        public SListSchema modify (final List<Predicate<Sexpr>> predicates,
                                   final List<Consumer<SListSchema>> before,
                                   final List<Consumer<SListSchema>> after)
        {
            return new ListSchema(predicates, before, after, prefix, suffix);
        }

        @Override
        public List<SexprSchema> prefix ()
        {
            return Collections.unmodifiableList(prefix);
        }

        @Override
        public SListSchema prefix (final SexprSchema... elements)
        {
            return new ListSchema(predicates(),
                                  beforeActions(),
                                  afterActions(),
                                  Arrays.asList(elements), suffix);
        }

        @Override
        public SexprSchema suffix ()
        {
            return suffix;
        }

        @Override
        public SListSchema suffix (final SexprSchema elements)
        {
            return new ListSchema(predicates(),
                                  beforeActions(),
                                  afterActions(),
                                  prefix,
                                  elements);
        }

        @Override
        public SListSchema then (SexprSchema element)
        {
            final List<SexprSchema> list = new LinkedList<>(prefix);
            list.add(element);
            return new ListSchema(predicates(),
                                  beforeActions(),
                                  afterActions(),
                                  list,
                                  suffix);
        }
    };

    private static final class OrSchema
            extends AbstractSchema<ChoiceSchema>
            implements ChoiceSchema
    {
        private final List<SexprSchema> options;

        public OrSchema (final List<Predicate<Sexpr>> predicates,
                         final List<Consumer<ChoiceSchema>> before,
                         final List<Consumer<ChoiceSchema>> after,
                         final List<SexprSchema> options)
        {
            super(predicates, before, after);
            this.options = Objects.requireNonNull(options);
        }

        @Override
        public ChoiceSchema modify (final List<Predicate<Sexpr>> predicates,
                                    final List<Consumer<ChoiceSchema>> before,
                                    final List<Consumer<ChoiceSchema>> after)
        {
            return new OrSchema(predicates, before, after, options);
        }

        @Override
        public List<SexprSchema> options ()
        {
            return Collections.unmodifiableList(options);
        }
    };

    @Override
    public SAtomSchema atom ()
    {
        return new AtomSchema(Collections.emptyList(),
                              Collections.emptyList(),
                              Collections.emptyList());
    }

    @Override
    public ChoiceSchema firstOf (SexprSchema... options)
    {
        final List<SexprSchema> list = new LinkedList<>();
        list.addAll(Arrays.asList(options));
        return new OrSchema(Collections.emptyList(),
                            Collections.emptyList(),
                            Collections.emptyList(),
                            list);
    }

    @Override
    public SListSchema list ()
    {
        return new ListSchema(Collections.emptyList(),
                              Collections.emptyList(),
                              Collections.emptyList(),
                              Collections.emptyList(),
                              null);
    }

    public static void main (String[] args)
    {
        final SexprSchemaFactory f = SexprSchemaFactory.instance;

        f.list().then(f.atom().require("call/cc"));

    }

}
