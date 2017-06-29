package com.mackenziehigh.sexpr.schema;

import com.mackenziehigh.sexpr.Sexpr;
import java.util.Collections;
import java.util.Objects;
import java.util.SortedMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * An instance of this class is a pattern that describes a symbolic-expression.
 */
public final class Schema
{
    private final Schema SELF = this;

    /**
     * An instance of this class is a constraint on a single node in a symbolic-expression.
     */
    public final class Rule
    {
        private final String name;

        private final BiFunction<Rule, Sexpr, MatchNode> matcher;

        private final Consumer<Sexpr> before;

        private final Consumer<Sexpr> after;

        /**
         * Sole Constructor.
         *
         * @param name will be the name().
         * @param matcher will be the matcher().
         * @param before will be the before().
         * @param after will be the after().
         */
        public Rule (final String name,
                     final BiFunction<Rule, Sexpr, MatchNode> matcher,
                     final Consumer<Sexpr> before,
                     final Consumer<Sexpr> after)
        {
            this.name = Objects.requireNonNull(name, "name");
            this.matcher = Objects.requireNonNull(matcher, "matcher");
            this.before = Objects.requireNonNull(before, "before");
            this.after = Objects.requireNonNull(after, "after");
        }

        /**
         * This method retrieves the name of this rule.
         *
         * @return the name of this rule.
         */
        public String name ()
        {
            return name;
        }

        /**
         * This method retrieves the predicate that will
         * be used to determine whether this rule matches.
         *
         * @return the requirement imposed by this rule.
         */
        public BiFunction<Rule, Sexpr, MatchNode> matcher ()
        {
            return matcher;
        }

        /**
         * This is the action that should be executed
         * before visiting any of the subordinate matches.
         *
         * @return an action.
         */
        public Consumer<Sexpr> before ()
        {
            return before;
        }

        /**
         * This is the action that should be executed
         * after visiting any of the subordinate matches.
         *
         * @return an action.
         */
        public Consumer<Sexpr> after ()
        {
            return after;
        }

        /**
         * This method retrieves the schema that this rule is defined within.
         *
         * @return the enclosing schema.
         */
        public Schema schema ()
        {
            return SELF;
        }

        /**
         * This method determines whether this rule matches
         * a given symbolic-expression.
         *
         *
         * @param node is the symbolic-expression.
         * @return a result indicating whether the match succeeded or failed.
         */
        public MatchNode match (final Sexpr node)
        {
            return matcher().apply(this, Objects.requireNonNull(node, "node"));
        }
    }

    private final Supplier<Rule> root;

    private final Supplier<SortedMap<String, Rule>> rules;

    /**
     * Sole Constructor.
     *
     * @param root lazily provides the root() rule of the schema.
     * @param rules lazily provides the rules() of the schema.
     */
    public Schema (final Supplier<Rule> root,
                   final Supplier<SortedMap<String, Rule>> rules)
    {
        this.root = Objects.requireNonNull(root, "root");
        this.rules = Objects.requireNonNull(rules, "rules");
    }

    /**
     * This method retrieves the root rule of the schema.
     *
     * @return the root rule.
     * @throws IllegalStateException if no root rule was defined.
     */
    public Rule root ()
    {
        return root.get();
    }

    /**
     * This method returns all of the rules defined in this schema.
     *
     * @return a map that maps rule-names to rules.
     */
    public SortedMap<String, Rule> rules ()
    {
        return Collections.unmodifiableSortedMap(rules.get());
    }

    /**
     * This method determines whether this schema
     * matches the given symbolic-expression.
     *
     * @param tree may be matched by this schema.
     * @return the result of the match attempt.
     */
    public MatchNode match (final Sexpr tree)
    {
        return root().match(tree);
    }
}
