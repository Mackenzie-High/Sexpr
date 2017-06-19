package com.mackenziehigh.sexpr.schema;

import com.mackenziehigh.sexpr.internal.SchemaFactory;

/**
 * Factory for SexprSchema objects.
 */
public interface SexprSchemaFactory
{
    /**
     * Default Instance.
     */
    public static SexprSchemaFactory instance = new SchemaFactory();

    /**
     * This method creates a schema that will match any SAtom.
     *
     * @return the new schema.
     */
    public SAtomSchema atom ();

    /**
     * This method creates a schema that will match any SList.
     *
     * @return the new schema.
     */
    public SListSchema list ();

    /**
     * This method creates an ordered choice schema that will match,
     * if and only if one of the options match.
     *
     * @param options are the options that will be tried in order.
     * @return the new schema.
     */
    public ChoiceSchema firstOf (SexprSchema... options);

}
