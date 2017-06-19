package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.Sexpr;
import java.util.Optional;

/**
 * An instance of this interface is the result
 * of attempting to match a Sexpr using a schema.
 */
public interface MatchResult
{
    /**
     * This method retrieves the root of the tree
     * that describes the individual rule matches.
     *
     * @return the match-tree, if the match succeeded.
     */
    public Optional<MatchNode> matchTree ();

    /**
     * This is the last node that successfully matched.
     *
     * <p>
     * If the match was unsuccessful, then this is
     * useful for debugging the point of failure.
     * </p>
     *
     * @return the last success, or null,
     * if no node successfully matched.
     */
    public Optional<Sexpr> lastSuccess ();

    /**
     * This method determines whether this object
     * represents a successful match attempt.
     *
     * @return true, iff the match succeeded.
     */
    public default boolean isSuccess ()
    {
        return matchTree().isPresent();
    }

    /**
     * This method executes all of the actions
     * in the matchTree() in the proper order.
     *
     * <p>
     * This method does nothing, if the match was unsuccessful.
     * </p>
     */
    public default void execute ()
    {
        if (isSuccess() == false)
        {
            matchTree().get().execute();
        }
    }
}
