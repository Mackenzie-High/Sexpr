package com.mackenziehigh.sexpr.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this method to mark a class or method in order to indicate
 * the translation-pass that the class or method related to.
 */
@Retention (RetentionPolicy.RUNTIME)
@Target (
        {
            ElementType.TYPE,
            ElementType.METHOD
        })
public @interface Pass
{
    /**
     * This method retrieves the user-defined name of the pass.
     *
     * @return the name of the translation-pass.
     */
    public String name ();
}
