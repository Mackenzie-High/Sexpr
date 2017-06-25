package com.mackenziehigh.sexpr;

import static org.junit.Assert.*;
import org.junit.Test;

public class ParserTest
{
    /**
     * Test: 20170617124858539699
     *
     * <p>
     * Method: <code>parse</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617124858539699 ()
    {
        System.out.println("Test: 20170617124858539699");

        final Parser p = new Parser("test20170617124858539699");

        /**
         * Empty input creates a single enclosing SList.
         */
        final SList list1 = p.parse("");
        assertTrue(list1.isEmpty());

        /**
         * Make sure that comments are ignored.
         */
        final SList list2 = p.parse("# Hello World!");
        assertTrue(list2.isEmpty());

        /**
         * Empty String Literals.
         */
        assertEquals("", ((SAtom) p.parse("@''''''").get(0)).content());
        assertEquals("", ((SAtom) p.parse("@\"\"").get(0)).content());
        assertEquals("", ((SAtom) p.parse("''''''").get(0)).content());
        assertEquals("", ((SAtom) p.parse("\"\"").get(0)).content());

        /**
         * Do escape sequences cause problems in string literals?
         */
        assertEquals("\'", ((SAtom) p.parse("'''\\\''''").get(0)).content());
        assertEquals("\"", ((SAtom) p.parse("\"\\\"\"").get(0)).content());

        /**
         * Escape Sequences.
         */
        assertEquals("\u1234\b\t\n\f\r\\\'\"", ((SAtom) p.parse("'''\\u1234\\b\\t\\n\\f\\r\\\\\\'\\\"'''").get(0)).content());
        assertEquals("\u1234\b\t\n\f\r\\\'\"", ((SAtom) p.parse("\"\\u1234\\b\\t\\n\\f\\r\\\\\\'\\\"\"").get(0)).content());

        /**
         * Symbolic Lists
         */
        assertEquals("((100 200) (300))", p.parse("(100 200) (300)").toString());
    }

    /**
     * Test: 20170617124858539776
     *
     * <p>
     * Method: <code>parse</code>
     * </p>
     *
     * <p>
     * Case: syntax error
     * </p>
     */
    @Test
    public void test20170617124858539776 ()
    {
        System.out.println("Test: 20170617124858539776");

        final Parser p = new Parser("test20170617124858539776");

        try
        {
            p.parse("'''");
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            final String message = ex.getMessage();
            assertTrue(message.matches("Parsing Failed At Line: [0-9]+, Column: [0-9]+, Source: test20170617124858539776"));
        }
    }
}
