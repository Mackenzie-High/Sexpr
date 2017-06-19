package com.mackenziehigh.sexpr.schema;

import com.mackenziehigh.sexpr.SAtom;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Symbolic Atom Schema.
 */
public interface SAtomSchema
        extends SexprSchema<SAtomSchema>
{
    /**
     * This method asserts that the atom must have a given textual representation.
     *
     * @param value is the exact required text.
     * @return a modified copy of this object.
     */
    public default SAtomSchema require (final String value)
    {
        return require(x -> x.isSAtom() && ((SAtom) x).content().equals(value));
    }

    /**
     * This method asserts that the atom must have a boolean representation.
     *
     * @return a modified copy of this object.
     */
    public default SAtomSchema requireBoolean ()
    {
        return require(x -> x.isSAtom() && ((SAtom) x).asBoolean().isPresent());
    }

    /**
     * This method asserts that the atom must have an integer representation.
     *
     * @return a modified copy of this object.
     */
    public default SAtomSchema requireInteger ()

    {
        return require(x -> x.isSAtom() && ((SAtom) x).asInteger().isPresent());
    }

    /**
     * This method asserts that the atom must have a floating-point representation.
     *
     * @return a modified copy of this object.
     */
    public default SAtomSchema requireFloat ()
    {
        return require(x -> x.isSAtom() && ((SAtom) x).asFloat().isPresent());
    }

    /**
     * This method asserts that the atom must have a real Class representation.
     *
     * @return a modified copy of this object.
     */
    public default SAtomSchema requireClass ()
    {
        return require(x -> x.isSAtom() && ((SAtom) x).asClass().isPresent());
    }

    /**
     * This method asserts that the atom must have a textual representation
     * that matches a given regular-expression.
     *
     * @param pattern is the given regular-expression.
     * @return a modified copy of this object.
     */
    public default SAtomSchema matches (String pattern)
    {
        return matches(Pattern.compile(pattern));
    }

    /**
     * This method asserts that the atom must have a textual representation
     * that matches a given regular-expression.
     *
     * @param pattern is the given regular-expression.
     * @return a modified copy of this object.
     */
    public default SAtomSchema matches (final Pattern pattern)
    {
        final Predicate<String> matcher = pattern.asPredicate();
        return require(x -> x.isSAtom() && matcher.test(((SAtom) x).content()));
    }
}
