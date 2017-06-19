package com.mackenziehigh.sexpr;

import com.mackenziehigh.sexpr.internal.Escaper;
import java.util.Optional;

/**
 * Symbolic Expression - Atom.
 *
 *
 * <p>
 * All instances of this interface are immutable.
 * </p>
 */
public interface SAtom
        extends Sexpr<SAtom>
{

    /**
     * This method retrieves the series of characters
     * that this atom contains.
     *
     * @return the content of this atom.
     */
    public String content ();

    /**
     * This method returns the content() with all
     * special characters escaped.
     *
     * @return the escaped content().
     */
    public default String escaped ()
    {
        return Escaper.instance.escape(content().toCharArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public default boolean isSAtom ()
    {
        return true;
    }

    /**
     * This method retrieves the class identified by this value.
     *
     * <p>
     * This method uses the ClassLoader that loaded this
     * interface in order to search for the class.
     * </p>
     *
     * @return the class, if it is found.
     */
    public default Optional<Class> asClass ()
    {
        return asClass(SAtom.class.getClassLoader());
    }

    /**
     * This method retrieves the class identified by this value.
     *
     * @param loader will be used to search for the class.
     * @return the class, if it is found.
     */
    public default Optional<Class> asClass (final ClassLoader loader)
    {
        try
        {
            return Optional.of(Class.forName(toString(), false, loader));
        }
        catch (RuntimeException | ClassNotFoundException ex)
        {
            return Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as a boolean.
     *
     * <p>
     * If toString() equals (ignoring case) "true", "yes", "on", "T", or "1",
     * then this method will return a true result.
     * </p>
     *
     * <p>
     * If toString() equals (ignoring case) "false", "no", "off", "F", or "0",
     * then this method will return a false result.
     * </p>
     *
     * @return the value, if possible.
     */
    public default Optional<Boolean> asBoolean ()
    {
        switch (toString().toLowerCase())
        {
            case "true":
            case "yes":
            case "on":
            case "t":
            case "1":
                return Optional.of(Boolean.TRUE);
            case "false":
            case "no":
            case "off":
            case "f":
            case "0":
                return Optional.of(Boolean.FALSE);
            default:
                return Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as an integer.
     *
     * @return the value, if possible.
     */
    public default Optional<Long> asInteger ()
    {
        try
        {
            return Optional.of(Long.parseLong(toString()));
        }
        catch (RuntimeException ex)
        {
            return Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as a float.
     *
     * @return the value, if possible.
     */
    public default Optional<Double> asFloat ()
    {
        try
        {
            if ("infinity".equalsIgnoreCase(content()))
            {
                return Optional.of(Double.POSITIVE_INFINITY);
            }
            else if ("inf".equalsIgnoreCase(content()))
            {
                return Optional.of(Double.POSITIVE_INFINITY);
            }
            else if ("-infinity".equalsIgnoreCase(content()))
            {
                return Optional.of(Double.NEGATIVE_INFINITY);
            }
            else if ("-inf".equalsIgnoreCase(content()))
            {
                return Optional.of(Double.NEGATIVE_INFINITY);
            }
            else if ("nan".equalsIgnoreCase(content()))
            {
                return Optional.of(Double.NaN);
            }
            else if ("-nan".equalsIgnoreCase(content()))
            {
                return Optional.of(-Double.NaN);
            }
            else
            {
                return Optional.of(Double.parseDouble(toString()));
            }
        }
        catch (RuntimeException ex)
        {
            return Optional.empty();
        }
    }

    /**
     * This method converts this object to its textual representation.
     *
     * @return the series of characters that comprise this SAtom.
     */
    @Override
    public String toString ();
}
