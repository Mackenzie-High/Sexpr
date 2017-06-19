package com.mackenziehigh.sexpr.schema;

import java.util.List;

/**
 * Symbolic List Schema.
 */
public interface SListSchema
        extends SexprSchema<SListSchema>
{
    /**
     * This method retrieves the schemas that define
     * the constant-length prefix of the list.
     *
     * @return the prefix schemas.
     */
    public List<SexprSchema> prefix ();

    /**
     * This method retrieves the schemas that define
     * the variable-length suffix of the list.
     *
     * @return the suffix schema.
     */
    public SexprSchema suffix ();

    /**
     * This method sets the constant-length
     * prefix that the SList must contain.
     *
     * <p>
     * If a prefix was previously specified,
     * then it will effectively be replaced.
     * </p>
     *
     * @param elements describe the prefix.
     * @return a modified copy of this schema.
     */
    public SListSchema prefix (SexprSchema... elements);

    /**
     * This method adds an element to the prefix
     * that the SList must contain.
     *
     * @param element is describes an element in the prefix.
     * @return a modified copy of this schema.
     */
    public SListSchema then (SexprSchema element);

    /**
     * This method sets the variable-length
     * suffix that the SList must contain.
     *
     * <p>
     * If a suffix was previously specified,
     * then it will effectively be replaced.
     * </p>
     *
     * @param elements describe the suffix.
     * @return a modified copy of this schema.
     */
    public SListSchema suffix (SexprSchema elements);
}
