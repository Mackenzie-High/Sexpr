package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.Sexpr;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * An instance of this interfaces represents
 * the successful match of a single schema rule.
 */
public interface MatchNode
{
    /**
     * This is the name of the rule that successfully matched.
     *
     * @return the name of the rule.
     */
    public String rule ();

    /**
     * This is the Sexpr that was successfully matched.
     *
     * @return the matched value.
     */
    public Sexpr value ();

    /**
     * If there were any rules that matched sub-parts of the value(),
     * then this method retrieves the related matches.
     *
     * @return an immutable collection of subordinate matches.
     */
    public MatchNode children ();

    /**
     * These are the actions that should be executed
     * before visiting any of the subordinate matches.
     *
     * @return an immutable collection of actions.
     */
    public Collection<Consumer<Sexpr>> before ();

    /**
     * These are the actions that should be executed
     * after visiting any of the subordinate matches.
     *
     * @return an immutable collection of actions.
     */
    public Collection<Consumer<Sexpr>> after ();

    /**
     * This method executes the attached actions
     * in the proper order.
     */
    public default void execute ()
    {
        before().forEach(x -> x.accept(this.value()));
        children().execute();
        after().forEach(x -> x.accept(this.value()));
    }
}
