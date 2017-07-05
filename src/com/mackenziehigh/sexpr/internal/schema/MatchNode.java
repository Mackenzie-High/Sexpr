package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.internal.schema.Schema.Rule;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * This method prints a description of this match to the output.
     *
     * @param out is where the description will be printed.
     */
    public void print (final PrintStream out)
    {
        if (rule.isAnonymous() == false)
        {
            out.println("BEFORE: " + rule.name());
        }

        if (treeSize == 1 && node.isAtom())
        {
            out.println("ATOM: " + node.toString());
        }
        else
        {
            children().forEach(x -> x.print(out));
        }

        if (rule.isAnonymous() == false)
        {
            out.println("AFTER: " + rule.name());
        }
    }
}
