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

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class SAtomTest
{
    /**
     * Test: 20170617092746028547
     *
     * <p>
     * Method: <code>content()</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617092746028547 ()
    {
        System.out.println("Test: 20170617092746028547");

        final StringBuilder content = new StringBuilder(70100);
        IntStream.rangeClosed(1, Character.MAX_VALUE).forEach(i -> content.append((char) i));
        final String expected = content.toString();
        final SAtom atom = SAtom.fromString(expected);
        final String actual = atom.content();
        assertEquals(expected, actual);

    }

    /**
     * Test: 20170617092746028626
     *
     * <p>
     * Method: <code>escaped</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617092746028626 ()
    {
        System.out.println("Test: 20170617092746028626");

        final String expected = "a\\tb";
        final String actual = SAtom.fromString("a\tb").escaped();
        assertEquals(expected, actual);
    }

    /**
     * Test: 20170617092746028665
     *
     * <p>
     * Method: <code>isSAtom()</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617092746028665 ()
    {
        System.out.println("Test: 20170617092746028665");

        assertTrue(SAtom.fromString("(x)").isAtom());
    }

    /**
     * Test: 20170617092746028682
     *
     * <p>
     * Method: <code>isSList()</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617092746028682 ()
    {
        System.out.println("Test: 20170617092746028682");

        assertFalse(SAtom.fromString("(x)").isList());
    }

    /**
     * Test: 20170617092746028696
     *
     * <p>
     * Method: <code>asClass()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617092746028696 ()
    {
        System.out.println("Test: 20170617092746028696");

        assertEquals(String.class, SAtom.fromString("java.lang.String").asClass().get());
        assertFalse(SAtom.fromString("MyString").asClass().isPresent());
        assertFalse(SAtom.fromString("123").asClass().isPresent());
    }

    /**
     * Test: 20170617092746028708
     *
     * <p>
     * Method: <code>asBoolean()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617092746028708 ()
    {
        System.out.println("Test: 20170617092746028708");

        // True, lowercase.
        assertTrue(SAtom.fromString("true").asBoolean().get());
        assertTrue(SAtom.fromString("yes").asBoolean().get());
        assertTrue(SAtom.fromString("on").asBoolean().get());
        assertTrue(SAtom.fromString("t").asBoolean().get());
        assertTrue(SAtom.fromString("1").asBoolean().get());

        // True, uppercase.
        assertTrue(SAtom.fromString("TRUE").asBoolean().get());
        assertTrue(SAtom.fromString("YES").asBoolean().get());
        assertTrue(SAtom.fromString("ON").asBoolean().get());
        assertTrue(SAtom.fromString("T").asBoolean().get());

        // False, lowercase.
        assertFalse(SAtom.fromString("false").asBoolean().get());
        assertFalse(SAtom.fromString("no").asBoolean().get());
        assertFalse(SAtom.fromString("off").asBoolean().get());
        assertFalse(SAtom.fromString("f").asBoolean().get());
        assertFalse(SAtom.fromString("0").asBoolean().get());

        // False, uppercase.
        assertFalse(SAtom.fromString("FALSE").asBoolean().get());
        assertFalse(SAtom.fromString("NO").asBoolean().get());
        assertFalse(SAtom.fromString("OFF").asBoolean().get());
        assertFalse(SAtom.fromString("F").asBoolean().get());

        // Neither True not False.
        assertFalse(SAtom.fromString("X").asBoolean().isPresent());
    }

    /**
     * Test: 20170624041328530777
     *
     * <p>
     * Method: <code>asChar()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624041328530777 ()
    {
        System.out.println("Test: 20170624041328530777");

        // All Numeric Values
        for (int i = Character.MIN_VALUE; i <= Character.MAX_VALUE; i++)
        {
            assertEquals((char) i, (char) SAtom.fromString(Integer.toString((int) i)).asChar().get());
        }

        // Named Values
        assertEquals(Character.MAX_VALUE, (char) SAtom.fromString("MAXIMUM").asChar().get());
        assertEquals(Character.MIN_VALUE, (char) SAtom.fromString("MINIMUM").asChar().get());
        assertEquals(Character.MAX_VALUE, (char) SAtom.fromString("MAX").asChar().get());
        assertEquals(Character.MIN_VALUE, (char) SAtom.fromString("MIN").asChar().get());
        assertEquals(Character.MAX_VALUE, (char) SAtom.fromString("max").asChar().get());
        assertEquals(Character.MIN_VALUE, (char) SAtom.fromString("min").asChar().get());

        // Overflow
        // Use Integer MAX_VALUE and MIN_VALUE here, instead of Character MAX_VALUE and MIN_VALUE.
        assertFalse(SAtom.fromString(Integer.toString(Integer.MAX_VALUE) + "000").asChar().isPresent());
        assertFalse(SAtom.fromString(Integer.toString(Integer.MIN_VALUE) + "000").asChar().isPresent());

        // Non Numeric
        assertFalse(SAtom.fromString("NaN").asChar().isPresent());
    }

    /**
     * Test: 20170624035717716224
     *
     * <p>
     * Method: <code>asByte()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624035717716224 ()
    {
        System.out.println("Test: 20170624035717716224");

        // All Numeric Values
        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++)
        {
            assertEquals((byte) i, (byte) SAtom.fromString(Byte.toString((byte) i)).asByte().get());
        }

        // Named Values
        assertEquals(Byte.MAX_VALUE, (byte) SAtom.fromString("MAXIMUM").asByte().get());
        assertEquals(Byte.MIN_VALUE, (byte) SAtom.fromString("MINIMUM").asByte().get());
        assertEquals(Byte.MAX_VALUE, (byte) SAtom.fromString("MAX").asByte().get());
        assertEquals(Byte.MIN_VALUE, (byte) SAtom.fromString("MIN").asByte().get());
        assertEquals(Byte.MAX_VALUE, (byte) SAtom.fromString("max").asByte().get());
        assertEquals(Byte.MIN_VALUE, (byte) SAtom.fromString("min").asByte().get());

        // Overflow
        assertFalse(SAtom.fromString(Byte.toString(Byte.MAX_VALUE) + "000").asByte().isPresent());
        assertFalse(SAtom.fromString(Byte.toString(Byte.MIN_VALUE) + "000").asByte().isPresent());

        // Non Numeric
        assertFalse(SAtom.fromString("NaN").asByte().isPresent());
    }

    /**
     * Test: 20170624035717716326
     *
     * <p>
     * Method: <code>asShort()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624035717716326 ()
    {
        System.out.println("Test: 20170624035717716326");

        // All Numeric Values
        for (int i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++)
        {
            assertEquals((short) i, (short) SAtom.fromString(Short.toString((short) i)).asShort().get());
        }

        // Named Values
        assertEquals(Short.MAX_VALUE, (short) SAtom.fromString("MAXIMUM").asShort().get());
        assertEquals(Short.MIN_VALUE, (short) SAtom.fromString("MINIMUM").asShort().get());
        assertEquals(Short.MAX_VALUE, (short) SAtom.fromString("MAX").asShort().get());
        assertEquals(Short.MIN_VALUE, (short) SAtom.fromString("MIN").asShort().get());
        assertEquals(Short.MAX_VALUE, (short) SAtom.fromString("max").asShort().get());
        assertEquals(Short.MIN_VALUE, (short) SAtom.fromString("min").asShort().get());

        // Overflow
        assertFalse(SAtom.fromString(Short.toString(Short.MAX_VALUE) + "000").asShort().isPresent());
        assertFalse(SAtom.fromString(Short.toString(Short.MIN_VALUE) + "000").asShort().isPresent());

        // Non Numeric
        assertFalse(SAtom.fromString("NaN").asShort().isPresent());
    }

    /**
     * Test: 20170624035717716359
     *
     * <p>
     * Method: <code>asInt()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624035717716359 ()
    {
        System.out.println("Test: 20170624035717716359");

        // Numeric Values
        assertEquals(Integer.MAX_VALUE, (int) SAtom.fromString(Integer.toString(Integer.MAX_VALUE)).asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) SAtom.fromString(Integer.toString(Integer.MIN_VALUE)).asInt().get());

        // Named Values
        assertEquals(Integer.MAX_VALUE, (int) SAtom.fromString("MAXIMUM").asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) SAtom.fromString("MINIMUM").asInt().get());
        assertEquals(Integer.MAX_VALUE, (int) SAtom.fromString("MAX").asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) SAtom.fromString("MIN").asInt().get());
        assertEquals(Integer.MAX_VALUE, (int) SAtom.fromString("max").asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) SAtom.fromString("min").asInt().get());

        // Overflow
        assertFalse(SAtom.fromString(Integer.toString(Integer.MAX_VALUE) + "000").asInt().isPresent());
        assertFalse(SAtom.fromString(Integer.toString(Integer.MIN_VALUE) + "000").asInt().isPresent());

        // Non Numeric
        assertFalse(SAtom.fromString("NaN").asInt().isPresent());
    }

    /**
     * Test: 20170617092746028723
     *
     * <p>
     * Method: <code>asLong()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617092746028723 ()
    {
        System.out.println("Test: 20170617092746028723");

        // Numeric Values
        assertEquals(Long.MAX_VALUE, (long) SAtom.fromString(Long.toString(Long.MAX_VALUE)).asLong().get());
        assertEquals(Long.MIN_VALUE, (long) SAtom.fromString(Long.toString(Long.MIN_VALUE)).asLong().get());

        // Named Values
        assertEquals(Long.MAX_VALUE, (long) SAtom.fromString("MAXIMUM").asLong().get());
        assertEquals(Long.MIN_VALUE, (long) SAtom.fromString("MINIMUM").asLong().get());
        assertEquals(Long.MAX_VALUE, (long) SAtom.fromString("MAX").asLong().get());
        assertEquals(Long.MIN_VALUE, (long) SAtom.fromString("MIN").asLong().get());
        assertEquals(Long.MAX_VALUE, (long) SAtom.fromString("max").asLong().get());
        assertEquals(Long.MIN_VALUE, (long) SAtom.fromString("min").asLong().get());

        // Overflow
        assertFalse(SAtom.fromString(Long.toString(Long.MAX_VALUE) + "000").asLong().isPresent());
        assertFalse(SAtom.fromString(Long.toString(Long.MIN_VALUE) + "000").asLong().isPresent());

        // Non Numeric
        assertFalse(SAtom.fromString("NaN").asLong().isPresent());
    }

    /**
     * Test: 20170624061055956220
     *
     * <p>
     * Method: <code>asFloat()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624061055956220 ()
    {
        System.out.println("Test: 20170624061055956220");

        // Constant Numeric Values
        assertEquals(Float.MAX_VALUE, SAtom.fromString(Float.toString(Float.MAX_VALUE)).asFloat().get(), 0.1);
        assertEquals(Float.MIN_VALUE, SAtom.fromString(Float.toString(Float.MIN_VALUE)).asFloat().get(), 0.1);
        assertEquals(Float.NaN, SAtom.fromString(Float.toString(Float.NaN)).asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, SAtom.fromString(Float.toString(Float.POSITIVE_INFINITY)).asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, SAtom.fromString(Float.toString(Float.NEGATIVE_INFINITY)).asFloat().get(), 0.1);

        // Random Numeric Values
        final Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10000; i++)
        {
            final float expected = random.nextFloat();
            final float actual = SAtom.fromString(Float.toString(expected)).asFloat().get();
            assertEquals(expected, actual, 0.001);
        }

        // Overflow
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString(Float.toString(Float.MAX_VALUE) + "000").asFloat().get(), 0.1);

        // Non Numeric
        assertFalse(SAtom.fromString("XYZ").asFloat().isPresent());

        // Case Insensitive Special Vaues
        assertEquals(Float.NaN, SAtom.fromString("NAN").asFloat().get(), 0.1);
        assertEquals(Float.NaN, SAtom.fromString("-NAN").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, SAtom.fromString("INFINITY").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, SAtom.fromString("-INFINITY").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, SAtom.fromString("INF").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, SAtom.fromString("-INF").asFloat().get(), 0.1);

        assertEquals(Float.NaN, SAtom.fromString("nan").asFloat().get(), 0.1);
        assertEquals(Float.NaN, SAtom.fromString("-nan").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, SAtom.fromString("infinity").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, SAtom.fromString("-infinity").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, SAtom.fromString("inf").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, SAtom.fromString("-inf").asFloat().get(), 0.1);
    }

    /**
     * Test: 20170617092746028737
     *
     * <p>
     * Method: <code>asDouble()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617092746028737 ()
    {
        System.out.println("Test: 20170617092746028737");

        // Constant Numeric Values
        assertEquals(Double.MAX_VALUE, SAtom.fromString(Double.toString(Double.MAX_VALUE)).asDouble().get(), 0.1);
        assertEquals(Double.MIN_VALUE, SAtom.fromString(Double.toString(Double.MIN_VALUE)).asDouble().get(), 0.1);
        assertEquals(Double.NaN, SAtom.fromString(Double.toString(Double.NaN)).asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString(Double.toString(Double.POSITIVE_INFINITY)).asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, SAtom.fromString(Double.toString(Double.NEGATIVE_INFINITY)).asDouble().get(), 0.1);

        // Random Numeric Values
        final Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10000; i++)
        {
            final double expected = random.nextDouble();
            final double actual = SAtom.fromString(Double.toString(expected)).asDouble().get();
            assertEquals(expected, actual, 0.00001);
        }

        // Overflow
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString(Double.toString(Double.MAX_VALUE) + "000").asDouble().get(), 0.1);

        // Non Numeric
        assertFalse(SAtom.fromString("XYZ").asDouble().isPresent());

        // Case Insensitive Special Vaues
        assertEquals(Double.NaN, SAtom.fromString("NAN").asDouble().get(), 0.1);
        assertEquals(Double.NaN, SAtom.fromString("-NAN").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString("INFINITY").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, SAtom.fromString("-INFINITY").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString("INF").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, SAtom.fromString("-INF").asDouble().get(), 0.1);

        assertEquals(Double.NaN, SAtom.fromString("nan").asDouble().get(), 0.1);
        assertEquals(Double.NaN, SAtom.fromString("-nan").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString("infinity").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, SAtom.fromString("-infinity").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, SAtom.fromString("inf").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, SAtom.fromString("-inf").asDouble().get(), 0.1);
    }

    /**
     * Test: 20170624053921963551
     *
     * <p>
     * Method: <code>asByteArray()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624053921963551 ()
    {
        SAtom.fromString("0a").asByteArray().get();

        System.out.println("Test: 20170624053921963551");

        /**
         * Case: Valid Values
         */
        for (int high = 0; high < 16; high++)
        {
            for (int low = 0; low < 16; low++)
            {
                final String hex = Integer.toHexString(high) + Integer.toHexString(low);
                final SAtom atom = SAtom.fromString(hex);
                final byte[] bytes = atom.asByteArray().get();
                assertEquals(1, bytes.length);
                assertEquals(Byte.toUnsignedInt((byte) (high * 16 + low)), Byte.toUnsignedInt(bytes[0]));
            }
        }

        /**
         * Case: Invalid Values
         */
        assertFalse(SAtom.fromString("000").asByteArray().isPresent()); // odd number of chars
        assertFalse(SAtom.fromString("0G").asByteArray().isPresent()); // non hex chars
        assertFalse(SAtom.fromString("").asByteArray().isPresent()); // empty
    }

    /**
     * Test: 20170617092746028751
     *
     * <p>
     * Method: <code>toString()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617092746028751 ()
    {
        System.out.println("Test: 20170617092746028751");

        // No Special Characters => No Quotes
        assertEquals("vulcan", SAtom.fromString("vulcan").toString());

        // Whitespace => Quote It!
        assertEquals("'europa ganymede'", SAtom.fromString("europa ganymede").toString());

        // Whitespace => Quote It!
        assertEquals("'alien@example.com'", SAtom.fromString("alien@example.com").toString());

        // Escape Sequence Characters => Quote It!
        assertEquals("'\\b\\t\\n\\f\\r\\\'\\\"\\\\'", SAtom.fromString("\b\t\n\f\r\'\"\\").toString());
    }

    /**
     * Test: 20170617092746028764
     *
     * <p>
     * Case: convenience constructors
     * </p>
     *
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void test20170617092746028764 ()
            throws UnsupportedEncodingException
    {
        System.out.println("Test: 20170617092746028764");

        SAtom atom;

        // boolean
        atom = SAtom.fromBoolean(true);
        assertEquals("true", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asBoolean().isPresent());
        assertTrue(atom.asBoolean().get());
        assertTrue(atom.asBoolean() == atom.asBoolean()); // identity

        // char
        atom = SAtom.fromChar('M');
        assertEquals("77", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asChar().isPresent());
        assertEquals((char) 77, (char) atom.asChar().get());
        assertTrue(atom.asChar() == atom.asChar()); // identity

        // byte
        atom = SAtom.fromByte((byte) 13);
        assertEquals("13", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asByte().isPresent());
        assertEquals((byte) 13, (byte) atom.asByte().get());
        assertTrue(atom.asByte() == atom.asByte()); // identity

        // short
        atom = SAtom.fromShort((short) 17);
        assertEquals("17", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asShort().isPresent());
        assertEquals((short) 17, (short) atom.asShort().get());
        assertTrue(atom.asShort() == atom.asShort()); // identity

        // int
        atom = SAtom.fromInt((int) 19);
        assertEquals("19", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asInt().isPresent());
        assertEquals((int) 19, (int) atom.asInt().get());
        assertTrue(atom.asInt() == atom.asInt()); // identity

        // long
        atom = SAtom.fromLong((long) 23);
        assertEquals("23", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asLong().isPresent());
        assertEquals((long) 23, (long) atom.asLong().get());
        assertTrue(atom.asLong() == atom.asLong()); // identity

        // float
        atom = SAtom.fromFloat((float) 27);
        assertEquals("27.0", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asFloat().isPresent());
        assertEquals((float) 27, (float) atom.asFloat().get(), 0.1);
        assertTrue(atom.asFloat() == atom.asFloat()); // identity

        // double
        atom = SAtom.fromDouble((double) 29);
        assertEquals("29.0", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asDouble().isPresent());
        assertEquals((double) 29.0, (double) atom.asDouble().get(), 0.1);
        assertTrue(atom.asDouble() == atom.asDouble()); // identity

        // String
        atom = SAtom.fromString("Vulcan");
        assertEquals("Vulcan", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue((Object) atom.content() == (Object) atom.content()); // identity

        // Class
        atom = SAtom.fromClass(String.class);
        assertEquals("java.lang.String", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asClass().isPresent());
        assertEquals(String.class, atom.asClass().get());
        assertTrue(atom.asClass() == atom.asClass()); // identity

        // byte[]
        atom = SAtom.fromByteArray("Earth".getBytes("UTF-8"));
        assertEquals("4561727468", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asByteArray().isPresent());
        assertArrayEquals("Earth".getBytes("UTF-8"), atom.asByteArray().get());
        assertTrue(atom.asByteArray() != atom.asByteArray()); // identity inequality

        final SourceLocation moon = new SourceLocation("Europa", 1, 2);

        // boolean
        atom = SAtom.fromBoolean(moon, true);
        assertEquals("true", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asBoolean().isPresent());
        assertTrue(atom.asBoolean().get());
        assertTrue(atom.asBoolean() == atom.asBoolean()); // identity

        // char
        atom = SAtom.fromChar(moon, 'M');
        assertEquals("77", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asChar().isPresent());
        assertEquals((char) 77, (char) atom.asChar().get());
        assertTrue(atom.asChar() == atom.asChar()); // identity

        // byte
        atom = SAtom.fromByte(moon, (byte) 13);
        assertEquals("13", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asByte().isPresent());
        assertEquals((byte) 13, (byte) atom.asByte().get());
        assertTrue(atom.asByte() == atom.asByte()); // identity

        // short
        atom = SAtom.fromShort(moon, (short) 17);
        assertEquals("17", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asShort().isPresent());
        assertEquals((short) 17, (short) atom.asShort().get());
        assertTrue(atom.asShort() == atom.asShort()); // identity

        // int
        atom = SAtom.fromInt(moon, (int) 19);
        assertEquals("19", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asInt().isPresent());
        assertEquals((int) 19, (int) atom.asInt().get());
        assertTrue(atom.asInt() == atom.asInt()); // identity

        // long
        atom = SAtom.fromLong(moon, (long) 23);
        assertEquals("23", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asLong().isPresent());
        assertEquals((long) 23, (long) atom.asLong().get());
        assertTrue(atom.asLong() == atom.asLong()); // identity

        // float
        atom = SAtom.fromFloat(moon, (float) 27);
        assertEquals("27.0", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asFloat().isPresent());
        assertEquals((float) 27, (float) atom.asFloat().get(), 0.1);
        assertTrue(atom.asFloat() == atom.asFloat()); // identity

        // double
        atom = SAtom.fromDouble(moon, (double) 29);
        assertEquals("29.0", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asDouble().isPresent());
        assertEquals((double) 29.0, (double) atom.asDouble().get(), 0.1);
        assertTrue(atom.asDouble() == atom.asDouble()); // identity

        // String
        atom = SAtom.fromString(moon, "Vulcan");
        assertEquals("Vulcan", atom.content());
        assertEquals(moon, atom.location());
        assertTrue((Object) atom.content() == (Object) atom.content()); // identity

        // Class
        atom = SAtom.fromClass(moon, String.class);
        assertEquals("java.lang.String", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asClass().isPresent());
        assertEquals(String.class, atom.asClass().get());
        assertTrue(atom.asClass() == atom.asClass()); // identity

        // byte[]
        atom = SAtom.fromByteArray(moon, "Earth".getBytes("UTF-8"));
        assertEquals("4561727468", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asByteArray().isPresent());
        assertArrayEquals("Earth".getBytes("UTF-8"), atom.asByteArray().get());
        assertTrue(atom.asByteArray() != atom.asByteArray()); // identity inequality
    }

    /**
     * Test: 20170624061414160191
     *
     * <p>
     * Method: <code>compareTo</code>
     * </p>
     *
     * <p>
     * Case: normal cases
     * </p>
     */
    @Test
    public void test20170624061414160191 ()
    {
        System.out.println("Test: 20170624061414160191");

        SAtom atom1;
        SAtom atom2;

        // Case: less
        atom1 = SAtom.fromString("A");
        atom2 = SAtom.fromString("B");
        assertTrue(atom1.compareTo(atom2) < 0);

        // Case: greater
        atom1 = SAtom.fromString("B");
        atom2 = SAtom.fromString("A");
        assertTrue(atom1.compareTo(atom2) > 0);

        // Case: equal
        atom1 = SAtom.fromString("X");
        atom2 = SAtom.fromString("X");
        assertTrue(atom1.compareTo(atom2) == 0);

        // Case: different case
        atom1 = SAtom.fromString("A");
        atom2 = SAtom.fromString("a");
        assertTrue(atom1.compareTo(atom2) < 0);

        // Case: different case
        atom1 = SAtom.fromString("a");
        atom2 = SAtom.fromString("A");
        assertTrue(atom1.compareTo(atom2) > 0);

        // Case: null
        atom1 = SAtom.fromString("A");
        atom2 = null;
        assertTrue(atom1.compareTo(atom2) > 0);
    }

    /**
     * Test: 20170624061414160276
     *
     * <p>
     * Method: <code>equals</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624061414160276 ()
    {
        System.out.println("Test: 20170624061414160276");

        SAtom atom1;
        SAtom atom2;

        // Case: null
        atom1 = SAtom.fromString("X");
        atom2 = null;
        assertFalse(atom1.equals(atom2));

        // Case: same object
        atom1 = SAtom.fromString("X");
        atom2 = atom1;
        assertTrue(atom1.equals(atom2));

        // Case: different type
        atom1 = SAtom.fromString("X");
        assertFalse(atom1.equals("X"));

        // Case: different case
        // Case: null
        atom1 = SAtom.fromString("X");
        atom2 = SAtom.fromString("x");
        assertFalse(atom1.equals(atom2));

        // Case: different object, returns false
        atom1 = SAtom.fromString("X");
        atom2 = SAtom.fromString("Y");
        assertFalse(atom1.equals(atom2));

        // Case: different object, returns true
        atom1 = SAtom.fromString("X");
        atom2 = SAtom.fromString("X");
        assertTrue(atom1.equals(atom2));
    }

    /**
     * Test: 20170624061414160303
     *
     * <p>
     * Method: <code>hashCode</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170624061414160303 ()
    {
        System.out.println("Test: 20170624061414160303");

        final SAtom atom1 = SAtom.fromString("X");
        final SAtom atom2 = SAtom.fromString("X");
        assertTrue(atom1.hashCode() == atom2.hashCode());
    }

    /**
     * Test: 20170624063213060668
     *
     * <p>
     * Method: <code>treeSize</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060668 ()
    {
        System.out.println("Test: 20170624063213060668");

        assertEquals(1, SAtom.fromString("XYZ").treeSize());
    }

    /**
     * Test: 20170624063213060752
     *
     * <p>
     * Method: <code>treeHeight</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060752 ()
    {
        System.out.println("Test: 20170624063213060752");

        assertEquals(1, SAtom.fromString("XYZ").treeHeight());
    }

    /**
     * Test: 20170624063213060779
     *
     * <p>
     * Method: <code>treeLeafCount</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060779 ()
    {
        System.out.println("Test: 20170624063213060779");

        assertEquals(1, SAtom.fromString("XYZ").treeLeafCount());
    }

    /**
     * Test: 20170624063213060801
     *
     * <p>
     * Method: <code>preorder</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060801 ()
    {
        System.out.println("Test: 20170624063213060801");

        assertTrue(SAtom.fromString("XYZ").preorder(x -> x.toString().equals("XYZ")));
        assertFalse(SAtom.fromString("XYZ").preorder(x -> x.toString().equals("ABC")));
    }

    /**
     * Test: 20170624063213060821
     *
     * <p>
     * Method: <code>postorder</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060821 ()
    {
        System.out.println("Test: 20170624063213060821");

        assertTrue(SAtom.fromString("XYZ").postorder(x -> x.toString().equals("XYZ")));
        assertFalse(SAtom.fromString("XYZ").postorder(x -> x.toString().equals("ABC")));
    }

    /**
     * Test: 20170624063213060840
     *
     * <p>
     * Method: <code>dfs</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060840 ()
    {
        System.out.println("Test: 20170624063213060840");

        assertTrue(SAtom.fromString("XYZ").dfs(x -> x.toString().equals("XYZ")));
        assertFalse(SAtom.fromString("XYZ").dfs(x -> x.toString().equals("ABC")));
    }

    /**
     * Test: 20170624063213060863
     *
     * <p>
     * Method: <code>bfs</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624063213060863 ()
    {
        System.out.println("Test: 20170624063213060863");

        assertTrue(SAtom.fromString("XYZ").bfs(x -> x.toString().equals("XYZ")));
        assertFalse(SAtom.fromString("XYZ").bfs(x -> x.toString().equals("ABC")));
    }

    /**
     * Test: 20170625213924913141
     *
     * <p>
     * Method: <code>transverse</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625213924913141 ()
    {
        System.out.println("Test: 20170625213924913141");

        final Object BEFORE = new Object();
        final Object AFTER = new Object();
        final List<Object> record = new LinkedList<>();

        final Consumer<Sexpr> before = x ->
        {
            record.add(BEFORE);
            record.add(x);
        };

        final Consumer<Sexpr> after = x ->
        {
            record.add(AFTER);
            record.add(x);
        };

        final SAtom atom = SAtom.fromInt(17);

        // Method Under Test
        atom.traverse(before, after);

        assertEquals(Arrays.asList(BEFORE, atom, AFTER, atom), record);
    }

    /**
     * Test: 20170625013811754162
     *
     * <p>
     * Method: <code>toList</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test (expected = ClassCastException.class)
    public void test20170625013811754162 ()
    {
        System.out.println("Test: 20170625013811754162");

        SAtom.fromInt(1).asList();
    }

    /**
     * Test: 20170625013811754272
     *
     * <p>
     * Method: <code>toAtom</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625013811754272 ()
    {
        System.out.println("Test: 20170625013811754272");

        final SAtom original = SAtom.fromInt(1);
        final SAtom result = original.asAtom();
        assertTrue(original == result); // identity
    }
}
