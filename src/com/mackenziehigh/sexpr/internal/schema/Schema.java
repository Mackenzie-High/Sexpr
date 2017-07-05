package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.Sexpr;
import java.util.Optional;
import java.util.SortedMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An instance of this interface is a pattern that describes a symbolic-expression.
 */
public interface Schema
{
    /**
     * An instance of this interface is a constraint on a single node in a symbolic-expression.
     */
    public interface Rule
    {
        /**
         * This method retrieves the name of this rule.
         *
         * @return the name of this rule.
         */
        public String name ();

        /**
         * This method determines whether this is an implicitly named rule.
         *
         * @return false, iff the user named this rule.
         */
        public boolean isAnonymous ();
    }

    /**
     * This method retrieves the root rule of the schema.
     *
     * @return the root rule.
     * @throws IllegalStateException if no root rule was defined.
     */
    public Rule root ();

    /**
     * This method returns all of the rules defined in this schema.
     *
     * @return a map that maps rule-names to rules.
     */
    public SortedMap<String, Rule> rules ();

    /**
     * This method determines whether a this schema
     * matches a given symbolic-expression.
     *
     * <p>
     * If the match is successful, then the onSuccess function
     * will be invoked passing-in the root of the match-tree
     * that describes the individual successful rule matches.
     * </p>
     *
     * <p>
     * If the match is unsuccessful, then the onFailure function
     * will be invoked passing-in the highest numbered node that
     * was successfully matched. If no node was successfully matched,
     * then an empty optional will be passed-in to the function.
     * Conceptually, the highest numbered node that is successfully
     * matched will be close to the site of failure; therefore,
     * the location of the node is useful for generating human
     * readable error-messages indication the approximate
     * location of the failure-to-match. Nodes are numbered
     * in accordance with a post-order transversal of the tree.
     * </p>
     *
     * @param tree is the symbolic-expression that this schema may match.
     * @param onSuccess will be invoked, iff the match is successful.
     * @param onFailure will be invoked, iff the match is unsuccessful.
     * @return true, iff the match was successful.
     */
    public boolean match (Sexpr tree,
                          Function<String, Predicate<Sexpr>> resolveCondition,
                          Function<String, Consumer<Sexpr>> resolveAction,
                          Consumer<MatchNode> onSuccess,
                          Consumer<Optional<Sexpr>> onFailure);
}
