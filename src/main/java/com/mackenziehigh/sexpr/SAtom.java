/*
 * Copyright 2017 Michael Mackenzie High
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mackenziehigh.sexpr;

import com.mackenziehigh.sexpr.internal.Escaper;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Symbolic Atom.
 *
 * <p>
 * All instances of this interface are immutable.
 * </p>
 */
public final class SAtom
        implements Sexpr<SAtom>
{
    /**
     * This is the content() of this atom.
     */
    private final String content;

    /**
     * This is the content() in a format that would be recognized by the parser.
     */
    private final String parsableContent;

    /**
     * This is the location of this atom in an input-string,
     * if this atom was obtained by parsing an input-string.
     */
    private final SourceLocation location;

    /**
     * Cached Value.
     */
    private Optional<Boolean> valueAsBoolean = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Character> valueAsChar = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Byte> valueAsByte = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Short> valueAsShort = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Integer> valueAsInt = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Long> valueAsLong = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Float> valueAsFloat = Optional.empty();

    /**
     * Cached Value.
     */
    private Optional<Double> valueAsDouble = Optional.empty();

    /**
     * Cached Hash Code.
     */
    private final int hash;

    /**
     * Constructor.
     *
     * @param value will be the content() of this atom.
     * @param location will be the location() of this atom.
     */
    private SAtom (final SourceLocation location,
                   final String value)
    {
        this.content = Objects.requireNonNull(value);
        this.location = Objects.requireNonNull(location);
        this.hash = value.hashCode();
        this.parsableContent = createParsableContent();
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromBoolean (final boolean value)
    {
        return fromBoolean(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromChar (final char value)
    {
        return fromChar(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromByte (final byte value)
    {
        return fromByte(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromShort (final short value)
    {
        return fromShort(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromInt (final int value)
    {
        return fromInt(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromLong (final long value)
    {
        return fromLong(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromFloat (final float value)
    {
        return fromFloat(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromDouble (final double value)
    {
        return fromDouble(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromString (final String value)
    {
        return fromString(SourceLocation.DEFAULT, value);
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromBoolean (final SourceLocation location,
                                     final boolean value)
    {
        return new SAtom(location, Boolean.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromChar (final SourceLocation location,
                                  final char value)
    {
        return fromInt(location, (int) value);
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromByte (final SourceLocation location,
                                  final byte value)
    {
        return new SAtom(location, Byte.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromShort (final SourceLocation location,
                                   final short value)
    {
        return new SAtom(location, Short.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromInt (final SourceLocation location,
                                 final int value)
    {
        return new SAtom(location, Integer.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromLong (final SourceLocation location,
                                  final long value)
    {
        return new SAtom(location, Long.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromFloat (final SourceLocation location,
                                   final float value)
    {
        return new SAtom(location, Float.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromDouble (final SourceLocation location,
                                    final double value)
    {
        return new SAtom(location, Double.toString(value));
    }

    /**
     * Factory Method.
     *
     * @param location will be the <code>location()</code> of the new atom.
     * @param value will be the <code>content()</code> of the new atom.
     * @return the new atom.
     */
    public static SAtom fromString (final SourceLocation location,
                                    final String value)
    {
        return new SAtom(location, value);
    }

    private String createParsableContent ()
    {
        /**
         * If the content() contains whitespace, parentheses,
         * at-symbols, single-quotes, double-quotes, or is empty,
         * then escape and quote the string; otherwise,
         * return the content() itself.
         */
        if (content.isEmpty() || content.matches("[^\\s\\t\\r\\n()@'\"]+"))
        {
            return content();
        }
        else
        {
            return "'" + escaped() + "'";
        }
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
    public boolean bfs (final Predicate<Sexpr<?>> condition)
    {
        return condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dfs (final Predicate<Sexpr<?>> condition)
    {
        return condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preorder (final Predicate<Sexpr<?>> condition)
    {
        return condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean postorder (final Predicate<Sexpr<?>> condition)
    {
        return condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void traverse (final Consumer<Sexpr<?>> before,
                          final Consumer<Sexpr<?>> after)
    {
        before.accept(this);
        after.accept(this);
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
        if (valueAsBoolean.isPresent())
        {
            return valueAsBoolean;
        }

        switch (toString().toLowerCase())
        {
            case "true":
            case "yes":
            case "on":
            case "t":
            case "1":
                return valueAsBoolean = Optional.of(Boolean.TRUE);
            case "false":
            case "no":
            case "off":
            case "f":
            case "0":
                return valueAsBoolean = Optional.of(Boolean.FALSE);
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
        if (valueAsChar.isPresent())
        {
            return valueAsChar;
        }
        else if ("max".equalsIgnoreCase(content()))
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
        else if (asInt().isPresent() && asInt().get() >= Character.MIN_VALUE && asInt().get() <= Character.MAX_VALUE)
        {
            valueAsChar = Optional.of((char) (int) asInt().get());
            return valueAsChar;
        }
        else
        {
            return Optional.empty();
        }
    }

    /**
     * This method retrieves this value, as a byte.
     *
     * @return the value, if possible.
     */
    public Optional<Byte> asByte ()
    {
        if (valueAsByte.isPresent())
        {
            return valueAsByte;
        }
        else if ("max".equalsIgnoreCase(content()))
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
            try
            {
                valueAsByte = Optional.of(Byte.parseByte(content()));
                return valueAsByte;
            }
            catch (RuntimeException ex)
            {
                return Optional.empty();
            }
        }
    }

    /**
     * This method retrieves this value, as a short.
     *
     * @return the value, if possible.
     */
    public Optional<Short> asShort ()
    {
        if (valueAsShort.isPresent())
        {
            return valueAsShort;
        }
        else if ("max".equalsIgnoreCase(content()))
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
            try
            {
                valueAsShort = Optional.of(Short.parseShort(content()));
                return valueAsShort;
            }
            catch (RuntimeException ex)
            {
                return Optional.empty();
            }
        }
    }

    /**
     * This method retrieves this value, as an integer.
     *
     * @return the value, if possible.
     */
    public Optional<Integer> asInt ()
    {
        if (valueAsInt.isPresent())
        {
            return valueAsInt;
        }
        else if ("max".equalsIgnoreCase(content()))
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
            try
            {
                valueAsInt = Optional.of(Integer.parseInt(content()));
                return valueAsInt;
            }
            catch (RuntimeException ex)
            {
                return Optional.empty();
            }
        }
    }

    /**
     * This method retrieves this value, as a long.
     *
     * @return the value, if possible.
     */
    public Optional<Long> asLong ()
    {

        if (valueAsLong.isPresent())
        {
            return valueAsLong;
        }
        else if ("max".equalsIgnoreCase(content()))
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
            try
            {
                valueAsLong = Optional.of(Long.parseLong(content()));
                return valueAsLong;
            }
            catch (RuntimeException ex)
            {
                return Optional.empty();
            }
        }
    }

    /**
     * This method retrieves this value, as a float.
     *
     * @return the value, if possible.
     */
    public Optional<Float> asFloat ()
    {
        if (valueAsFloat.isPresent())
        {
            return valueAsFloat;
        }
        else
        {
            final Optional<Double> number = asDouble();
            valueAsFloat = number.isPresent() ? Optional.of(number.get().floatValue()) : Optional.empty();
            return valueAsFloat;
        }
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
            if (valueAsDouble.isPresent())
            {
                return valueAsDouble;
            }
            else if ("infinity".equalsIgnoreCase(content()))
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
                valueAsDouble = Optional.of(Double.parseDouble(toString()));
                return valueAsDouble;
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
        return hash;
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
            final boolean result = hash == other.hash && toString().equals(other.toString());
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString ()
    {
        return parsableContent;
    }
}
