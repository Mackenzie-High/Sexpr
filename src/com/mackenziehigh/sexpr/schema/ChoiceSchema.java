package com.mackenziehigh.sexpr.schema;

import java.util.List;

/**
 * Choice Schema.
 */
public interface ChoiceSchema
        extends SexprSchema<ChoiceSchema>
{
    /**
     * This method retrieves the schemas that
     * define the options in this choice.
     *
     * @return the options herein.
     */
    public List<SexprSchema> options ();
}
