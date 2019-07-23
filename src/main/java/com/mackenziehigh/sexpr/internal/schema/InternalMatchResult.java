package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.Schema.MatchResult;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author mackenzie
 */
final class InternalMatchResult
        implements MatchResult
{
    private final MatchNode root;

    private final boolean success;

    private final Sexpr<?> lastSuccess;

    /**
     * These are the names of the user-defined translation passes.
     */
    private final List<String> passes;

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr>>>> beforeActions;

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr>>>> afterActions;

    public InternalMatchResult (final boolean success,
                                final MatchNode root,
                                final Sexpr<?> lastSuccess,
                                final List<String> passes,
                                final Map<String, Map<String, List<Consumer<Sexpr>>>> beforeActions,
                                final Map<String, Map<String, List<Consumer<Sexpr>>>> afterActions)
    {
        this.success = success;
        this.root = root;
        this.lastSuccess = lastSuccess;
        this.passes = List.copyOf(passes);
        this.beforeActions = Map.copyOf(beforeActions);
        this.afterActions = Map.copyOf(afterActions);
    }

    @Override
    public boolean isSuccess ()
    {
        return success;
    }

    @Override
    public boolean isFailure ()
    {
        return !isSuccess();
    }

    @Override
    public Sexpr<?> root ()
    {
        return root.node();
    }

    @Override
    public Optional<Sexpr<?>> lastSuccess ()
    {
        return Optional.ofNullable(lastSuccess);
    }

    @Override
    public MatchResult execute ()
    {
        executeActions(root);
        return this;
    }

    /**
     * On successful matches, this method transverses the match-tree
     * in multiple "translation passes" executing user-defined actions.
     *
     * @param tree describes a successful match attempt.
     */
    private void executeActions (final MatchNode tree)
    {
        passes.forEach(pass -> executeActions(pass, tree));
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
