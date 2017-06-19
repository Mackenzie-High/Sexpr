package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.Sexpr;

/**
 * An instance of this interface is a pattern that describes a Sexpr.
 */
public interface Schema
{
    /**
     * This method determines whether this schema
     * matches the given symbolic-expression.
     *
     * @param tree is the Sexpr that may match this schema.
     * @return the result of the match
     */
    public MatchResult match (Sexpr tree);

}
