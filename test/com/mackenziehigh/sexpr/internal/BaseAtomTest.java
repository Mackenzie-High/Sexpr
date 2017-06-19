package com.mackenziehigh.sexpr.internal;

import java.util.stream.IntStream;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class BaseAtomTest
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
        final BaseAtom atom = new BaseAtom(expected);
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
        final String actual = new BaseAtom("a\tb").escaped();
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

        assertTrue(new BaseAtom("(x)").isSAtom());
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

        assertFalse(new BaseAtom("(x)").isSList());
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

        assertEquals(String.class, new BaseAtom("java.lang.String").asClass().get());
        assertFalse(new BaseAtom("MyString").asClass().isPresent());
        assertFalse(new BaseAtom("123").asClass().isPresent());
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

        assertTrue(new BaseAtom("true").asBoolean().get());
        assertTrue(new BaseAtom("yes").asBoolean().get());
        assertTrue(new BaseAtom("on").asBoolean().get());
        assertTrue(new BaseAtom("t").asBoolean().get());
        assertTrue(new BaseAtom("1").asBoolean().get());

        assertTrue(new BaseAtom("TRUE").asBoolean().get());
        assertTrue(new BaseAtom("YES").asBoolean().get());
        assertTrue(new BaseAtom("ON").asBoolean().get());
        assertTrue(new BaseAtom("T").asBoolean().get());

        assertFalse(new BaseAtom("false").asBoolean().get());
        assertFalse(new BaseAtom("no").asBoolean().get());
        assertFalse(new BaseAtom("off").asBoolean().get());
        assertFalse(new BaseAtom("f").asBoolean().get());
        assertFalse(new BaseAtom("0").asBoolean().get());

        assertFalse(new BaseAtom("FALSE").asBoolean().get());
        assertFalse(new BaseAtom("NO").asBoolean().get());
        assertFalse(new BaseAtom("OFF").asBoolean().get());
        assertFalse(new BaseAtom("F").asBoolean().get());
    }

    /**
     * Test: 20170617092746028723
     *
     * <p>
     * Method: <code>asInteger()</code>
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

        assertEquals(Long.MAX_VALUE, (long) new BaseAtom(Long.toString(Long.MAX_VALUE)).asInteger().get());
        assertEquals(Long.MIN_VALUE, (long) new BaseAtom(Long.toString(Long.MIN_VALUE)).asInteger().get());

        // Overflow
        assertFalse(new BaseAtom(Long.toString(Long.MAX_VALUE) + "000").asInteger().isPresent());
        assertFalse(new BaseAtom(Long.toString(Long.MIN_VALUE) + "000").asInteger().isPresent());

        // Non Numeric
        assertFalse(new BaseAtom("NaN").asInteger().isPresent());
    }

    /**
     * Test: 20170617092746028737
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
    public void test20170617092746028737 ()
    {
        System.out.println("Test: 20170617092746028737");

        assertEquals(Double.MAX_VALUE, new BaseAtom(Double.toString(Double.MAX_VALUE)).asFloat().get(), 0.1);
        assertEquals(Double.MIN_VALUE, new BaseAtom(Double.toString(Double.MIN_VALUE)).asFloat().get(), 0.1);
        assertEquals(Double.NaN, new BaseAtom(Double.toString(Double.NaN)).asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new BaseAtom(Double.toString(Double.POSITIVE_INFINITY)).asFloat().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new BaseAtom(Double.toString(Double.NEGATIVE_INFINITY)).asFloat().get(), 0.1);

        // Overflow
        assertEquals(Double.POSITIVE_INFINITY, new BaseAtom(Double.toString(Double.MAX_VALUE) + "000").asFloat().get(), 0.1);

        // Non Numeric
        assertFalse(new BaseAtom("XYZ").asFloat().isPresent());

        // Case Insensitive Special Vaues
        assertEquals(Double.NaN, new BaseAtom("NAN").asFloat().get(), 0.1);
        assertEquals(Double.NaN, new BaseAtom("-NAN").asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new BaseAtom("INFINITY").asFloat().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new BaseAtom("-INFINITY").asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new BaseAtom("INF").asFloat().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new BaseAtom("-INF").asFloat().get(), 0.1);

        assertEquals(Double.NaN, new BaseAtom("nan").asFloat().get(), 0.1);
        assertEquals(Double.NaN, new BaseAtom("-nan").asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new BaseAtom("infinity").asFloat().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new BaseAtom("-infinity").asFloat().get(), 0.1);
        assertEquals(Double.POSITIVE_INFINITY, new BaseAtom("inf").asFloat().get(), 0.1);
        assertEquals(Double.NEGATIVE_INFINITY, new BaseAtom("-inf").asFloat().get(), 0.1);

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

        assertEquals("vulcan", new BaseAtom("vulcan").toString());
        assertEquals("europa ganymede", new BaseAtom("europa ganymede").toString());
        fail();
    }

    /**
     * Test: 20170617092746028764
     *
     * <p>
     * Method: <code></code>
     * </p>
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170617092746028764 ()
    {
        System.out.println("Test: 20170617092746028764");
        fail();
    }

    /**
     * Test: 20170617092746028776
     *
     * <p>
     * Method: <code></code>
     * </p>
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170617092746028776 ()
    {
        System.out.println("Test: 20170617092746028776");
        fail();
    }

    /**
     * Test: 20170617092746028789
     *
     * <p>
     * Method: <code></code>
     * </p>
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170617092746028789 ()
    {
        System.out.println("Test: 20170617092746028789");
        fail();
    }

    /**
     * Test: 20170617092746028801
     *
     * <p>
     * Method: <code></code>
     * </p>
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170617092746028801 ()
    {
        System.out.println("Test: 20170617092746028801");
        fail();
    }

    /**
     * Test: 20170617092746028814
     *
     * <p>
     * Method: <code></code>
     * </p>
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170617092746028814 ()
    {
        System.out.println("Test: 20170617092746028814");
        fail();
    }
}
