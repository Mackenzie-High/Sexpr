package com.mackenziehigh.sexpr.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Use this method to mark a method as the implementation
 * of a user-defined condition that can be referenced
 * by 'require' rules within a symbolic-expression schema.
 */
@Retention (RetentionPolicy.RUNTIME)
public @interface Condition
{
    /**
     * This method retrieves user-defined name of the condition.
     *
     * @return the name of the condition.
     */
    public String name ();
}
