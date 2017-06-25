package com.mackenziehigh.sexpr;

import com.mackenziehigh.sexpr.Escaper;
import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class EscaperTest
{
    /**
     * Test: 20170617083241112155
     *
     * <p>
     * Case: Singleton Exists
     * </p>
     */
    @Test
    public void test20170617083241112155 ()
    {
        System.out.println("Test: 20170617083241112155");

        assertNotNull(Escaper.instance);
        assertTrue(Escaper.instance instanceof Escaper);
    }

    /**
     * Test: 20170617083241112244
     *
     * <p>
     * Methods: <code>escape(*) and expand(*)</code>
     * </p>
     *
     * <p>
     * Case: reversibility
     * </p>
     */
    @Test
    public void test20170617083241112244 ()
    {
        System.out.println("Test: 20170617083241112244");

        for (int i = 1; i <= Character.MAX_VALUE; i++)
        {
            final char[] input = new char[1];
            input[0] = (char) i;
            final char[] output = Escaper.instance.expand(Escaper.instance.escape(input));
            assertEquals(input.length, output.length);
            assertEquals(input[0], output[0]);
        }
    }

    /**
     * Test: 20170617083241112272
     *
     * <p>
     * Method: <code>escape(*)</code>
     * </p>
     *
     * <p>
     * Case: Only certain ASCII characters remain unchanged.
     * </p>
     */
    @Test
    public void test20170617083241112272 ()
    {
        System.out.println("Test: 20170617083241112272");

        int total = 0;

        for (int i = 0; i <= Character.MAX_VALUE; i++)
        {
            final char[] input = new char[1];
            input[0] = (char) i;
            final int len = Escaper.instance.escape(input).length();
            total += len;

            if (i > 127)
            {
                assertTrue(len == 6);
            }
        }

        assertEquals((8 * 2) + (91 * 1) + ((65536 - 99) * 6), total);
    }

    /**
     * Test: 20170617083241112297
     *
     * <p>
     * Method: <code>escape(*)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617083241112297 ()
    {
        System.out.println("Test: 20170617083241112297");

        final String input = "\b\t\n\f\r\\\'\"\u9988abcz";
        final String output = "\\b\\t\\n\\f\\r\\\\\\'\\\"\\u9988abcz";

        assertEquals(output, Escaper.instance.escape(input.toCharArray()));
    }

    /**
     * Test: 20170617083241112319
     *
     * <p>
     * Method: <code>expand(*)</code>
     * </p>
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170617083241112319 ()
    {
        System.out.println("Test: 20170617083241112319");

        final String input = "\\b\\t\\n\\f\\r\\\\\\'\\\"\\u9988abcz";
        final String output = "\b\t\n\f\r\\\'\"\u9988abcz";

        assertTrue(Arrays.equals(output.toCharArray(), Escaper.instance.expand(input)));
    }

    /**
     * Test: 20170617091622955355
     *
     * <p>
     * Method: <code>expand(*)</code>
     * </p>
     *
     * <p>
     * Case: No Such Escape Sequence
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170617091622955355 ()
    {
        System.out.println("Test: 20170617091622955355");

        Escaper.instance.expand("\\m");
    }

    /**
     * Test: 20170617091622955446
     *
     * <p>
     * Method: <code>expand(*)</code>
     * </p>
     *
     * <p>
     * Case: Invalid Unicode Escape Sequence
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170617091622955446 ()
    {
        System.out.println("Test: 20170617091622955446");

        Escaper.instance.expand("\\u123G");
    }

    /**
     * Test: 20170617091622955501
     *
     * <p>
     * Method: <code>expand(*)</code>
     * </p>
     *
     * <p>
     * Case: Escape Slash at End of String
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170617091622955501 ()
    {
        System.out.println("Test: 20170617091622955501");

        Escaper.instance.expand("xyz\\");
    }
}
