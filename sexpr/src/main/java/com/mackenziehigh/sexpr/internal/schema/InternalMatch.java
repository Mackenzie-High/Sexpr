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

import com.mackenziehigh.sexpr.Schema.Match;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 *
 * @author mackenzie
 */
final class InternalMatch
        implements Match
{
    private final InternalMatchNode root;

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
    private final Map<String, Map<String, List<Consumer<Sexpr<?>>>>> beforeActions;

    /**
     * This map maps the name of a translation pass (P) to a map that maps the name
     * of a rule (R) in the schema to a list of user-defined actions (A1 ... AN)
     * that will be performed for each successful match of (R) during pass (P).
     */
    private final Map<String, Map<String, List<Consumer<Sexpr<?>>>>> afterActions;

    public InternalMatch (final boolean success,
                          final InternalMatchNode root,
                          final Sexpr<?> lastSuccess,
                          final List<String> passes,
                          final Map<String, Map<String, List<Consumer<Sexpr<?>>>>> beforeActions,
                          final Map<String, Map<String, List<Consumer<Sexpr<?>>>>> afterActions)
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
    public Sexpr<?> input ()
    {
        return root.node();
    }

    @Override
    public Optional<Sexpr<?>> lastSuccess ()
    {
        return Optional.ofNullable(lastSuccess);
    }

    @Override
    public Match execute ()
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
    private void executeActions (final InternalMatchNode tree)
    {
        passes.forEach(pass -> executeActions(pass, tree));
    }

    private void executeActions (final String pass,
                                 final InternalMatchNode node)
    {
        if (beforeActions.containsKey(pass) && beforeActions.get(pass).containsKey(node.rule().name()))
        {
            final List<Consumer<Sexpr<?>>> actions = beforeActions.get(pass).get(node.rule().name());
            actions.forEach(action -> action.accept(node.node()));
        }

        node.children().forEach(child -> executeActions(pass, child));

        if (afterActions.containsKey(pass) && afterActions.get(pass).containsKey(node.rule().name()))
        {
            final List<Consumer<Sexpr<?>>> actions = afterActions.get(pass).get(node.rule().name());
            actions.forEach(action -> action.accept(node.node()));
        }
    }

}
