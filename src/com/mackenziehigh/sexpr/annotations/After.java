package com.mackenziehigh.sexpr.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this annotation on a method in order to indicate that
 * the method is a schema action to perform after successful
 * matches of a rule with the given name.
 */
@Retention (RetentionPolicy.RUNTIME)
public @interface After
{
    /**
     * This is the name of the schema rule that this action applies to.
     *
     * @return the name of a rule in a symbolic-expression schema.
     */
    public String name ();
}
