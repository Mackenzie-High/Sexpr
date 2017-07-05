package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.internal.schema.Schema.Rule;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedMap;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * This class is used by the parser to create schemas.
 * In addition, this class provides a schema implementation.
 */
final class SchemaBuilder
{
    /**
     * This counter is used to create names for anonymous rules.
     */
    private static int counter = 1;

    /**
     * This maintains important state during a match attempt.
     */
    final class MatchState
    {
        /**
         * This stack is used to store match-nodes.
         * Whenever a successful match occurs, it will be placed onto this stack.
         * Matches will be popped of this stack and made the children of other matches.
         */
        private final Stack<Object> matches = new Stack<>();

        /**
         * This was the last node in the symbolic-expression
         * that was successfully matched.
         */
        private Sexpr lastSuccess = null;

        /**
         * This method retrieves the last node in the symbolic-expression
         * that was successfully matched by a rule in the schema,
         * if such a node exists.
         *
         * <p>
         * The last successful node is useful for debugging purposes.
         * </p>
         *
         * @return the site of the last successful match, if any.
         */
        public Optional<Sexpr> lastSuccess ()
        {
            return Optional.ofNullable(lastSuccess);
        }

        /**
         * This method is invoked whenever a rule begins a match-attempt.
         *
         * @param node is the node that the rule is attempting to match.
         */
        private void enter (final Sexpr node)
        {
            Objects.requireNonNull(node, "node");
            matches.push(node);
        }

        /**
         * This method is invoked whenever a rule exits a match-attempt
         * due to the fact that the rule successfully matched.
         *
         * @param rule is the rule invoking this method.
         * @param node is the node that was successfully matched.
         * @return the representation of the successful match.
         */
        private MatchNode exitOnSuccess (final Rule rule,
                                         final Sexpr node)
        {
            lastSuccess = node;

            /**
             * Remove any successful child matches from the stack.
             */
            final LinkedList<MatchNode> children = new LinkedList<>();

            while (matches.peek() != node)
            {
                children.addFirst((MatchNode) matches.pop());
            }

            /**
             * Undo push(node) in enter(node).
             */
            matches.pop();

            /**
             * Create the representation of the successful match.
             */
            final MatchNode match = new MatchNode(rule, node, children);
            matches.push(match);
            return match;
        }

        /**
         * This method is invoked whenever a rule exits a match-attempt
         * due to the fact that the rule failed to match the node.
         *
         * @param rule is the rule invoking this method.
         * @param node is the node that was unsuccessfully matched.
         * @return null.
         */
        private MatchNode exitOnFailure (final Sexpr node)
        {
            /**
             * Remove any successful child matches from the stack.
             */
            while (matches.peek() != node)
            {
                matches.pop();
            }

            /**
             * Undo push(node) in enter(node).
             */
            matches.pop();
            return null;
        }
    }

    /**
     * Partial Implementation of the Rule interface.
     */
    abstract class AbstractRule
            implements Rule
    {
        /**
         * The default-name starts with a '#' sign,
         * since '#' starts comments in schemas
         * we can be sure that no rule will (ever)
         * have such a name.
         */
        private final String defaultName = "#" + counter++;

        /**
         * This method determines whether this rule matches the given node.
         *
         * <p>
         * This method must invoke state.enter(node).
         * This method must invoke state.exitOnSuccess(node)
         * or state.exitOnFailure(node) depending on the result.
         * </p>
         *
         * @param state maintains the state of the overall match attempt.
         * @param node may obey the pattern described by this rule.
         * @return an object representing the successful match of this rule,
         * or null, if this rule does not match the given node.
         */
        public abstract MatchNode match (MatchState state,
                                         Sexpr node);

        /**
         * {@inheritDoc}
         */
        @Override
        public String name ()
        {
            return defaultName;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isAnonymous ()
        {
            return true;
        }
    }

    /**
     * An instance of this interface represents a single element in a sequence-rule.
     */
    interface SequenceElement
    {
        /**
         * This method retrieves the name of the rule that describes the sequence element.
         *
         * @return the name of a rule in the schema.
         */
        public String element ();

        /**
         * This method retrieves the minimum number of times that the element must repeat.
         *
         * @return the lower bound of the sequence-element.
         */
        public int minimum ();

        /**
         * This method retrieves the maximum number of times that the element must repeat.
         *
         * @return the upper bound of the sequence-element.
         */
        public int maximum ();
    }

    /**
     * These are all of the rules that are defined within the schema.
     */
    private final SortedMap<String, AbstractRule> rules = new TreeMap<>();

    /**
     * This supplier supplies the name of the root rule of the schema.
     */
    private Supplier<AbstractRule> rootSupplier = () -> rules.get("root");

    /**
     * These are the passes defined in the schema in the order they appear therein.
     */
    private final List<String> passes = new LinkedList<>();

    private final List<String> setupActions = new LinkedList<>();

    private final List<String> closeActions = new LinkedList<>();

    /**
     * This map maps the name of a pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of names of user-defined
     * actions (A1 ... AN) perform.
     */
    private final Map<String, Map<String, List<String>>> beforeActions = new TreeMap<>();

    /**
     * This map maps the name of a pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of names of user-defined
     * actions (A1 ... AN) perform.
     */
    private final Map<String, Map<String, List<String>>> afterActions = new TreeMap<>();

    /**
     * These are the names of all of the rules that have been used in the schema.
     * This may include undefined rules due to typos, etc, made by the user.
     * Such problems need to be detected and reported.
     */
    private final Set<String> usedRules = new TreeSet<>();

    /**
     * Sole Constructor.
     */
    public SchemaBuilder ()
    {
        /**
         * Define the predefined rules, whose names always start with a '$' by convention.
         */
        rules.put("$ANY", defineRuleByPredicate(x -> true));
        rules.put("$BOOLEAN", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asBoolean().isPresent()));
        rules.put("$CHAR", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asChar().isPresent()));
        rules.put("$BYTE", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asByte().isPresent()));
        rules.put("$SHORT", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asShort().isPresent()));
        rules.put("$INT", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asInt().isPresent()));
        rules.put("$LONG", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asLong().isPresent()));
        rules.put("$FLOAT", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asFloat().isPresent()));
        rules.put("$DOUBLE", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asDouble().isPresent()));
        rules.put("$CLASS", defineRuleByPredicate(x -> x.isAtom() && x.toAtom().asClass().isPresent()));
        rules.put("$ATOM", defineRuleByPredicate(x -> x.isAtom()));
        rules.put("$LIST", defineRuleByPredicate(x -> x.isList()));
    }

    /**
     * This method causes a rule to be added to this schema.
     *
     * @param rule is the rule to add.
     * @return the rule.
     */
    private AbstractRule define (final AbstractRule rule)
    {
        rules.put(rule.name(), rule);
        return rule;
    }

    /**
     * Use this method to specify the root rule of this schema.
     *
     * @param name is the name of the root rule.
     */
    public void defineRoot (final String name)
    {
        usedRules.add(name);
        rootSupplier = () -> rules.get(name);
    }

    /**
     * Use this method to create a rule that provides a name for another rule.
     *
     * @param name is the name of the new rule.
     * @param body is the name of the referenced (usually anonymous) rule.
     * @return the new rule.
     */
    public AbstractRule defineNamedRule (final String name,
                                         final String body)
    {
        usedRules.add(name);
        usedRules.add(body);

        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public String name ()
            {
                return name;
            }

            @Override
            public boolean isAnonymous ()
            {
                return false;
            }

            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                state.enter(node);
                final boolean answer = rules.get(body).match(state, node) != null;
                return answer ? state.exitOnSuccess(this, node) : state.exitOnFailure(node);
            }
        };

        return define(rule);
    }

    /**
     * Use this method to create a rule that is a lazy reference to another rule.
     *
     * @param name is the name of the new rule.
     * @return the new rule.
     */
    public AbstractRule defineReference (final String name)
    {
        usedRules.add(name);

        /**
         * This type of rule is a special-case.
         * Do *not* invoke enter(*), exitOnSuccess(*), or exitOnFailure(*),
         * because that would break how action execution works.
         */
        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                return rules.get(name).match(state, node);
            }
        };

        return define(rule);
    }

    /**
     * Use this method to define a rule that will only successfully match
     * a node when a series of operand rules always match the node.
     *
     * @param operands must all match the same node in order for the new rule to match.
     * @return the new rule.
     */
    public AbstractRule defineAndRule (final List<String> operands)
    {
        usedRules.addAll(operands);

        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                state.enter(node);
                final boolean answer = operands.stream().map(name -> rules.get(name)).allMatch(rule -> rule.match(state, node) != null);
                return answer ? state.exitOnSuccess(this, node) : state.exitOnFailure(node);
            }
        };

        return define(rule);
    }

    /**
     * Use this method to define a rule that will successfully match a node
     * when the first of a series of operand rules matches the node.
     *
     * @param operands are the options that may match the node.
     * @return the new rule.
     */
    public AbstractRule defineOrRule (final List<String> operands)
    {
        usedRules.addAll(operands);

        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                state.enter(node);
                final boolean answer = operands.stream().map(name -> rules.get(name)).anyMatch(rule -> rule.match(state, node) != null);
                return answer ? state.exitOnSuccess(this, node) : state.exitOnFailure(node);
            }
        };

        return define(rule);
    }

    /**
     * Use this method to define a rule that will only successfully
     * match a node when a given operand rule fails to match.
     *
     * @param operand the rule that is negated by the new rule.
     * @return the new rule.
     */
    public AbstractRule defineNotRule (final String operand)
    {
        usedRules.add(operand);

        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                state.enter(node);
                final boolean answer = rules.get(operand).match(state, node) == null;
                return answer ? state.exitOnSuccess(this, node) : state.exitOnFailure(node);
            }
        };

        return define(rule);
    }

    /**
     * Use this method to define a rule that will only successfully
     * match a symbolic-list that obeys a proscribed sequence.
     *
     * @param operands describe the elements in the sequence.
     * @return the new rule.
     */
    public AbstractRule defineSequenceRule (final List<? extends SequenceElement> operands)
    {
        operands.forEach(x -> usedRules.add(x.element()));

        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                return sequenceMatch(this, operands, state, node);
            }
        };

        return define(rule);
    }

    private MatchNode sequenceMatch (final Rule rule,
                                     final List<? extends SequenceElement> operands,
                                     final MatchState state,
                                     final Sexpr node)
    {
        state.enter(node);

        if (node.isList() == false)
        {
            return state.exitOnFailure(node);
        }

        final Deque<Sexpr> nodes = new ArrayDeque<>(node.toList());

seq:    for (SequenceElement operand : operands)
        {
            int i;

            /**
             * The operand rule must match at least the minimum number of times.
             */
            for (i = 0; i < operand.minimum(); i++)
            {
                /**
                 * If no more nodes are in the list, then the rule has failed to match,
                 * because the minium match count was not reached for this operand rule.
                 */
                if (nodes.isEmpty())
                {
                    return state.exitOnFailure(node);
                }

                final Sexpr next = nodes.peek();
                final MatchNode match = rules.get(operand.element()).match(state, next);

                if (match == null)
                {
                    return state.exitOnFailure(node);
                }
                else
                {
                    nodes.pop();
                }
            }

            /**
             * The operand rule can continue to match until the maximum is reached.
             */
            for (i = i + 0; i < operand.maximum(); i++)
            {
                /**
                 * If no more nodes are in the list, then goto the next operand rule,
                 * because the tail rules may be require to match more than zero times.
                 * In that case, the overall sequence rule has failed,
                 * even though the current operand rule succeeded.
                 */
                if (nodes.isEmpty())
                {
                    continue seq;
                }

                final Sexpr next = nodes.peek();
                final MatchNode match = rules.get(operand.element()).match(state, next);

                if (match == null)
                {
                    continue seq; // Go to the next operand rule.
                }
                else
                {
                    nodes.pop();
                }
            }
        }

        /**
         * If there are still more nodes in the list,
         * then the sequence-rule only described the prefix of the list,
         * which we do not consider to be a true match of the list.
         */
        if (nodes.isEmpty() == false)
        {
            return state.exitOnFailure(node);
        }

        return state.exitOnSuccess(rule, node);
    }

    /**
     * Use this method to define a rule that will only successfully match
     * a symbolic-atom whose content() matches a given regular-expression.
     *
     * @param pattern is the given symbolic-expression.
     * @return the new rule.
     */
    public AbstractRule defineRegexRule (final String pattern)
    {
        return defineRuleByPredicate(x -> x.isAtom() && x.toAtom().content().matches(pattern));
    }

    /**
     * Use this method to define a rule that will only successfully
     * match a symbolic-atom whose content() equals the given value.
     *
     * @param value is the value that must be the content() of the node.
     * @return the new rule.
     */
    public AbstractRule defineConstantRule (final String value)
    {
        return defineRuleByPredicate(x -> x.isAtom() && x.toAtom().content().equals(value));
    }

    /**
     * Use this method to define a rule that will only successfully
     * match a symbolic-atom whose numeric representation is
     * within the proscribed range.
     *
     * @param minimum is the lower-bound of the acceptable range.
     * @param minimumInclusive is true, iff the lower-bound is inclusive.
     * @param maximum is the upper-bound of the acceptable range.
     * @param maximumInclusive is true, iff the upper-bound is inclusive.
     * @return the new rule.
     */
    public AbstractRule defineRangeRule (final double minimum,
                                         final boolean minimumInclusive,
                                         final double maximum,
                                         final boolean maximumInclusive)
    {
        return defineRuleByPredicate(x -> x.isAtom()
                                          && x.toAtom().asFloat().isPresent()
                                          && (minimumInclusive ? minimum <= x.toAtom().asFloat().get() : minimum < x.toAtom().asFloat().get())
                                          && (maximumInclusive ? maximum >= x.toAtom().asFloat().get() : maximum > x.toAtom().asFloat().get()));
    }

    /**
     * Use this method to define a new rule that will only successfully
     * match a node when a user-defined predicate matches the node.
     *
     * @param name is the name of the user-defined requirement.
     * @return the new rule.
     */
    public AbstractRule definePredicateRule (final String name)
    {
        return null;
    }

    /**
     * Use this method to define a new rule that will only successfully
     * match a node when a given predicate matches the node.
     *
     * @param condition is the predicate.
     * @return the new rule.
     */
    private AbstractRule defineRuleByPredicate (final Predicate<Sexpr> condition)
    {
        final AbstractRule rule = new AbstractRule()
        {
            @Override
            public MatchNode match (final MatchState state,
                                    final Sexpr node)
            {
                state.enter(node);
                final boolean answer = condition.test(node);
                return answer ? state.exitOnSuccess(this, node) : state.exitOnFailure(node);
            }
        };

        return define(rule);
    }

    /**
     * Use this method to define another compiler pass.
     *
     * <p>
     * The order in which this method is invoked will
     * define the order of compiler passes relative
     * to one another.
     * </p>
     *
     * @param name is the name of the new compiler pass.
     */
    public void definePass (final String name)
    {
        Objects.requireNonNull(name, "name");
        passes.add(name);
    }

    public void defineSetupAction (final String action)
    {
        setupActions.add(action);
    }

    public void defineCloseAction (final String action)
    {
        closeActions.add(action);
    }

    public void defineBeforeAction (final String pass,
                                    final String rule,
                                    final String action)
    {
        if (beforeActions.containsKey(pass) == false)
        {
            beforeActions.put(pass, new TreeMap<>());
        }

        if (beforeActions.get(pass).containsKey(rule) == false)
        {
            beforeActions.get(pass).put(rule, new LinkedList<>());
        }

        beforeActions.get(pass).get(rule).add(action);
    }

    public void defineAfterAction (final String pass,
                                   final String rule,
                                   final String action)
    {
        if (afterActions.containsKey(pass) == false)
        {
            afterActions.put(pass, new TreeMap<>());
        }

        if (afterActions.get(pass).containsKey(rule) == false)
        {
            afterActions.get(pass).put(rule, new LinkedList<>());
        }

        afterActions.get(pass).get(rule).add(action);
    }

    /**
     * Use this method to create a new schema from the current state of this builder.
     *
     * @return the new schema.
     */
    public Schema build ()
    {

        checkForUndefinedRules();

        return new Schema()
        {
            @Override
            public boolean match (final Sexpr tree,
                                  final Function<String, Predicate<Sexpr>> resolveCondition,
                                  final Function<String, Consumer<Sexpr>> resolveAction,
                                  final Consumer<MatchNode> onSuccess,
                                  final Consumer<Optional<Sexpr>> onFailure)
            {
                final MatchState state = new MatchState();
                final AbstractRule root = (AbstractRule) root();
                final MatchNode match = root.match(state, tree);
                if (match == null)
                {
                    onFailure.accept(state.lastSuccess());
                    return false;
                }
                else
                {
                    onSuccess.accept(match);
                    executeActions(match, resolveAction);
                    return true;
                }
            }

            @Override
            public Rule root ()
            {
                final Rule root = rootSupplier.get();

                if (root == null)
                {
                    throw new IllegalStateException("No Root Rule");
                }

                return root;
            }

            @Override
            public SortedMap<String, Rule> rules ()
            {
                return Collections.unmodifiableSortedMap(rules);
            }
        };
    }

    /**
     * This method reports any rules that are used, but not defined in the schema.
     */
    private void checkForUndefinedRules ()
    {
        final Set<String> definedRules = rules.keySet();
        final Set<String> temp = new HashSet<>(usedRules);
        temp.removeAll(definedRules);
        final List<String> undefinedRules = new ArrayList<>(temp);

        if (undefinedRules.isEmpty() == false)
        {
            final String names = SList.copyOf(undefinedRules.stream().map(name -> new SAtom(name))).toString();
            final String message = "Undefined Rules Detected: " + names;
            throw new IllegalStateException(message);
        }
    }

    /**
     * On successful matches, this method transverses the match-tree
     * in multiple "passes" executing user-defined actions.
     *
     */
    private void executeActions (final MatchNode tree,
                                 final Function<String, Consumer<Sexpr>> resolveAction)
    {
        setupActions.forEach(name -> Optional.ofNullable(resolveAction.apply(name)).ifPresent(x -> x.accept(tree.node())));

        for (String pass : passes)
        {
            executeActions(pass, tree, resolveAction);
        }

        closeActions.forEach(name -> Optional.ofNullable(resolveAction.apply(name)).ifPresent(x -> x.accept(tree.node())));
    }

    private void executeActions (final String pass,
                                 final MatchNode node,
                                 final Function<String, Consumer<Sexpr>> resolveAction)
    {
        if (beforeActions.containsKey(pass) && beforeActions.get(pass).containsKey(node.rule().name()))
        {
            beforeActions.get(pass).get(node.rule().name()).forEach(action -> Optional.ofNullable(resolveAction.apply(action)).ifPresent(x -> x.accept(node.node())));
        }

        node.children().forEach(child -> executeActions(pass, child, resolveAction));

        if (afterActions.containsKey(pass) && afterActions.get(pass).containsKey(node.rule().name()))
        {
            afterActions.get(pass).get(node.rule().name()).forEach(action -> Optional.ofNullable(resolveAction.apply(action)).ifPresent(x -> x.accept(node.node())));
        }
    }
}