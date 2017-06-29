package com.mackenziehigh.sexpr.schema;

import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.schema.Schema.Rule;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/**
 * An instance of this interfaces represents
 * the successful match of a single schema rule.
 */
public final class MatchNode
{
    private final Rule rule;

    private final Sexpr node;

    private final List<MatchNode> children;

    private final boolean success;

    private final int treeSize;

    /**
     * Constructor.
     *
     * @param rule will be the rule().
     * @param node will be the node().
     * @param children will be the children().
     * @param success is true, if this object describes a successful match.
     */
    public MatchNode (final Rule rule,
                      final Sexpr node,
                      final List<MatchNode> children,
                      final boolean success)
    {
        this.rule = Objects.requireNonNull(rule, "rule");
        this.node = Objects.requireNonNull(node, "node");
        this.children = Collections.unmodifiableList(new ArrayList(Objects.requireNonNull(children, "children")));
        this.success = success;
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

    /**
     * This method executes the attached actions
     * in the proper order.
     */
    public void execute ()
    {
        if (isSuccess())
        {
            rule().before().accept(node());
        }

        children().forEach(x -> x.execute());

        if (isSuccess())
        {
            rule().after().accept(node());
        }
    }

    /**
     * This method determines whether this object
     * represents a successful match attempt.
     *
     * @return true, iff the match succeeded.
     */
    public boolean isSuccess ()
    {
        return success;
    }

    /**
     * This method searches the match-tree for the last
     * successful match and then returns a stack
     * representing the path from the root match-node
     * to the last successful match-node.
     *
     * @return the most recent success on the top of the stack
     * and the root match on the bottom of the stack.
     */
    public Deque<MatchNode> lastSuccess ()
    {
        Deque<MatchNode> stack = new ArrayDeque<>();

        /**
         * If this match is a leaf in the match-tree
         * and represents a successful match,
         * then this is the first node to push
         * onto the stack.
         */
        if (isSuccess() && !children().isEmpty())
        {
            stack.push(this);
            return stack;
        }

        /**
         * Search each child match, recursively,
         * in search of successful matches.
         * Perform the search in reverse order
         * in order to ensure the most-recent
         * match is found first.
         */
        for (int i = children.size(); i > 0; i--)
        {
            stack = children.get(i).lastSuccess();

            if (stack.isEmpty() == false)
            {
                stack.addLast(this);
                break;
            }
        }

        return stack;
    }
}
