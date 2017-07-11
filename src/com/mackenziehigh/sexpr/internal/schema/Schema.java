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
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.annotations.Before;
import com.mackenziehigh.sexpr.annotations.Condition;
import com.mackenziehigh.sexpr.annotations.Pass;
import java.lang.reflect.Method;
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
import java.util.function.Function;
import java.util.function.Predicate;
import com.mackenziehigh.sexpr.annotations.After;

/**
 * An instance of this class is a pattern that describes a symbolic-expression.
 * THis class provides the actual implementation of schemas.
 */
public final class Schema
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
         * @return null always.
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
        public abstract MatchNode match (MatchState state,
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

        /**
         * This method determines whether this is an implicitly named rule.
         *
         * @return false, iff the user named this rule.
         */
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
     * This function will be invoked on failed match attempts.
     */
    private Consumer<Optional<Sexpr>> failureHandler = x ->
    {
        // Pass.
    };

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
    private final Map<String, Predicate<Sexpr>> conditions = new TreeMap<>();

    /**
     * These are the names of the user-defined translation passes.
     */
    private final List<String> passes = new LinkedList<>();

    /**
     * These are the actions to perform before the first
     * translation pass upon a successful match.
     */
    private final List<Consumer<Sexpr>> setupActions = new LinkedList<>();

    /**
     * These are the actions to perform after the last
     * translation pass upon a successful match.
     */
    private final List<Consumer<Sexpr>> closeActions = new LinkedList<>();

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr>>>> beforeActions = new TreeMap<>();

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr>>>> afterActions = new TreeMap<>();

    /**
     * These are the names of all of the rules that have been used in the schema.
     * This may include undefined rules due to typos, etc, made by the user.
     * Such problems need to be detected and reported.
     */
    private final Set<String> usedRules = new TreeSet<>();

    /**
     * Sole Constructor.
     */
    public Schema ()
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
     * This method sets the failure-handler.
     *
     * @param handler is the new handler.
     */
    public void setFailureHandler (final Consumer<Optional<Sexpr>> handler)
    {
        Objects.requireNonNull(handler, "handler");
        failureHandler = handler;
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
                                 final Predicate<Sexpr> condition)
    {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(condition, "condition");
        conditions.put(name, condition);
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
    final Rule defineAndRule (final List<String> operands)
    {
        Objects.requireNonNull(operands, "operands");
        operands.forEach(operand -> Objects.requireNonNull(operand, "operand"));

        usedRules.addAll(operands);

        final Rule rule = new Rule()
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
    final Rule defineOrRule (final List<String> operands)
    {
        Objects.requireNonNull(operands, "operands");
        operands.forEach(operand -> Objects.requireNonNull(operand, "operand"));

        usedRules.addAll(operands);

        final Rule rule = new Rule()
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
    final Rule defineNotRule (final String operand)
    {
        Objects.requireNonNull(operand, "operand");

        usedRules.add(operand);

        final Rule rule = new Rule()
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
    final Rule defineRegexRule (final String pattern)
    {
        Objects.requireNonNull(pattern, "pattern");

        return defineRuleByPredicate(x -> x.isAtom() && x.toAtom().content().matches(pattern));
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
    final Rule defineRangeRule (final double minimum,
                                final boolean minimumInclusive,
                                final double maximum,
                                final boolean maximumInclusive)
    {
        if (maximum < minimum)
        {
            final String message = String.format("Invalid Range: maximum (%f) < minimum (%f)", maximum, minimum);
            throw new IllegalArgumentException(message);
        }

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

    public void defineSetupAction (final Consumer<Sexpr> action)
    {
        Objects.requireNonNull(action, "action");
        setupActions.add(action);
    }

    public void defineCloseAction (final Consumer<Sexpr> action)
    {
        Objects.requireNonNull(action, "action");
        closeActions.add(action);
    }

    public void defineBeforeAction (final String pass,
                                    final String rule,
                                    final Consumer<Sexpr> action)
    {
        Objects.requireNonNull(pass, "pass");
        Objects.requireNonNull(rule, "rule");
        Objects.requireNonNull(action, "action");

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
                                   final Consumer<Sexpr> action)
    {
        Objects.requireNonNull(pass, "pass");
        Objects.requireNonNull(rule, "rule");
        Objects.requireNonNull(action, "action");

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
     * Given an object containing properly annotated methods,
     * define the conditions and actions defined therein.
     *
     * @param object contains condition and action definitions.
     */
    public void defineViaReflection (final Object object)
    {
        final Optional<String> defaultPass = getDefaultPass(object);

        for (Method method : object.getClass().getMethods())
        {
            if (method.isAnnotationPresent(Condition.class))
            {
                defineConditionViaReflection(defaultPass, object, method);
            }

            if (method.isAnnotationPresent(Before.class))
            {
                defineBeforeActionByReflection(defaultPass, object, method);
            }

            if (method.isAnnotationPresent(After.class))
            {
                defineAfterActionByReflection(defaultPass, object, method);
            }
        }
    }

    private Optional<String> getDefaultPass (final Object object)
    {
        if (object.getClass().isAnnotationPresent(Pass.class))
        {
            final String name = object.getClass().getAnnotation(Pass.class).name();
            return Optional.of(name);
        }
        else
        {
            return Optional.empty();
        }
    }

    private String getPass (final Optional<String> defaultPass,
                            final Method method)
    {
        if (method.isAnnotationPresent(Pass.class))
        {
            final String name = method.getAnnotation(Pass.class).name();
            return name;
        }
        else if (defaultPass.isPresent())
        {
            return defaultPass.get();
        }
        else
        {
            throw new IllegalArgumentException("No translation pass was specified on either the class or method.");
        }
    }

    private void defineConditionViaReflection (final Optional<String> defaultPass,
                                               final Object object,
                                               final Method method)
    {
        /**
         * Obtain the user-defined name of the condition.
         */
        final String name = method.getAnnotation(Condition.class).name();

        /**
         * The method cannot throw any checked exceptions.
         */
        if (method.getExceptionTypes().length != 0)
        {
            final String message = String.format("Do *not* throw checked exceptions in (%s).", method.toString());
            throw new IllegalArgumentException(message);
        }

        /**
         * The return-type of the method must be boolean.
         */
        if (method.getReturnType().equals(boolean.class) == false)
        {
            final String message = String.format("You must return boolean from (%s), not %s.",
                                                 method.toString(),
                                                 method.getReturnType().getName());
            throw new IllegalArgumentException(message);
        }

        /**
         * The method must take exactly one argument,
         * which must be a symbolic-expression.
         */
        if (method.getParameterCount() != 1)
        {
            final String message = String.format("Method (%s) must take exactly one parameter.", method.toString());
            throw new IllegalArgumentException(message);
        }
        else if (method.getParameterTypes()[0].equals(Sexpr.class))
        {
            final Function<Sexpr, Object> invocation = createInvocation(object, method);
            final Predicate<Sexpr> condition = x -> (Boolean) invocation.apply(x);
            defineCondition(name, condition);
        }
        else if (method.getParameterTypes()[0].equals(SAtom.class))
        {
            final Function<SAtom, Object> invocation = createInvocation(object, method);
            final Predicate<Sexpr> condition = x -> x.isAtom() ? (Boolean) invocation.apply(x.toAtom()) : false;
            defineCondition(name, condition);
        }
        else if (method.getParameterTypes()[0].equals(SList.class))
        {
            final Function<SList, Object> invocation = createInvocation(object, method);
            final Predicate<Sexpr> condition = x -> x.isList() ? (Boolean) invocation.apply(x.toList()) : false;
            defineCondition(name, condition);
        }
        else
        {
            final String message = String.format("Method (%s) must take a %s|%s|%s as its only parameter.",
                                                 method.toString(),
                                                 Sexpr.class.getName(),
                                                 SAtom.class.getName(),
                                                 SList.class.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void defineBeforeActionByReflection (final Optional<String> defaultPass,
                                                 final Object object,
                                                 final Method method)
    {
        /**
         * Obtain the name of the translation pass that this action applies to.
         */
        final String pass = getPass(defaultPass, method);

        /**
         * Obtain the user-defined name of the rule that this action applies to.
         */
        final String rule = method.getAnnotation(Before.class).rule();

        /**
         * The method cannot throw any checked exceptions.
         */
        if (method.getExceptionTypes().length != 0)
        {
            final String message = String.format("Do *not* throw checked exceptions in (%s).", method.toString());
            throw new IllegalArgumentException(message);
        }

        /**
         * The return-type of the method must be void.
         */
        if (method.getReturnType().equals(void.class) == false)
        {
            final String message = String.format("You must return boolean from (%s), not %s.",
                                                 method.toString(),
                                                 method.getReturnType().getName());
            throw new IllegalArgumentException(message);
        }

        /**
         * The method must take exactly one argument,
         * which must be a symbolic-expression.
         */
        if (method.getParameterCount() != 1)
        {
            final String message = String.format("Method (%s) must take exactly one parameter.", method.toString());
            throw new IllegalArgumentException(message);
        }
        else if (method.getParameterTypes()[0].equals(Sexpr.class))
        {
            final Function<Sexpr, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x);
            defineBeforeAction(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SAtom.class))
        {
            final Function<SAtom, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.toAtom());
            defineBeforeAction(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SList.class))
        {
            final Function<SList, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.toList());
            defineBeforeAction(pass, rule, action);
        }
        else
        {
            final String message = String.format("Method (%s) must take a %s|%s|%s as its only parameter.",
                                                 method.toString(),
                                                 Sexpr.class.getName(),
                                                 SAtom.class.getName(),
                                                 SList.class.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private void defineAfterActionByReflection (final Optional<String> defaultPass,
                                                final Object object,
                                                final Method method)
    {
        /**
         * Obtain the name of the translation pass that this action applies to.
         */
        final String pass = getPass(defaultPass, method);

        /**
         * Obtain the user-defined name of the rule that this action applies to.
         */
        final String rule = method.getAnnotation(Before.class).rule();

        /**
         * The method cannot throw any checked exceptions.
         */
        if (method.getExceptionTypes().length != 0)
        {
            final String message = String.format("Do *not* throw checked exceptions in (%s).", method.toString());
            throw new IllegalArgumentException(message);
        }

        /**
         * The return-type of the method must be void.
         */
        if (method.getReturnType().equals(void.class) == false)
        {
            final String message = String.format("You must return boolean from (%s), not %s.",
                                                 method.toString(),
                                                 method.getReturnType().getName());
            throw new IllegalArgumentException(message);
        }

        /**
         * The method must take exactly one argument,
         * which must be a symbolic-expression.
         */
        if (method.getParameterCount() != 1)
        {
            final String message = String.format("Method (%s) must take exactly one parameter.", method.toString());
            throw new IllegalArgumentException(message);
        }
        else if (method.getParameterTypes()[0].equals(Sexpr.class))
        {
            final Function<Sexpr, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x);
            defineBeforeAction(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SAtom.class))
        {
            final Function<SAtom, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.toAtom());
            defineBeforeAction(pass, rule, action);
        }
        else if (method.getParameterTypes()[0].equals(SList.class))
        {
            final Function<SList, Object> invocation = createInvocation(object, method);
            final Consumer<Sexpr> action = x -> invocation.apply(x.toList());
            defineAfterAction(pass, rule, action);
        }
        else
        {
            final String message = String.format("Method (%s) must take a %s|%s|%s as its only parameter.",
                                                 method.toString(),
                                                 Sexpr.class.getName(),
                                                 SAtom.class.getName(),
                                                 SList.class.getName());
            throw new IllegalArgumentException(message);
        }
    }

    private <T> Function<T, Object> createInvocation (final Object object,
                                                      final Method method)
    {
        final Function<T, Object> function = x ->
        {
            try
            {
                return method.invoke(object, x);
            }
            catch (Throwable ex)
            {
                throw new RuntimeException(ex);
            }
        };

        return function;
    }

    /**
     * This method performs a match-attempt.
     *
     * @param tree is the symbolic-expression that this schema may match.
     * @return true, iff the match was successful.
     */
    public boolean match (final Sexpr tree)
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
        final MatchNode match = rootRule.match(state, tree);

        /**
         * If the match-attempt failed, then report the failure;
         * otherwise, execute the user-defined passes and actions.
         */
        if (match == null)
        {
            failureHandler.accept(state.lastSuccess());
            return false;
        }
        else
        {
            executeActions(match);
            return true;
        }
    }

    private void validate ()
    {
        requireRoot();
        checkForUndefinedRules();
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
            final String names = SList.copyOf(undefinedRules.stream().map(name -> new SAtom(name))).toString();
            final String message = "Undefined Rules Detected: " + names;
            throw new IllegalStateException(message);
        }
    }

    /**
     * On successful matches, this method transverses the match-tree
     * in multiple "translation passes" executing user-defined actions.
     *
     * @param tree describes a successful match attempt.
     */
    private void executeActions (final MatchNode tree)
    {
        setupActions.forEach(action -> action.accept(tree.node()));

        for (String pass : passes)
        {
            executeActions(pass, tree);
        }

        closeActions.forEach(action -> action.accept(tree.node()));
    }

    private void executeActions (final String pass,
                                 final MatchNode node)
    {
        if (beforeActions.containsKey(pass) && beforeActions.get(pass).containsKey(node.rule().name()))
        {
            final List<Consumer<Sexpr>> actions = beforeActions.get(pass).get(node.rule().name());
            actions.forEach(action -> action.accept(node.node()));
        }

        node.children().forEach(child -> executeActions(pass, child));

        if (afterActions.containsKey(pass) && afterActions.get(pass).containsKey(node.rule().name()))
        {
            final List<Consumer<Sexpr>> actions = afterActions.get(pass).get(node.rule().name());
            actions.forEach(action -> action.accept(node.node()));
        }
    }
}
