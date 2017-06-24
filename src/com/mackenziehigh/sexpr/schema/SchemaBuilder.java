package com.mackenziehigh.sexpr.schema;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Use this class to create Schema objects based on a Sexpr grammar.
 */
public interface SchemaBuilder
{
    /**
     * This interface defines methods for selecting
     * the type of rule being defined.
     */
    public interface RuleBuilder
    {
        /**
         * This method defines this rule as the root rule in the schema.
         *
         * @return this.
         */
        public RuleBuilder root ();

        /**
         * This method defines a rule that matches a SAtom only.
         *
         * @return a builder for the rule.
         */
        public Leaf leaf ();

        /**
         * This method defines a rule that consists of a series of options,
         * such that all of the options must successfully match.
         *
         * <p>
         * The options will be tried from first to last.
         * </p>
         *
         * @return a builder for the rule.
         */
        public Conjunction all ();

        /**
         * This method defines a rule that consists of a series of options,
         * such that exactly one of the options must successfully match.
         *
         * <p>
         * The options will be tried from first to last.
         * </p>
         *
         * @return a builder for the rule.
         */
        public Disjunction choice ();

        /**
         * This method defines a rule that consists of a series of options,
         * such that none of the options must successfully match.
         *
         * <p>
         * The options will be tried from first to last.
         * </p>
         *
         * @return a builder for the rule.
         */
        public Negation none ();

        /**
         * This method defines a rule that matches a SList.
         *
         * <p>
         * The rule requires a fixed-length prefix
         * and an optional variable-length suffix.
         * </p>
         *
         * @return a builder for the rule.
         */
        public SequencePrefix list ();

        /**
         * This method defines a rule that matches any Sexpr.
         *
         * @return a builder for the rule.
         */
        public Any any ();
    }

    /**
     * This method defines methods shared by all types of rules.
     *
     * @param <T> is the specific type of rule.
     */
    public interface Rule<T extends Rule<T>>
    {
        /**
         * This method adds an action to execute upon
         * successfully matching this rule.
         *
         * <p>
         * Multiple actions may be defined by repeated
         * calls to this method.
         * </p>
         *
         * @param action is the action to add.
         * @return this.
         */
        public T before (Consumer<? extends Sexpr> action);

        /**
         * This method adds an action to execute upon
         * successfully matching this rule.
         *
         * <p>
         * Multiple actions may be defined by repeated
         * calls to this method.
         * </p>
         *
         * @param action is the action to add.
         * @return this.
         */
        public T after (Consumer<? extends Sexpr> action);
    }

    /**
     * An instance of this interface configures a rule
     * that can only be used to match a SAtom.
     */
    public interface Leaf
            extends Rule<Leaf>
    {
        /**
         * This method specifies that the SAtom must
         * match a given condition.
         *
         * @param condition must return for this rule to match.
         * @return this.
         */
        public Leaf require (Predicate<SAtom> condition);

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * a boolean representation.
         *
         * @return this.
         */
        public default Leaf requireBoolean ()
        {
            require(x -> x.asBoolean().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * a character representation.
         *
         * @return this.
         */
        public default Leaf requireChar ()
        {
            require(x -> x.asChar().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * an integer representation.
         *
         * @return this.
         */
        public default Leaf requireByte ()
        {
            require(x -> x.asByte().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * an integer representation.
         *
         * @return this.
         */
        public default Leaf requireShort ()
        {
            require(x -> x.asShort().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * an integer representation.
         *
         * @return this.
         */
        public default Leaf requireInt ()
        {
            require(x -> x.asInt().isPresent());
            return this;

        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * an integer representation.
         *
         * @return this.
         */
        public default Leaf requireLong ()
        {
            require(x -> x.asLong().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * a floating-point representation.
         *
         * @return this.
         */
        public default Leaf requireFloat ()
        {
            require(x -> x.asFloat().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * a floating-point representation.
         *
         * @return this.
         */
        public default Leaf requireDouble ()
        {
            require(x -> x.asDouble().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only successfully match atoms that have
         * a real Class representation.
         *
         * @return this.
         */
        public default Leaf requireClass ()
        {
            require(x -> x.asClass().isPresent());
            return this;
        }

        /**
         * This method specifies that this rule will
         * only match when the content() of the atom
         * matches the given regular-expression.
         *
         * @param pattern is the regular-expression.
         * @return this.
         */
        public default Leaf requireMatch (String pattern)
        {
            return requireMatch(Pattern.compile(pattern));
        }

        /**
         * This method specifies that this rule will
         * only match when the content() of the atom
         * matches the given regular-expression.
         *
         * @param pattern is the regular-expression.
         * @return this.
         */
        public default Leaf requireMatch (Pattern pattern)
        {
            require(x -> pattern.matcher(x.content()).matches());
            return this;
        }

        /**
         * This method specifies that this rule will only
         * successfully match when the content() of the atom
         * is one of the given values.
         *
         * @param values are the given values.
         * @return this.
         */
        public default Leaf require (String... values)
        {
            Arrays.asList(values).forEach(x -> Objects.requireNonNull(x));
            final Set<String> set = new TreeSet<>();
            set.addAll(Arrays.asList(values));
            require(x -> set.contains(x.content()));
            return this;
        }
    }

    /**
     * An instance of this class configures a rule
     * that consists of a conjunction of other rules.
     */
    public interface Conjunction
            extends Rule<Conjunction>
    {
        /**
         * This method specifies the rules that all
         * must match in order for this rule to match.
         *
         * @param names are the names of rules.
         * @return this.
         */
        public Conjunction of (String... names);
    }

    /**
     * An instance of this class configures a rule
     * that consists of a disjunction of other rules.
     */
    public interface Disjunction
            extends Rule<Disjunction>
    {
        /**
         * This method specifies the rules,
         * one of which must match in order
         * for this rule to match.
         *
         * @param names are the names of rules.
         * @return this.
         */
        public Disjunction of (String... names);
    }

    /**
     * An instance of this class configures a rule that
     * consists of a negated conjunction of other rules.
     */
    public interface Negation
            extends Rule<Negation>
    {
        /**
         * This method specifies the rules that cannot
         * match in order for this rule to match.
         *
         * @param names are the names of rules.
         * @return this.
         */
        public Negation of (String... names);
    }

    /**
     * An instance of this class configures the prefix part
     * of a sequence rule that matches the front of a SList.
     */
    public interface SequencePrefix
            extends Rule<SequencePrefix>
    {
        /**
         * This method specifies that the rule will only successfully match,
         * iff the elements (E1, E2, E3, ... EN) of the SList when a series
         * of rules (R1, R2, R3, ... RN) pairwise match the elements (e.g. R1 == E1).
         *
         * @param names are the names of the rules that must match the elements.
         * @return the suffix part of the sequence rule.
         */
        public SequenceSuffix of (String... names);
    }

    /**
     * An instance of this class configures the suffix part
     * of a sequence rule that matches the tail of a SList.
     */
    public interface SequenceSuffix
            extends Rule<SequenceSuffix>
    {
        /**
         * This method specifies a rule that must successfully
         * match any elements beyond the fixed-length prefix
         * of the SList.
         *
         * @param name is the name of the rule.
         * @return this.
         */
        public SequenceSuffix followedBy (String name);

        /**
         * This method specifies that the length of the suffix
         * must be at least a given number of elements.
         *
         * @param count is the required minimum length.
         * @return this.
         */
        public SequenceSuffix min (int count);

        /**
         * This method specifies that the length of the suffix
         * must be at most a given number of elements.
         *
         * @param count is the required maximum length.
         * @return this.
         */
        public SequenceSuffix max (int count);
    }

    /**
     * An instance of this interfaces configures a rule
     * that will successfully match any Sexpr.
     */
    public interface Any
            extends Rule<Any>
    {
        // Pass
    }

    /**
     * This method adds another rule to the schema.
     *
     * @param name will be the name of the new rule.
     * @return an object that can be used to configure the new rule.
     */
    public RuleBuilder define (String name);

    /**
     * This method creates the schema described by this grammar.
     *
     * @return the schema.
     * @throws IllegalStateException if this method was already invoked.
     */
    public Schema build ();
}
