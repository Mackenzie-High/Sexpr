package com.mackenziehigh.sexpr;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Symbolic Atom.
 *
 *
 * <p>
 * All instances of this interface are immutable.
 * </p>
 */
public final class SAtom
        implements Sexpr<SAtom>
{
    private final String content;

    private final SourceLocation location;

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final boolean value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final char value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final byte value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final short value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final int value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final long value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final float value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final double value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final Class value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final String value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     */
    public SAtom (final byte[] value)
    {
        this(SourceLocation.DEFAULT, value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final boolean value)
    {
        this(location, Boolean.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final char value)
    {
        this(location, (int) value);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final byte value)
    {
        this(location, Byte.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final short value)
    {
        this(location, Short.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final int value)
    {
        this(location, Integer.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final long value)
    {
        this(location, Long.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final float value)
    {
        this(location, Float.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final double value)
    {
        this(location, Double.toString(value));
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final String value)
    {
        this.content = Objects.requireNonNull(value);
        this.location = Objects.requireNonNull(location);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final Class value)
    {
        this.content = Objects.requireNonNull(value).getName();
        this.location = Objects.requireNonNull(location);
    }

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    public SAtom (final SourceLocation location,
                  final byte[] value)
    {
        final StringBuilder str = new StringBuilder(value.length * 2);

        final String HEX = "0123456789ABCDEF";

        for (int i = 0; i < value.length; i++)
        {
            final char high = HEX.charAt(Byte.toUnsignedInt(value[i]) >> 4);
            final char low = HEX.charAt(Byte.toUnsignedInt(value[i]) & 0b1111);
            str.append(high).append(low);
        }

        this.content = str.toString();
        this.location = Objects.requireNonNull(location);
    }

    /**
     * This method retrieves the series of characters
     * that this atom contains.
     *
     * @return the content of this atom.
     */
    public String content ()
    {
        return content;
    }

    /**
     * This method returns the content() with all
     * special characters escaped.
     *
     * @return the escaped content().
     */
    public String escaped ()
    {
        return Escaper.instance.escape(content().toCharArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAtom ()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isList ()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bfs (final Predicate<Sexpr> condition)
    {
        return condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dfs (final Predicate<Sexpr> condition)
    {
        return condition.test(this);
    }

    @Override
    public SexprMutator<SAtom> mutator ()
    {
        return new SexprMutator(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preorder (final Predicate<Sexpr> condition)
    {
        return condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean postorder (final Predicate<Sexpr> condition)
    {
        return condition.test(this);
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
    public Optional<Class> asClass ()
    {
        return asClass(SAtom.class.getClassLoader());
    }

    /**
     * This method retrieves the class identified by this value.
     *
     * @param loader will be used to search for the class.
     * @return the class, if it is found.
     */
    public Optional<Class> asClass (final ClassLoader loader)
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
     * This method retrieves this value as a byte-array.
     *
     * <p>
     * The content() must be a hexadecimal encoded string.
     * In other words, the content() is a series of character-pairs,
     * where each pair are two hexadecimal digits (case-insensitive).
     * The string is encoded using big-endian.
     * </p>
     *
     * @return this value as a byte-array.
     */
    public Optional<byte[]> asByteArray ()
    {
        if (content.length() % 2 != 0)
        {
            return Optional.empty();
        }

        final byte[] bytes = new byte[content.length() / 2];

        for (int i = 0; i < content.length(); i = i + 2)
        {
            int high = Character.toUpperCase(content.charAt(i + 0));
            high = 48 <= high && high <= 57 ? high - 48 : high;
            high = 65 <= high && high <= 70 ? high - 65 : high;

            int low = Character.toUpperCase(content.charAt(i + 1));
            low = 48 <= low && low <= 57 ? low - 48 : low;
            low = 65 <= low && low <= 70 ? low - 65 : low;

            if (high > 15)
            {
                return Optional.empty();
            }
            else
            {
                bytes[i / 2] = (byte) (high * 16 + low);
            }
        }

        return Optional.of(bytes);
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
    public Optional<Boolean> asBoolean ()
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
     * This method retrieves this value, as a char.
     *
     * @return the value, if possible.
     */
    public Optional<Character> asChar ()
    {
        if ("max".equalsIgnoreCase(content()))
        {
            return Optional.of(Character.MAX_VALUE);
        }
        else if ("maximum".equalsIgnoreCase(content()))
        {
            return Optional.of(Character.MAX_VALUE);
        }
        else if ("min".equalsIgnoreCase(content()))
        {
            return Optional.of(Character.MIN_VALUE);
        }
        else if ("minimum".equalsIgnoreCase(content()))
        {
            return Optional.of(Character.MIN_VALUE);
        }
        else
        {
            final Optional<Long> number = asLong();
            return number.isPresent() ? Optional.of((char) number.get().intValue()) : Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as a byte.
     *
     * @return the value, if possible.
     */
    public Optional<Byte> asByte ()
    {
        if ("max".equalsIgnoreCase(content()))
        {
            return Optional.of(Byte.MAX_VALUE);
        }
        else if ("maximum".equalsIgnoreCase(content()))
        {
            return Optional.of(Byte.MAX_VALUE);
        }
        else if ("min".equalsIgnoreCase(content()))
        {
            return Optional.of(Byte.MIN_VALUE);
        }
        else if ("minimum".equalsIgnoreCase(content()))
        {
            return Optional.of(Byte.MIN_VALUE);
        }
        else
        {
            final Optional<Long> number = asLong();
            return number.isPresent() ? Optional.of(number.get().byteValue()) : Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as a short.
     *
     * @return the value, if possible.
     */
    public Optional<Short> asShort ()
    {
        if ("max".equalsIgnoreCase(content()))
        {
            return Optional.of(Short.MAX_VALUE);
        }
        else if ("maximum".equalsIgnoreCase(content()))
        {
            return Optional.of(Short.MAX_VALUE);
        }
        else if ("min".equalsIgnoreCase(content()))
        {
            return Optional.of(Short.MIN_VALUE);
        }
        else if ("minimum".equalsIgnoreCase(content()))
        {
            return Optional.of(Short.MIN_VALUE);
        }
        else
        {
            final Optional<Long> number = asLong();
            return number.isPresent() ? Optional.of(number.get().shortValue()) : Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as an integer.
     *
     * @return the value, if possible.
     */
    public Optional<Integer> asInt ()
    {
        if ("max".equalsIgnoreCase(content()))
        {
            return Optional.of(Integer.MAX_VALUE);
        }
        else if ("maximum".equalsIgnoreCase(content()))
        {
            return Optional.of(Integer.MAX_VALUE);
        }
        else if ("min".equalsIgnoreCase(content()))
        {
            return Optional.of(Integer.MIN_VALUE);
        }
        else if ("minimum".equalsIgnoreCase(content()))
        {
            return Optional.of(Integer.MIN_VALUE);
        }
        else
        {
            final Optional<Long> number = asLong();
            return number.isPresent() ? Optional.of(number.get().intValue()) : Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as a long.
     *
     * @return the value, if possible.
     */
    public Optional<Long> asLong ()
    {
        try
        {
            if ("max".equalsIgnoreCase(content()))
            {
                return Optional.of(Long.MAX_VALUE);
            }
            else if ("maximum".equalsIgnoreCase(content()))
            {
                return Optional.of(Long.MAX_VALUE);
            }
            else if ("min".equalsIgnoreCase(content()))
            {
                return Optional.of(Long.MIN_VALUE);
            }
            else if ("minimum".equalsIgnoreCase(content()))
            {
                return Optional.of(Long.MIN_VALUE);
            }
            else
            {
                return Optional.of(Long.parseLong(toString()));
            }
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
    public Optional<Float> asFloat ()
    {
        final Optional<Double> number = asDouble();
        return number.isPresent() ? Optional.of(number.get().floatValue()) : Optional.empty();
    }

    /**
     * This method retrieves this value, as a float.
     *
     * @return the value, if possible.
     */
    public Optional<Double> asDouble ()
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
     * {@inheritDoc}
     */
    @Override
    public int treeHeight ()
    {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int treeLeafCount ()
    {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int treeSize ()
    {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceLocation location ()
    {
        return location;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        return 391 * content.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals (final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        else if (obj == null)
        {
            return false;
        }
        else if (obj instanceof SAtom == false)
        {
            return false;
        }
        else
        {
            final SAtom other = (SAtom) obj;
            final boolean result = content.equals(other.content);
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString ()
    {
        final String symbol = content();
        final String string = "@'" + escaped() + "'";
        final String result = escaped().equals(content()) ? symbol : string;
        return result;
    }
}
