/*
 * Copyright 2017 Michael Mackenzie High
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Schema.Match;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
import java.util.function.Predicate;

/**
 * An instance of this class is a pattern that describes a symbolic-expression.
 */
public final class InternalSchema
{
    /**
     * This counter is used to create names for anonymous rules.
     */
    private static int counter = 1;

    /**
     * This maintains important state during a match attempt.
     */
    private final class MatchState
    {
        /**
         * This stack is used to store match-nodes.
         * Whenever a successful match occurs, it will be placed onto this stack.
         * Matches will be popped of this stack and made the children of other matches.
         */
        private final Stack<Object> matches = new Stack<>();

        /**
         * This was the last node in the symbolic-expression
         * that was successfully matched, if any.
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
        private InternalMatchNode exitOnSuccess (final Rule rule,
                                                 final Sexpr node)
        {
            lastSuccess = node;

            /**
             * Remove any successful child matches from the stack.
             */
            final LinkedList<InternalMatchNode> children = new LinkedList<>();

            while (matches.peek() != node)
            {
                children.addFirst((InternalMatchNode) matches.pop());
            }

            /**
             * Undo push(node) in enter(node).
             */
            matches.pop();

            /**
             * Create the representation of the successful match.
             */
            final InternalMatchNode match = new InternalMatchNode(rule, node, children);
            matches.push(match);

            return match;
        }

        /**
         * This method is invoked whenever a rule exits a match-attempt
         * due to the fact that the rule failed to match the node.
         *
         * @param node is the node that was unsuccessfully matched.
         * @return null always.
         */
        private InternalMatchNode exitOnFailure (final Sexpr node)
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
     * An instance of this interface is a constraint on a single node in a symbolic-expression.
     */
    abstract class Rule
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
        public abstract InternalMatchNode match (MatchState state,
                                                 Sexpr node);

        /**
         * This method retrieves the name of this rule.
         *
         * @return the name of this rule.
         */
        public String name ()
        {
            return defaultName;
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
    private final SortedMap<String, Rule> rules = new TreeMap<>();

    /**
     * This supplier supplies the name of the root rule of the schema.
     */
    private String root = "root";

    /**
     * This map maps the names of user-defined conditions
     * to the definitions of those conditions, if any.
     */
    private final Map<String, Predicate<Sexpr<?>>> conditions = new TreeMap<>();

    /**
     * These are the names of the user-defined translation passes.
     */
    private final List<String> passes = new LinkedList<>();

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr<?>>>>> beforeActions = new TreeMap<>();

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr<?>>>>> afterActions = new TreeMap<>();

    /**
     * These are the names of all of the rules that have been used in the schema.
     * This may include undefined rules due to typos, etc, made by the user.
     * Such problems need to be detected and reported.
     */
    private final Set<String> usedRules = new TreeSet<>();

    /**
     * Sole Constructor.
     */
    public InternalSchema ()
    {
        /**
         * Define the predefined rules, whose names always start with a '$' by convention.
         */
        rules.put("$ANY", defineRuleByPredicate(x -> true));
        rules.put("$BOOLEAN", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asBoolean().isPresent()));
        rules.put("$CHAR", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asChar().isPresent()));
        rules.put("$BYTE", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asByte().isPresent()));
        rules.put("$SHORT", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asShort().isPresent()));
        rules.put("$INT", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asInt().isPresent()));
        rules.put("$LONG", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asLong().isPresent()));
        rules.put("$FLOAT", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asFloat().isPresent()));
        rules.put("$DOUBLE", defineRuleByPredicate(x -> x.isAtom() && x.asAtom().asDouble().isPresent()));
        rules.put("$ATOM", defineRuleByPredicate(x -> x.isAtom()));
        rules.put("$LIST", defineRuleByPredicate(x -> x.isList()));
    }

    /**
     * This method causes a rule to be added to this schema.
     *
     * @param rule is the rule to add.
     * @return the rule.
     */
    private Rule define (final Rule rule)
    {
        Objects.requireNonNull(rule, "rule");
        rules.put(rule.name(), rule);
        return rule;
    }

    /**
     * Use this method to specify the root rule of this schema.
     *
     * @param root is the name of the root rule.
     */
    public void defineRoot (final String root)
    {
        Objects.requireNonNull(root, "root");
        usedRules.add(root);
        this.root = root;
    }

    /**
     * This method defines a condition that can be referenced by a 'require' rule.
     *
     * @param name is the name of the user-defined condition.
     * @param condition is the user-defined condition itself.
     */
    public void defineCondition (final String name,
                                 final Predicate<Sexpr<?>> condition)
    {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(condition, "condition");
        conditions.put(name, condition);
    }

    public void definePass (final String name)
    {
        passes.add(name);
    }

    public void defineBeforeAction (final String pass,
                                    final String rule,
                                    final Consumer<Sexpr<?>> action)
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
                                   final Consumer<Sexpr<?>> action)
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
     * Use this method to create a rule that provides a name for another rule.
     *
     * @param name is the name of the new rule.
     * @param body is the name of the referenced (usually anonymous) rule.
     * @return the new rule.
     */
    final Rule defineNamedRule (final String name,
                                final String body)
    {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(body, "body");

        if (rules.containsKey(name))
        {
            throw new IllegalStateException("Duplicate Rule: " + name);
        }

        usedRules.add(name);
        usedRules.add(body);

        final Rule rule = new Rule()
        {
            @Override
            public String name ()
            {
                return name;
            }

            @Override
            public InternalMatchNode match (final MatchState state,
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
    final Rule defineReference (final String name)
    {
        Objects.requireNonNull(name, "name");

        usedRules.add(name);

        /**
         * This type of rule is a special-case.
         * Do *not* invoke enter(*), exitOnSuccess(*), or exitOnFailure(*),
         * because that would break how action execution works.
         */
        final Rule rule = new Rule()
        {
            @Override
            public InternalMatchNode match (final MatchState state,
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
    final Rule defineAndRule (final List<String> operands)
    {
        Objects.requireNonNull(operands, "operands");
        operands.forEach(operand -> Objects.requireNonNull(operand, "operand"));

        usedRules.addAll(operands);

        final Rule rule = new Rule()
        {
            @Override
            public InternalMatchNode match (final MatchState state,
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
    final Rule defineOrRule (final List<String> operands)
    {
        Objects.requireNonNull(operands, "operands");
        operands.forEach(operand -> Objects.requireNonNull(operand, "operand"));

        usedRules.addAll(operands);

        final Rule rule = new Rule()
        {
            @Override
            public InternalMatchNode match (final MatchState state,
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
    final Rule defineNotRule (final String operand)
    {
        Objects.requireNonNull(operand, "operand");

        usedRules.add(operand);

        final Rule rule = new Rule()
        {
            @Override
            public InternalMatchNode match (final MatchState state,
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
    final Rule defineSequenceRule (final List<? extends SequenceElement> operands)
    {
        Objects.requireNonNull(operands, "operands");
        operands.forEach(operand -> Objects.requireNonNull(operand, "operand"));

        for (SequenceElement operand : operands)
        {
            if (operand.maximum() < operand.minimum())
            {
                final String message = String.format("Invalid Range: { %d, %d }",
                                                     operand.minimum(),
                                                     operand.maximum());
                throw new IllegalStateException(message);
            }
        }

        operands.forEach(x -> usedRules.add(x.element()));

        final Rule rule = new Rule()
        {
            @Override
            public InternalMatchNode match (final MatchState state,
                                            final Sexpr node)
            {
                return sequenceMatch(this, operands, state, node);
            }
        };

        return define(rule);
    }

    private InternalMatchNode sequenceMatch (final Rule rule,
                                             final List<? extends SequenceElement> operands,
                                             final MatchState state,
                                             final Sexpr node)
    {
        Objects.requireNonNull(rule, "rule");
        Objects.requireNonNull(operands, "operands");
        operands.forEach(operand -> Objects.requireNonNull(operand, "operand"));
        Objects.requireNonNull(state, "state");
        Objects.requireNonNull(node, "node");

        state.enter(node);

        if (node.isList() == false)
        {
            return state.exitOnFailure(node);
        }

        final Deque<Sexpr> nodes = new ArrayDeque<>(node.asList());

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
                final InternalMatchNode match = rules.get(operand.element()).match(state, next);

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
                final InternalMatchNode match = rules.get(operand.element()).match(state, next);

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
    final Rule defineRegexRule (final String pattern)
    {
        Objects.requireNonNull(pattern, "pattern");

        return defineRuleByPredicate(x -> x.isAtom() && x.asAtom().content().matches(pattern));
    }

    /**
     * Use this method to define a rule that will only successfully
     * match a symbolic-atom whose content() equals the given value.
     *
     * @param value is the value that must be the content() of the node.
     * @return the new rule.
     */
    final Rule defineConstantRule (final String value)
    {
        Objects.requireNonNull(value, "value");

        return defineRuleByPredicate(x -> x.isAtom() && x.asAtom().content().equals(value));
    }

    /**
     * Use this method to define a new rule that will only successfully
     * match a node when a user-defined predicate matches the node.
     *
     * @param name is the name of the user-defined requirement.
     * @return the new rule.
     */
    final Rule definePredicateRule (final String name)
    {
        Objects.requireNonNull(name, "name");

        return defineRuleByPredicate(x -> Optional.ofNullable(conditions.get(name)).get().test(x));
    }

    /**
     * Use this method to define a new rule that will only successfully
     * match a node when a given predicate matches the node.
     *
     * @param condition is the predicate.
     * @return the new rule.
     */
    private Rule defineRuleByPredicate (final Predicate<Sexpr> condition)
    {
        Objects.requireNonNull(condition, "condition");

        final Rule rule = new Rule()
        {
            @Override
            public InternalMatchNode match (final MatchState state,
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
     * This method performs a match-attempt.
     *
     * @param tree is the symbolic-expression that this schema may match.
     * @return an object describing the result.
     */
    public Match match (final Sexpr tree)
    {
        Objects.requireNonNull(tree, "tree");

        /**
         * Verify that this schema is well-defined.
         */
        validate();

        /**
         * Perform the match attempt.
         */
        final MatchState state = new MatchState();
        final Rule rootRule = rules.get(root);
        final InternalMatchNode match = rootRule.match(state, tree);

        /**
         * If the match-attempt failed, then report the failure;
         * otherwise, execute the user-defined passes and actions.
         */
        final boolean success = match != null;
        final Match result = new InternalMatch(success,
                                               match,
                                               state.lastSuccess,
                                               List.copyOf(passes),
                                               Map.copyOf(beforeActions),
                                               Map.copyOf(afterActions));
        return result;
    }

    public void validate ()
    {
        requireRoot();
        checkForUndefinedRules();
        checkForUndeclaredPasses();
    }

    private void requireRoot ()
    {
        if (rules.containsKey(root) == false)
        {
            throw new IllegalStateException("No Root Rule");
        }
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
            final String names = SList.copyOf(undefinedRules.stream().map(name -> SAtom.fromString(name))).toString();
            final String message = "Undefined Rules Detected: " + names;
            throw new IllegalStateException(message);
        }
    }

    private void checkForUndeclaredPasses ()
    {
        /**
         * Check for before-actions that are not apart of a declared pass.
         */
        for (String pass : beforeActions.keySet())
        {
            if (passes.contains(pass) == false)
            {
                throw new IllegalStateException(String.format("Undeclared Pass: %s", pass));
            }
        }

        /**
         * Check for after-actions that are not apart of a declared pass.
         */
        for (String pass : afterActions.keySet())
        {
            if (passes.contains(pass) == false)
            {
                throw new IllegalStateException(String.format("Undeclared Pass: %s", pass));
            }
        }
    }
}
