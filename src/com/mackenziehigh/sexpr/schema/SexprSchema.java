package com.mackenziehigh.sexpr.schema;

import com.mackenziehigh.sexpr.Sexpr;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Symbolic Expression Schema.
 *
 * @param <T>
 */
public interface SexprSchema<T extends SexprSchema<T>>
{
    /**
     * These are the predicates that this schema enforces.
     *
     * @return an immutable list of predicates.
     */
    public List<Predicate<Sexpr>> predicates ();

    /**
     * This method retrieves the actions that were added with before(*).
     *
     * @return the before(*) actions that are bound to this schema.
     */
    public List<Consumer<T>> beforeActions ();

    /**
     * This method retrieves the actions that were added with after(*).
     *
     * @return the after(*) actions that are bound to this schema.
     */
    public List<Consumer<T>> afterActions ();

    /**
     * This method asserts that the Sexpr must obey a given condition.
     *
     * @param condition is the condition that the Sexpr must obey.
     * @return the new schema.
     */
    public T require (Predicate<Sexpr> condition);

    /**
     * This method binds an action to this schema that
     * will be performed when the schema matches.
     *
     * @param action is the action to perform.
     * @return the new schema.
     */
    public T before (Consumer<T> action);

    /**
     * This method binds an action to this schema that
     * will be performed when the schema matches.
     *
     * @param action is the action to perform.
     * @return the new schema.
     */
    public T after (Consumer<T> action);
}
