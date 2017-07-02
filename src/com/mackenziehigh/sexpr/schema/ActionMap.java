package com.mackenziehigh.sexpr.schema;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Consumer;

/**
 * An instance of this class defines actions that
 * can be performed when traversing a match-tree.
 */
public final class ActionMap
{
    /**
     * An instance of this class defines the actions for a single rule.
     */
    public final class ActionsForRule
    {
        private final List<Consumer<MatchNode>> before = new LinkedList<>();

        private final List<Consumer<MatchNode>> after = new LinkedList<>();

        /**
         * This method defines an action that will be executed immediately
         * before a node matching the rule is encountered.
         *
         * @param action is the action to perform.
         * @return this.
         */
        public ActionsForRule before (final Consumer<MatchNode> action)
        {
            before.add(Objects.requireNonNull(action, "action"));
            return this;
        }

        /**
         * This method defines an action that will be executed immediately
         * after a node matching the rule is encountered.
         *
         * @param action is the action to perform.
         * @return this.
         */
        public ActionsForRule after (final Consumer<MatchNode> action)
        {
            after.add(Objects.requireNonNull(action, "action"));
            return this;
        }
    }

    private final Map<String, ActionsForRule> actions = new TreeMap<>();

    /**
     * This method retrieves the object that is used
     * to add actions for a named rule.
     *
     * <p>
     * This method is named "add" in order to make the API more fluent.
     * However, this method really returns an object whose
     * method will perform the actual addition of actions.
     * </p>
     *
     * @param name is the name of a schema rule.
     * @return the object that defines actions for the given rule.
     */
    public ActionsForRule add (final String name)
    {
        Objects.requireNonNull(name, "name");

        if (actions.containsKey(name) == false)
        {
            actions.put(name, new ActionsForRule());
        }

        return actions.get(name);
    }

    /**
     * This method transverses a given match-tree and executes
     * the actions that are defined herein whenever a relevant
     * node is encountered within the tree.
     *
     * @param node is match-tree.
     * @return this.
     */
    public ActionMap execute (final MatchNode node)
    {
        Objects.requireNonNull(node, "node");

        final ActionsForRule relevantActions = add(node.rule().name());

        relevantActions.before.forEach(x -> x.accept(node));
        node.children().forEach(x -> execute(x));
        relevantActions.after.forEach(x -> x.accept(node));

        return this;
    }
}
