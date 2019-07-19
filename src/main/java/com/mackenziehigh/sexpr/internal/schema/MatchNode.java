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

import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.internal.schema.Schema.Rule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * An instance of this interfaces represents
 * the successful match of a single schema rule.
 */
final class MatchNode
{
    private final Rule rule;

    private final Sexpr node;

    private final List<MatchNode> children;

    private final int treeSize;

    /**
     * Constructor.
     *
     * @param rule will be the rule().
     * @param node will be the node().
     * @param children will be the children().
     */
    public MatchNode (final Rule rule,
                      final Sexpr node,
                      final List<MatchNode> children)
    {
        this.rule = Objects.requireNonNull(rule, "rule");
        this.node = Objects.requireNonNull(node, "node");
        this.children = Collections.unmodifiableList(new ArrayList(Objects.requireNonNull(children, "children")));
        this.treeSize = children.stream().mapToInt(x -> x.treeSize()).sum() + 1;
    }

    /**
     * This is the the rule that this result describes.
     *
     * @return the creator of this object.
     */
    public Rule rule ()
    {
        return rule;
    }

    /**
     * This is the symbolic-expression that was successfully matched.
     *
     * <p>
     * Usually, this is the root of a proper sub-tree
     * from within a larger symbolic-expression.
     * </p>
     *
     * @return the matched value.
     */
    public Sexpr node ()
    {
        return node;
    }

    /**
     * If there were any rules that matched sub-parts of the value(),
     * then this method retrieves the related matches.
     *
     * @return an immutable collection of subordinate matches.
     */
    public List<MatchNode> children ()
    {
        return children;
    }

    /**
     * This method retrieves the number of matches
     * in the match-tree rooted at this node.
     *
     * @return the size of this match-tree.
     */
    public int treeSize ()
    {
        return treeSize;
    }
}
