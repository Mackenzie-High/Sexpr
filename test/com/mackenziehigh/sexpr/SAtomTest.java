package com.mackenziehigh.sexpr;

import java.util.Random;
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
        final SAtom atom = new SAtom(expected);
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
        final String actual = new SAtom("a\tb").escaped();
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

        assertTrue(new SAtom("(x)").isAtom());
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

        assertFalse(new SAtom("(x)").isList());
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

        assertEquals(String.class, new SAtom("java.lang.String").asClass().get());
        assertFalse(new SAtom("MyString").asClass().isPresent());
        assertFalse(new SAtom("123").asClass().isPresent());
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
        assertTrue(new SAtom("true").asBoolean().get());
        assertTrue(new SAtom("yes").asBoolean().get());
        assertTrue(new SAtom("on").asBoolean().get());
        assertTrue(new SAtom("t").asBoolean().get());
        assertTrue(new SAtom("1").asBoolean().get());

        // True, uppercase.
        assertTrue(new SAtom("TRUE").asBoolean().get());
        assertTrue(new SAtom("YES").asBoolean().get());
        assertTrue(new SAtom("ON").asBoolean().get());
        assertTrue(new SAtom("T").asBoolean().get());

        // False, lowercase.
        assertFalse(new SAtom("false").asBoolean().get());
        assertFalse(new SAtom("no").asBoolean().get());
        assertFalse(new SAtom("off").asBoolean().get());
        assertFalse(new SAtom("f").asBoolean().get());
        assertFalse(new SAtom("0").asBoolean().get());

        // False, uppercase.
        assertFalse(new SAtom("FALSE").asBoolean().get());
        assertFalse(new SAtom("NO").asBoolean().get());
        assertFalse(new SAtom("OFF").asBoolean().get());
        assertFalse(new SAtom("F").asBoolean().get());

        // Neither True not False.
        assertFalse(new SAtom("X").asBoolean().isPresent());
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
            assertEquals((char) i, (char) new SAtom(Integer.toString((int) i)).asChar().get());
        }

        // Named Values
        assertEquals(Character.MAX_VALUE, (char) new SAtom("MAXIMUM").asChar().get());
        assertEquals(Character.MIN_VALUE, (char) new SAtom("MINIMUM").asChar().get());
        assertEquals(Character.MAX_VALUE, (char) new SAtom("MAX").asChar().get());
        assertEquals(Character.MIN_VALUE, (char) new SAtom("MIN").asChar().get());
        assertEquals(Character.MAX_VALUE, (char) new SAtom("max").asChar().get());
        assertEquals(Character.MIN_VALUE, (char) new SAtom("min").asChar().get());

        // Overflow
        // Use Integer MAX_VALUE and MIN_VALUE here, instead of Character MAX_VALUE and MIN_VALUE.
        assertFalse(new SAtom(Integer.toString(Integer.MAX_VALUE) + "000").asChar().isPresent());
        assertFalse(new SAtom(Integer.toString(Integer.MIN_VALUE) + "000").asChar().isPresent());

        // Non Numeric
        assertFalse(new SAtom("NaN").asChar().isPresent());
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
            assertEquals((byte) i, (byte) new SAtom(Byte.toString((byte) i)).asByte().get());
        }

        // Named Values
        assertEquals(Byte.MAX_VALUE, (byte) new SAtom("MAXIMUM").asByte().get());
        assertEquals(Byte.MIN_VALUE, (byte) new SAtom("MINIMUM").asByte().get());
        assertEquals(Byte.MAX_VALUE, (byte) new SAtom("MAX").asByte().get());
        assertEquals(Byte.MIN_VALUE, (byte) new SAtom("MIN").asByte().get());
        assertEquals(Byte.MAX_VALUE, (byte) new SAtom("max").asByte().get());
        assertEquals(Byte.MIN_VALUE, (byte) new SAtom("min").asByte().get());

        // Overflow
        assertFalse(new SAtom(Byte.toString(Byte.MAX_VALUE) + "000").asByte().isPresent());
        assertFalse(new SAtom(Byte.toString(Byte.MIN_VALUE) + "000").asByte().isPresent());

        // Non Numeric
        assertFalse(new SAtom("NaN").asByte().isPresent());
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
            assertEquals((short) i, (short) new SAtom(Short.toString((short) i)).asShort().get());
        }

        // Named Values
        assertEquals(Short.MAX_VALUE, (short) new SAtom("MAXIMUM").asShort().get());
        assertEquals(Short.MIN_VALUE, (short) new SAtom("MINIMUM").asShort().get());
        assertEquals(Short.MAX_VALUE, (short) new SAtom("MAX").asShort().get());
        assertEquals(Short.MIN_VALUE, (short) new SAtom("MIN").asShort().get());
        assertEquals(Short.MAX_VALUE, (short) new SAtom("max").asShort().get());
        assertEquals(Short.MIN_VALUE, (short) new SAtom("min").asShort().get());

        // Overflow
        assertFalse(new SAtom(Short.toString(Short.MAX_VALUE) + "000").asShort().isPresent());
        assertFalse(new SAtom(Short.toString(Short.MIN_VALUE) + "000").asShort().isPresent());

        // Non Numeric
        assertFalse(new SAtom("NaN").asShort().isPresent());
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
        assertEquals(Integer.MAX_VALUE, (int) new SAtom(Integer.toString(Integer.MAX_VALUE)).asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) new SAtom(Integer.toString(Integer.MIN_VALUE)).asInt().get());

        // Named Values
        assertEquals(Integer.MAX_VALUE, (int) new SAtom("MAXIMUM").asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) new SAtom("MINIMUM").asInt().get());
        assertEquals(Integer.MAX_VALUE, (int) new SAtom("MAX").asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) new SAtom("MIN").asInt().get());
        assertEquals(Integer.MAX_VALUE, (int) new SAtom("max").asInt().get());
        assertEquals(Integer.MIN_VALUE, (int) new SAtom("min").asInt().get());

        // Overflow
        assertFalse(new SAtom(Integer.toString(Integer.MAX_VALUE) + "000").asInt().isPresent());
        assertFalse(new SAtom(Integer.toString(Integer.MIN_VALUE) + "000").asInt().isPresent());

        // Non Numeric
        assertFalse(new SAtom("NaN").asInt().isPresent());
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
        assertEquals(Long.MAX_VALUE, (long) new SAtom(Long.toString(Long.MAX_VALUE)).asLong().get());
        assertEquals(Long.MIN_VALUE, (long) new SAtom(Long.toString(Long.MIN_VALUE)).asLong().get());

        // Named Values
        assertEquals(Long.MAX_VALUE, (long) new SAtom("MAXIMUM").asLong().get());
        assertEquals(Long.MIN_VALUE, (long) new SAtom("MINIMUM").asLong().get());
        assertEquals(Long.MAX_VALUE, (long) new SAtom("MAX").asLong().get());
        assertEquals(Long.MIN_VALUE, (long) new SAtom("MIN").asLong().get());
        assertEquals(Long.MAX_VALUE, (long) new SAtom("max").asLong().get());
        assertEquals(Long.MIN_VALUE, (long) new SAtom("min").asLong().get());

        // Overflow
        assertFalse(new SAtom(Long.toString(Long.MAX_VALUE) + "000").asLong().isPresent());
        assertFalse(new SAtom(Long.toString(Long.MIN_VALUE) + "000").asLong().isPresent());

        // Non Numeric
        assertFalse(new SAtom("NaN").asLong().isPresent());
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
        assertEquals(Float.MAX_VALUE, new SAtom(Float.toString(Float.MAX_VALUE)).asFloat().get(), 0.1);
        assertEquals(Float.MIN_VALUE, new SAtom(Float.toString(Float.MIN_VALUE)).asFloat().get(), 0.1);
        assertEquals(Float.NaN, new SAtom(Float.toString(Float.NaN)).asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, new SAtom(Float.toString(Float.POSITIVE_INFINITY)).asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, new SAtom(Float.toString(Float.NEGATIVE_INFINITY)).asFloat().get(), 0.1);

        // Random Numeric Values
        final Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10000; i++)
        {
            final float expected = random.nextFloat();
            final float actual = new SAtom(Float.toString(expected)).asFloat().get();
            assertEquals(expected, actual, 0.001);
        }

        // Overflow
        assertEquals(Double.POSITIVE_INFINITY, new SAtom(Float.toString(Float.MAX_VALUE) + "000").asFloat().get(), 0.1);

        // Non Numeric
        assertFalse(new SAtom("XYZ").asFloat().isPresent());

        // Case Insensitive Special Vaues
        assertEquals(Float.NaN, new SAtom("NAN").asFloat().get(), 0.1);
        assertEquals(Float.NaN, new SAtom("-NAN").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, new SAtom("INFINITY").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, new SAtom("-INFINITY").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, new SAtom("INF").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, new SAtom("-INF").asFloat().get(), 0.1);

        assertEquals(Float.NaN, new SAtom("nan").asFloat().get(), 0.1);
        assertEquals(Float.NaN, new SAtom("-nan").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, new SAtom("infinity").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, new SAtom("-infinity").asFloat().get(), 0.1);
        assertEquals(Float.POSITIVE_INFINITY, new SAtom("inf").asFloat().get(), 0.1);
        assertEquals(Float.NEGATIVE_INFINITY, new SAtom("-inf").asFloat().get(), 0.1);
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
        assertEquals(Double.MAX_VALUE, new SAtom(Double.toString(Double.MAX_VALUE)).asDouble().get(), 0.1);
        assertEquals(Double.MIN_VALUE, new SAtom(Double.toString(Double.MIN_VALUE)).asDouble().get(), 0.1);
        assertEquals(Double.NaN, new SAtom(Double.toString(Double.NaN)).asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new SAtom(Double.toString(Double.POSITIVE_INFINITY)).asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new SAtom(Double.toString(Double.NEGATIVE_INFINITY)).asDouble().get(), 0.1);

        // Random Numeric Values
        final Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 10000; i++)
        {
            final double expected = random.nextDouble();
            final double actual = new SAtom(Double.toString(expected)).asDouble().get();
            assertEquals(expected, actual, 0.00001);
        }

        // Overflow
        assertEquals(Double.POSITIVE_INFINITY, new SAtom(Double.toString(Double.MAX_VALUE) + "000").asDouble().get(), 0.1);

        // Non Numeric
        assertFalse(new SAtom("XYZ").asDouble().isPresent());

        // Case Insensitive Special Vaues
        assertEquals(Double.NaN, new SAtom("NAN").asDouble().get(), 0.1);
        assertEquals(Double.NaN, new SAtom("-NAN").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new SAtom("INFINITY").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new SAtom("-INFINITY").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new SAtom("INF").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new SAtom("-INF").asDouble().get(), 0.1);

        assertEquals(Double.NaN, new SAtom("nan").asDouble().get(), 0.1);
        assertEquals(Double.NaN, new SAtom("-nan").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new SAtom("infinity").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new SAtom("-infinity").asDouble().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new SAtom("inf").asDouble().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new SAtom("-inf").asDouble().get(), 0.1);
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
        new SAtom("0a").asByteArray().get();

        System.out.println("Test: 20170624053921963551");

        /**
         * Case: Valid Values
         */
        for (int high = 0; high < 16; high++)
        {
            for (int low = 0; low < 16; low++)
            {
                final String hex = Integer.toHexString(high) + Integer.toHexString(low);
                final SAtom atom = new SAtom(hex);
                final byte[] bytes = atom.asByteArray().get();
                assertEquals(1, bytes.length);
                assertEquals(Byte.toUnsignedInt((byte) (high * 16 + low)), Byte.toUnsignedInt(bytes[0]));
            }
        }

        /**
         * Case: Invalid Values
         */
        assertFalse(new SAtom("000").asByteArray().isPresent()); // odd number of chars
        assertFalse(new SAtom("0G").asByteArray().isPresent()); // non hex chars
        assertFalse(new SAtom("").asByteArray().isPresent()); // empty
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

        assertEquals("vulcan", new SAtom("vulcan").toString());
        assertEquals("europa ganymede", new SAtom("'europa ganymede'").toString());
        fail();
    }

    /**
     * Test: 20170617092746028764
     *
     * <p>
     * Case: convenience constructors
     * </p>
     */
    @Test
    public void test20170617092746028764 ()
    {
        System.out.println("Test: 20170617092746028764");

        SAtom atom;

        // boolean
        atom = new SAtom(true);
        assertEquals("true", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asBoolean().isPresent());
        assertTrue(atom.asBoolean().get());
        assertTrue(atom.asBoolean() == atom.asBoolean()); // identity

        // char
        atom = new SAtom('M');
        assertEquals("77", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asChar().isPresent());
        assertEquals((char) 77, (char) atom.asChar().get());
        assertTrue(atom.asChar() == atom.asChar()); // identity

        // byte
        atom = new SAtom((byte) 13);
        assertEquals("13", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asByte().isPresent());
        assertEquals((byte) 13, (byte) atom.asByte().get());
        assertTrue(atom.asByte() == atom.asByte()); // identity

        // short
        atom = new SAtom((short) 17);
        assertEquals("17", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asShort().isPresent());
        assertEquals((short) 17, (short) atom.asShort().get());
        assertTrue(atom.asShort() == atom.asShort()); // identity

        // int
        atom = new SAtom((int) 19);
        assertEquals("19", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asInt().isPresent());
        assertEquals((int) 19, (int) atom.asInt().get());
        assertTrue(atom.asInt() == atom.asInt()); // identity

        // long
        atom = new SAtom((long) 23);
        assertEquals("23", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asLong().isPresent());
        assertEquals((long) 23, (long) atom.asLong().get());
        assertTrue(atom.asLong() == atom.asLong()); // identity

        // float
        atom = new SAtom((float) 27);
        assertEquals("27.0", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asFloat().isPresent());
        assertEquals((float) 27, (float) atom.asFloat().get(), 0.1);
        assertTrue(atom.asFloat() == atom.asFloat()); // identity

        // double
        atom = new SAtom((double) 29);
        assertEquals("29.0", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asDouble().isPresent());
        assertEquals((double) 29.0, (double) atom.asDouble().get(), 0.1);
        assertTrue(atom.asDouble() == atom.asDouble()); // identity

        // String
        atom = new SAtom("Vulcan");
        assertEquals("Vulcan", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue((Object) atom.content() == (Object) atom.content()); // identity

        // Class
        atom = new SAtom(String.class);
        assertEquals("java.lang.String", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asClass().isPresent());
        assertEquals(String.class, atom.asClass().get());
        assertTrue(atom.asClass() == atom.asClass()); // identity

        // byte[]
        atom = new SAtom("Earth".getBytes());
        assertEquals("4561727468", atom.content());
        assertEquals(SourceLocation.DEFAULT, atom.location());
        assertTrue(atom.asByteArray().isPresent());
        assertArrayEquals("Earth".getBytes(), atom.asByteArray().get());
        assertTrue(atom.asByteArray() != atom.asByteArray()); // identity inequality

        final SourceLocation moon = new SourceLocation("Europa", 1, 2);

        // boolean
        atom = new SAtom(moon, true);
        assertEquals("true", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asBoolean().isPresent());
        assertTrue(atom.asBoolean().get());
        assertTrue(atom.asBoolean() == atom.asBoolean()); // identity

        // char
        atom = new SAtom(moon, 'M');
        assertEquals("77", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asChar().isPresent());
        assertEquals((char) 77, (char) atom.asChar().get());
        assertTrue(atom.asChar() == atom.asChar()); // identity

        // byte
        atom = new SAtom(moon, (byte) 13);
        assertEquals("13", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asByte().isPresent());
        assertEquals((byte) 13, (byte) atom.asByte().get());
        assertTrue(atom.asByte() == atom.asByte()); // identity

        // short
        atom = new SAtom(moon, (short) 17);
        assertEquals("17", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asShort().isPresent());
        assertEquals((short) 17, (short) atom.asShort().get());
        assertTrue(atom.asShort() == atom.asShort()); // identity

        // int
        atom = new SAtom(moon, (int) 19);
        assertEquals("19", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asInt().isPresent());
        assertEquals((int) 19, (int) atom.asInt().get());
        assertTrue(atom.asInt() == atom.asInt()); // identity

        // long
        atom = new SAtom(moon, (long) 23);
        assertEquals("23", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asLong().isPresent());
        assertEquals((long) 23, (long) atom.asLong().get());
        assertTrue(atom.asLong() == atom.asLong()); // identity

        // float
        atom = new SAtom(moon, (float) 27);
        assertEquals("27.0", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asFloat().isPresent());
        assertEquals((float) 27, (float) atom.asFloat().get(), 0.1);
        assertTrue(atom.asFloat() == atom.asFloat()); // identity

        // double
        atom = new SAtom(moon, (double) 29);
        assertEquals("29.0", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asDouble().isPresent());
        assertEquals((double) 29.0, (double) atom.asDouble().get(), 0.1);
        assertTrue(atom.asDouble() == atom.asDouble()); // identity

        // String
        atom = new SAtom(moon, "Vulcan");
        assertEquals("Vulcan", atom.content());
        assertEquals(moon, atom.location());
        assertTrue((Object) atom.content() == (Object) atom.content()); // identity

        // Class
        atom = new SAtom(moon, String.class);
        assertEquals("java.lang.String", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asClass().isPresent());
        assertEquals(String.class, atom.asClass().get());
        assertTrue(atom.asClass() == atom.asClass()); // identity

        // byte[]
        atom = new SAtom(moon, "Earth".getBytes());
        assertEquals("4561727468", atom.content());
        assertEquals(moon, atom.location());
        assertTrue(atom.asByteArray().isPresent());
        assertArrayEquals("Earth".getBytes(), atom.asByteArray().get());
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
        atom1 = new SAtom("A");
        atom2 = new SAtom("B");
        assertTrue(atom1.compareTo(atom2) < 0);

        // Case: greater
        atom1 = new SAtom("B");
        atom2 = new SAtom("A");
        assertTrue(atom1.compareTo(atom2) > 0);

        // Case: equal
        atom1 = new SAtom("X");
        atom2 = new SAtom("X");
        assertTrue(atom1.compareTo(atom2) == 0);

        // Case: different case
        atom1 = new SAtom("A");
        atom2 = new SAtom("a");
        assertTrue(atom1.compareTo(atom2) < 0);

        // Case: different case
        atom1 = new SAtom("a");
        atom2 = new SAtom("A");
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
        atom1 = new SAtom("X");
        atom2 = null;
        assertFalse(atom1.equals(atom2));

        // Case: same object
        atom1 = new SAtom("X");
        atom2 = atom1;
        assertTrue(atom1.equals(atom2));

        // Case: different type
        atom1 = new SAtom("X");
        assertFalse(atom1.equals("X"));

        // Case: different case
        // Case: null
        atom1 = new SAtom("X");
        atom2 = new SAtom("x");
        assertFalse(atom1.equals(atom2));

        // Case: different object, returns false
        atom1 = new SAtom("X");
        atom2 = new SAtom("Y");
        assertFalse(atom1.equals(atom2));

        // Case: different object, returns true
        atom1 = new SAtom("X");
        atom2 = new SAtom("X");
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

        final SAtom atom1 = new SAtom("X");
        final SAtom atom2 = new SAtom("X");
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

        assertEquals(1, new SAtom("XYZ").treeSize());
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

        assertEquals(1, new SAtom("XYZ").treeHeight());
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

        assertEquals(1, new SAtom("XYZ").treeLeafCount());
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

        assertTrue(new SAtom("XYZ").preorder(x -> x.toString().equals("XYZ")));
        assertFalse(new SAtom("XYZ").preorder(x -> x.toString().equals("ABC")));
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

        assertTrue(new SAtom("XYZ").postorder(x -> x.toString().equals("XYZ")));
        assertFalse(new SAtom("XYZ").postorder(x -> x.toString().equals("ABC")));
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

        assertTrue(new SAtom("XYZ").dfs(x -> x.toString().equals("XYZ")));
        assertFalse(new SAtom("XYZ").dfs(x -> x.toString().equals("ABC")));
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

        assertTrue(new SAtom("XYZ").bfs(x -> x.toString().equals("XYZ")));
        assertFalse(new SAtom("XYZ").bfs(x -> x.toString().equals("ABC")));
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

        new SAtom(1).toList();
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

        final SAtom original = new SAtom(1);
        final SAtom result = original.toAtom();
        assertTrue(original == result); // identity
    }
}
