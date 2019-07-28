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
package com.mackenziehigh.sexpr.internal;

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
