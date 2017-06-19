package com.mackenziehigh.sexpr;

/**
 * Symbolic Expression.
 *
 * <p>
 * The textual representations of symbolic expressions are case-sensitive.
 * </p>
 *
 * <p>
 * Symbolic expressions are naturally comparable
 * based on their textual representations.
 * </p>
 *
 * <p>
 * Two symbolic expressions (X) and (Y) are equal,
 * iff X.toString() equals Y.toString().
 * </p>
 *
 * <p>
 * All instances of this interface are immutable.
 * </p>
 *
 * @param <T>
 */
public interface Sexpr<T extends Sexpr<T>>
        extends Comparable<Sexpr>
{
    /**
     * This method determines whether this object is a SAtom.
     *
     * @return true, iff this object is a SAtom.
     */
    public default boolean isSAtom ()
    {
        return false;
    }

    /**
     * This method determines whether this object is a SList.
     *
     * @return true, iff this object is a SAtom.
     */
    public default boolean isSList ()
    {
        return false;
    }

    /**
     * This method retrieves an object describing the source
     * from which this object we obtained.
     *
     * @return the source-code location of this Sexpr.
     */
    public default SourceLocation location ()
    {
        return new SourceLocation()
        {
            // Pass
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public default int compareTo (final Sexpr other)
    {
        return other == null ? 1 : toString().compareTo(other.toString());
    }

    /**
     * This method retrieves the textual representation
     * of this symbolic expression.
     *
     * @return this object as a string.
     */
    @Override
    public String toString ();
}
