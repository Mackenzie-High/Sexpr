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

import com.mackenziehigh.sexpr.annotations.After;
import com.mackenziehigh.sexpr.annotations.Before;
import com.mackenziehigh.sexpr.annotations.Condition;
import com.mackenziehigh.sexpr.annotations.Pass;
import com.mackenziehigh.sexpr.internal.schema.Schema;
import com.mackenziehigh.sexpr.internal.schema.SchemaParser;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public final class SexprSchemaTest
{

    private boolean parse (final String schema,
                           final String expression)
    {
        /**
         * Remember, parse(*) adds an implicit pair of parentheses.
         * Remove the extra parentheses for the sake of simplicity.
         */
        final Sexpr tree = SList.parse(SexprSchemaTest.class.getName(), expression).asList().get(0);

        /**
         * Parse the schema.
         */
        final Schema grammar = SchemaParser.parse(SexprSchemaTest.class.getName(), schema);

        /**
         * Attempt to match the symbolic-expression using the schema.
         */
        final boolean result = grammar.match(tree);

        return result;
    }

    /**
     * Test: 20170703031359687941
     *
     * <p>
     * Case: Special Predefined Rules
     * </p>
     */
    @Test
    public void test20170703031359687941 ()
    {
        System.out.println("Test: 20170703031359687941");

        assertTrue(parse(" (root = (ref $BOOLEAN))", "true"));
        assertTrue(parse(" (root = (ref $BOOLEAN))", "false"));
        assertFalse(parse("(root = (ref $BOOLEAN))", "XYZ"));

        assertTrue(parse(" (root = (ref $BYTE))", "100"));
        assertTrue(parse(" (root = (ref $BYTE))", "-100"));
        assertFalse(parse("(root = (ref $BYTE))", "30.0"));
        assertFalse(parse("(root = (ref $BYTE))", "XYZ"));

        assertTrue(parse(" (root = (ref $SHORT))", "1000"));
        assertTrue(parse(" (root = (ref $SHORT))", "-200"));
        assertFalse(parse("(root = (ref $SHORT))", "30.0"));
        assertFalse(parse("(root = (ref $SHORT))", "XYZ"));

        assertTrue(parse(" (root = (ref $INT))", "1000"));
        assertTrue(parse(" (root = (ref $INT))", "-200"));
        assertFalse(parse("(root = (ref $INT))", "30.0"));
        assertFalse(parse("(root = (ref $INT))", "XYZ"));

        assertTrue(parse(" (root = (ref $LONG))", "1000"));
        assertTrue(parse(" (root = (ref $LONG))", "-200"));
        assertFalse(parse("(root = (ref $LONG))", "30.0"));
        assertFalse(parse("(root = (ref $LONG))", "XYZ"));
    }

    /**
     * Test: 20170703020824718850
     *
     * <p>
     * Case: Constant Rules
     * </p>
     */
    @Test
    public void test20170703020824718850 ()
    {
        System.out.println("Test: 20170703020824718850");

        assertTrue(parse("(root = (keyword '123'))", "123"));
        assertTrue(parse("(root = (keyword \"123\"))", "123"));
    }

    /**
     * Test: 20170703020824718946
     *
     * <p>
     * Case: Regular-Expression Rules
     * </p>
     */
    @Test
    public void test20170703020824718946 ()
    {
        System.out.println("Test: 20170703020824718946");

        assertTrue(parse("(root = (atom 'X[0-9]*Y'))", "XY"));
        assertTrue(parse("(root = (atom 'X[0-9]*Y'))", "X1Y"));
        assertTrue(parse("(root = (atom 'X[0-9]*Y'))", "X12Y"));
        assertTrue(parse("(root = (atom 'X[0-9]*Y'))", "X123Y"));

        assertFalse(parse("(root = (atom 'X[0-9]+Y'))", "XYZ"));
    }

    /**
     * Test: 20170703020824719014
     *
     * <p>
     * Case: And Rules
     * </p>
     */
    @Test
    public void test20170703020824719014 ()
    {
        System.out.println("Test: 20170703020824719014");

        assertTrue(parse("(root = (and (atom 'X[0-9]+.') (atom '.[0-9]+Y')))", "X123Y"));

        assertFalse(parse("(root = (and (atom 'X[0-9]+.') (atom '.[0-9]+Y')))", "X123Z"));
    }

    /**
     * Test: 20170703020824719042
     *
     * <p>
     * Case: Or Rules
     * </p>
     */
    @Test
    public void test20170703020824719042 ()
    {
        System.out.println("Test: 20170703020824719042");

        assertTrue(parse("(root = (either (atom '12') (atom '34') (atom '56')))", "12"));
        assertTrue(parse("(root = (either (atom '12') (atom '34') (atom '56')))", "34"));
        assertTrue(parse("(root = (either (atom '12') (atom '34') (atom '56')))", "56"));

        assertFalse(parse("(root = (either (atom '12') (atom '34') (atom '56')))", "78"));
    }

    /**
     * Test: 20170703023207618848
     *
     * <p>
     * Case: Not Rules
     * </p>
     */
    @Test
    public void test20170703023207618848 ()
    {
        System.out.println("Test: 20170703023207618848");

        final String schema = "(root = (and (atom '[0-9]') (not (atom '3')) (not (atom '5')) (not (atom '7'))))";

        assertTrue(parse(schema, "0"));
        assertTrue(parse(schema, "1"));
        assertTrue(parse(schema, "2"));
        assertFalse(parse(schema, "3"));
        assertTrue(parse(schema, "4"));
        assertFalse(parse(schema, "5"));
        assertTrue(parse(schema, "6"));
        assertFalse(parse(schema, "7"));
        assertTrue(parse(schema, "8"));
        assertTrue(parse(schema, "9"));
    }

    /**
     * Test: 20170703022350691405
     *
     * <p>
     * Case: Sequence Rules (Basic Functionality)
     * </p>
     */
    @Test
    public void test20170703022350691405 ()
    {
        System.out.println("Test: 20170703022350691405");

        assertTrue(parse("(root = (seq))", "()"));
        assertTrue(parse("(root = (seq (atom 12)))", "(12)"));
        assertTrue(parse("(root = (seq (atom 12) (atom 34)))", "(12 34)"));
        assertTrue(parse("(root = (seq (atom 12) (atom 34) (atom 56)))", "(12 34 56)"));

        assertFalse(parse("(root = (seq (atom 12) (atom 34)))", "(12 34 56)"));
    }

    /**
     * Test: 20170703022350691495
     *
     * <p>
     * Case: Sequence Rules (Zero-Or-One Modifiers)
     * </p>
     */
    @Test
    public void test20170703022350691495 ()
    {
        System.out.println("Test: 20170703022350691495");

        assertTrue(parse(" (root = (seq (atom X) (option (atom Y)) (atom Z)))", "(X Z)"));
        assertTrue(parse(" (root = (seq (atom X) (option (atom Y)) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (option (atom Y)) (atom Z)))", "(X Y Y Z)"));
    }

    /**
     * Test: 20170703022350691526
     *
     * <p>
     * Case: Sequence Rules (Zero-Or-More Modifiers)
     * </p>
     */
    @Test
    public void test20170703022350691526 ()
    {
        System.out.println("Test: 20170703022350691526");

        assertTrue(parse(" (root = (seq (atom X) (star (atom Y)) (atom Z)))", "(X Z)"));
        assertTrue(parse(" (root = (seq (atom X) (star (atom Y)) (atom Z)))", "(X Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (star (atom Y)) (atom Z)))", "(X Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (star (atom Y)) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (star (atom Y)) (atom Z)))", "(X M Z)"));
    }

    /**
     * Test: 20170703022350691558
     *
     * <p>
     * Case: Sequence Rules (One-Or-More Modifiers)
     * </p>
     */
    @Test
    public void test20170703022350691558 ()
    {
        System.out.println("Test: 20170703022350691558");

        assertFalse(parse("(root = (seq (atom X) (plus (atom Y)) (atom Z)))", "(X Z)"));
        assertTrue(parse(" (root = (seq (atom X) (plus (atom Y)) (atom Z)))", "(X Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (plus (atom Y)) (atom Z)))", "(X Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (plus (atom Y)) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (plus (atom Y)) (atom Z)))", "(X M Z)"));
    }

    /**
     * Test: 20170703022350691591
     *
     * <p>
     * Case: Sequence Rules (Exact Modifiers)
     * </p>
     */
    @Test
    public void test20170703022350691591 ()
    {
        System.out.println("Test: 20170703022350691591");

        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 0 0) (atom Z)))", "(X Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 0 0) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 0 0) (atom Z)))", "(X Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 0 0) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 0 0) (atom Z)))", "(X M Z)"));

        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X M Z)"));

        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 2 2) (atom Z)))", "(X Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 2 2) (atom Z)))", "(X Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 2 2) (atom Z)))", "(X Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 2 2) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 2 2) (atom Z)))", "(X M Z)"));

        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 3) (atom Z)))", "(X Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 3) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 3) (atom Z)))", "(X Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 3) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 3) (atom Z)))", "(X M Z)"));

        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 4 4) (atom Z)))", "(X Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 4 4) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 4 4) (atom Z)))", "(X Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 4 4) (atom Z)))", "(X Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 4 4) (atom Z)))", "(X M Z)"));
    }

    /**
     * Test: 20170703022350691619
     *
     * <p>
     * Case: Sequence Rules (Min-Max Modifiers)
     * </p>
     */
    @Test
    public void test20170703022350691619 ()
    {
        System.out.println("Test: 20170703022350691619");

        /**
         * Zero-width rule.
         */
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 1) (atom Z)))", "(X Y Y Z)"));

        /**
         * The match must stop at the maximum.
         */
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 1 2) (atom Z)))", "(X Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 1 2) (atom Z)))", "(X Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 1 2) (atom Z)))", "(X Y Y Y Z)"));

        /**
         * The minimum must be reached.
         */
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 4) (atom Z)))", "(X Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 4) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 4) (atom Z)))", "(X Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 4) (atom Z)))", "(X Y Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 4) (atom Z)))", "(X Y Y Y Y Z)"));

        /**
         * Wider range.
         */
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Y Y Y Z)"));
        assertTrue(parse(" (root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Y Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Y Y Y Y Y Z)"));
        assertFalse(parse("(root = (seq (atom X) (repeat (atom Y) 3 7) (atom Z)))", "(X Y Y Y Y Y Y Y Y Y Z)"));
    }

    /**
     * Test: 20170703022350691646
     *
     * <p>
     * Case: No Root Specified
     * </p>
     */
    @Test
    public void test20170703022350691646 ()
    {
        System.out.println("Test: 20170703022350691646");

        /**
         * By default, the rule named 'root' will be treated as the root rule.
         */
        assertTrue(parse("(root = (keyword @'X'))", "X"));
    }

    /**
     * Test: 20170703022350691671
     *
     * <p>
     * Case: Duplicate Rules
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170703022350691671 ()
    {
        System.out.println("Test: 20170703022350691671");

        parse("(root x) (x = (atom 'X')) (x = (atom 'Y'))", "(X)");
    }

    /**
     * Test: 20170704163111209006
     *
     * <p>
     * Case: Impossible Ranges in Sequence Rules
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170704163111209006 ()
    {
        System.out.println("Test: 20170704163111209006");

        parse("(root = (seq (repeat (atom X) 3 2)))", "X");
    }

    /**
     * Test: 20170703022749118428
     *
     * <p>
     * Case: Regular-Expression Rule - Invalid Regular-Expression
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170703022749118428 ()
    {
        System.out.println("Test: 20170703022749118428");

        /**
         * An opening-bracket indicates the start of a character-class.
         */
        parse("(root = (atom '['))", "X");
    }

    /**
     * Test: 20170711022745123106
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Define Predicate-Rule Condition using Sexpr Parameter.
     * </p>
     */
    @Test
    public void test20170711022745123106 ()
    {
        System.out.println("Test: 20170711022745123106");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public boolean test (final Sexpr arg)
            {
                return arg.isAtom() && arg.asAtom().toString().equals("X");
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();

        final SList input1 = SList.parse("N/A", "A X Z");
        final SList input2 = SList.parse("N/A", "A Y Z");

        assertTrue(schema.match(input1));
        assertFalse(schema.match(input2));
    }

    /**
     * Test: 20170711022745123175
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Define Predicate-Rule Condition using SAtom Parameter.
     * </p>
     */
    @Test
    public void test20170711022745123175 ()
    {
        System.out.println("Test: 20170711022745123175");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public boolean test (final SAtom arg)
            {
                return arg.asAtom().toString().equals("X");
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();

        final SList input1 = SList.parse("N/A", "A X Z");
        final SList input2 = SList.parse("N/A", "A Y Z");
        final SList input3 = SList.parse("N/A", "A (X) Z");

        assertTrue(schema.match(input1));
        assertFalse(schema.match(input2));
        assertFalse(schema.match(input3));
    }

    /**
     * Test: 20170711022745123201
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Define Predicate-Rule Condition using SList Parameter.
     * </p>
     */
    @Test
    public void test20170711022745123201 ()
    {
        System.out.println("Test: 20170711022745123201");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public boolean test (final SList arg)
            {
                return arg.asList().toString().equals("(X)");
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();

        final SList input1 = SList.parse("N/A", "A X Z");
        final SList input2 = SList.parse("N/A", "A Y Z");
        final SList input3 = SList.parse("N/A", "A (X) Z");
        final SList input4 = SList.parse("N/A", "A (Y) Z");
        final SList input5 = SList.parse("N/A", "A () Z");

        assertFalse(schema.match(input1));
        assertFalse(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));
        assertFalse(schema.match(input5));
    }

    /**
     * Test: 20170711022745123225
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Predicate-Rule Condition with Invalid Parameter Count.
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711022745123225 ()
    {
        System.out.println("Test: 20170711022745123225");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public boolean test ()
            {
                return false;
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();
    }

    /**
     * Test: 20170711022745123247
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Predicate-Rule Condition with Invalid Parameter Type.
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711022745123247 ()
    {
        System.out.println("Test: 20170711022745123247");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public boolean test (final Object arg)
            {
                return false;
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();
    }

    /**
     * Test: 20170711022745123269
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Predicate-Rule Condition with Invalid Return Type.
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711022745123269 ()
    {
        System.out.println("Test: 20170711022745123269");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public Object test (final SAtom atom)
            {
                return false;
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();
    }

    /**
     * Test: 20170711022745123289
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Predicate-Rule Condition with Invalid Throws Clause.
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711022745123289 ()
    {
        System.out.println("Test: 20170711022745123289");

        final Object object = new Object()
        {
            @Condition ("TEST1")
            public boolean test (final SAtom atom)
                    throws Exception
            {
                return false;
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (predicate TEST1) (atom Z)))")
                .defineViaAnnotations(object)
                .build();
    }

    /**
     * Test: 20170711022745123310
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with Sexpr Parameter
     * </p>
     */
    @Test
    public void test20170711022745123310 ()
    {
        System.out.println("Test: 20170711022745123310");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute1 (final Sexpr<?> atom)
            {
                list.add("P = " + atom);
            }

            @Pass ("PASS2")
            @Before ("M")
            public void execute2 (final Sexpr<?> atom)
            {
                list.add("Q = " + atom);
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (either (atom X) (atom Y)))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();

        final SList input1 = SList.parse("N/A", "A W Z");
        final SList input2 = SList.parse("N/A", "A X Z");
        final SList input3 = SList.parse("N/A", "A Y Z");
        final SList input4 = SList.parse("N/A", "A Z Z");

        assertFalse(schema.match(input1));
        assertTrue(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));

        assertEquals(4, list.size());
        assertEquals("P = X", list.get(0));
        assertEquals("Q = X", list.get(1));
        assertEquals("P = Y", list.get(2));
        assertEquals("Q = Y", list.get(3));
    }

    /**
     * Test: 20170711022745123330
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with SAtom Parameter
     * </p>
     */
    @Test
    public void test20170711022745123330 ()
    {
        System.out.println("Test: 20170711022745123330");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute1 (final SAtom atom)
            {
                list.add("P = " + atom);
            }

            @Pass ("PASS2")
            @Before ("M")
            public void execute2 (final SAtom atom)
            {
                list.add("Q = " + atom);
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (either (atom X) (atom Y)))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();

        final SList input1 = SList.parse("N/A", "A W Z");
        final SList input2 = SList.parse("N/A", "A X Z");
        final SList input3 = SList.parse("N/A", "A Y Z");
        final SList input4 = SList.parse("N/A", "A Z Z");

        assertFalse(schema.match(input1));
        assertTrue(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));

        assertEquals(4, list.size());
        assertEquals("P = X", list.get(0));
        assertEquals("Q = X", list.get(1));
        assertEquals("P = Y", list.get(2));
        assertEquals("Q = Y", list.get(3));
    }

    /**
     * Test: 20170711022745123349
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with SList Parameter
     * </p>
     */
    @Test
    public void test20170711022745123349 ()
    {
        System.out.println("Test: 20170711022745123349");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute1 (final SList node)
            {
                list.add("P = " + node);
            }

            @Pass ("PASS2")
            @Before ("M")
            public void execute2 (final SList node)
            {
                list.add("Q = " + node);
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();

        final SList input1 = SList.parse("N/A", "A (W) Z");
        final SList input2 = SList.parse("N/A", "A (X) Z");
        final SList input3 = SList.parse("N/A", "A (Y) Z");
        final SList input4 = SList.parse("N/A", "A (Z) Z");

        assertFalse(schema.match(input1));
        assertTrue(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));

        assertEquals(4, list.size());
        assertEquals("P = (X)", list.get(0));
        assertEquals("Q = (X)", list.get(1));
        assertEquals("P = (Y)", list.get(2));
        assertEquals("Q = (Y)", list.get(3));
    }

    /**
     * Test: 20170711023511488273
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with Invalid Parameter Count
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488273 ()
    {
        System.out.println("Test: 20170711023511488273");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute1 ()
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .build();
    }

    /**
     * Test: 20170711023511488349
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with Invalid Parameter Type
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488349 ()
    {
        System.out.println("Test: 20170711023511488349");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute1 (final Object node)
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .build();
    }

    /**
     * Test: 20170711023511488378
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with Invalid Return Type
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488378 ()
    {
        System.out.println("Test: 20170711023511488378");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public Object execute1 (final SList node)
            {
                return null;
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .build();
    }

    /**
     * Test: 20170711023511488405
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Before-Action with Invalid Throws Clause
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488405 ()
    {
        System.out.println("Test: 20170711023511488405");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute1 (final SList node)
                    throws Exception
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .build();
    }

    /**
     * Test: 20170711023511488430
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with Sexpr Parameter
     * </p>
     */
    @Test
    public void test20170711023511488430 ()
    {
        System.out.println("Test: 20170711023511488430");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute1 (final Sexpr<?> atom)
            {
                list.add("P = " + atom);
            }

            @Pass ("PASS2")
            @After ("M")
            public void execute2 (final Sexpr<?> atom)
            {
                list.add("Q = " + atom);
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (either (atom X) (atom Y)))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();

        final SList input1 = SList.parse("N/A", "A W Z");
        final SList input2 = SList.parse("N/A", "A X Z");
        final SList input3 = SList.parse("N/A", "A Y Z");
        final SList input4 = SList.parse("N/A", "A Z Z");

        assertFalse(schema.match(input1));
        assertTrue(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));

        assertEquals(4, list.size());
        assertEquals("P = X", list.get(0));
        assertEquals("Q = X", list.get(1));
        assertEquals("P = Y", list.get(2));
        assertEquals("Q = Y", list.get(3));
    }

    /**
     * Test: 20170711023511488453
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with SAtom Parameter
     * </p>
     */
    @Test
    public void test20170711023511488453 ()
    {
        System.out.println("Test: 20170711023511488453");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute1 (final SAtom atom)
            {
                list.add("P = " + atom);
            }

            @Pass ("PASS2")
            @After ("M")
            public void execute2 (final SAtom atom)
            {
                list.add("Q = " + atom);
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (either (atom X) (atom Y)))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();

        final SList input1 = SList.parse("N/A", "A W Z");
        final SList input2 = SList.parse("N/A", "A X Z");
        final SList input3 = SList.parse("N/A", "A Y Z");
        final SList input4 = SList.parse("N/A", "A Z Z");

        assertFalse(schema.match(input1));
        assertTrue(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));

        assertEquals(4, list.size());
        assertEquals("P = X", list.get(0));
        assertEquals("Q = X", list.get(1));
        assertEquals("P = Y", list.get(2));
        assertEquals("Q = Y", list.get(3));
    }

    /**
     * Test: 20170711023511488474
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with SList Parameter
     * </p>
     */
    @Test
    public void test20170711023511488474 ()
    {
        System.out.println("Test: 20170711023511488474");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute1 (final SList node)
            {
                list.add("P = " + node);
            }

            @Pass ("PASS2")
            @After ("M")
            public void execute2 (final SList node)
            {
                list.add("Q = " + node);
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();

        final SList input1 = SList.parse("N/A", "A (W) Z");
        final SList input2 = SList.parse("N/A", "A (X) Z");
        final SList input3 = SList.parse("N/A", "A (Y) Z");
        final SList input4 = SList.parse("N/A", "A (Z) Z");

        assertFalse(schema.match(input1));
        assertTrue(schema.match(input2));
        assertTrue(schema.match(input3));
        assertFalse(schema.match(input4));

        assertEquals(4, list.size());
        assertEquals("P = (X)", list.get(0));
        assertEquals("Q = (X)", list.get(1));
        assertEquals("P = (Y)", list.get(2));
        assertEquals("Q = (Y)", list.get(3));
    }

    /**
     * Test: 20170711023511488494
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with Invalid Parameter Count
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488494 ()
    {
        System.out.println("Test: 20170711023511488494");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute ()
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();
    }

    /**
     * Test: 20170711023511488514
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with Invalid Parameter Type
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488514 ()
    {
        System.out.println("Test: 20170711023511488514");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute (final Object node)
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();
    }

    /**
     * Test: 20170711023511488534
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with Invalid Return Type
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711023511488534 ()
    {
        System.out.println("Test: 20170711023511488534");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public Object execute (final SList node)
            {
                return null;
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();
    }

    /**
     * Test: 20170711024605706433
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: After-Action with Invalid Throws Clause
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711024605706433 ()
    {
        System.out.println("Test: 20170711024605706433");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute (final SList node)
                    throws Exception
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .build();
    }

    /**
     * Test: 20170711024605706536
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: No Such Translation Pass for Before-Action
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170711024605706536 ()
    {
        System.out.println("Test: 20170711024605706536");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("M")
            public void execute (final SList node)
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .build();
    }

    /**
     * Test: 20170711024605706563
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: No Such Translation Pass for After-Action
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170711024605706563 ()
    {
        System.out.println("Test: 20170711024605706563");

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @After ("M")
            public void execute (final SList node)
            {
                // Pass
            }
        };

        SexprSchema.fromString("N/A", "(root = (seq (atom A) (ref M) (atom Z))) (M = (seq (either (atom X) (atom Y))))")
                .defineViaAnnotations(object)
                .build();
    }

    @Pass ("PASS1")
    private static final class TestClass1
    {
        public final List<String> calls = new ArrayList<>();

        @Pass ("PASS3") // Overrides PASS1 (above)
        @Before ("A")
        public void execute1 (final Sexpr<?> node)
        {
            calls.add("A = " + node.toString());
        }

        @Pass ("PASS2") // Overrides PASS1 (above)
        @Before ("B")
        public void execute2 (final Sexpr<?> node)
        {
            calls.add("B = " + node.toString());
        }

        @Before ("C")
        public void execute3 (final Sexpr<?> node)
        {
            calls.add("C = " + node.toString());
        }
    };

    /**
     * Test: 20170711025550465323
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Overridden Translation Pass for Before-Action
     * </p>
     */
    @Test
    public void test20170711025550465323 ()
    {
        System.out.println("Test: 20170711025550465323");

        final TestClass1 object = new TestClass1();

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom X) (ref A) (ref B) (ref C) (atom X))) (A = (atom 13)) (B = (atom 17)) (C = (atom 19))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .pass("PASS3")
                .build();

        final SList input1 = SList.parse("N/A", "X 13 17 19 X");

        assertTrue(schema.match(input1));

        assertEquals(3, object.calls.size());
        assertEquals("C = 19", object.calls.get(0));
        assertEquals("B = 17", object.calls.get(1));
        assertEquals("A = 13", object.calls.get(2));
    }

    @Pass ("PASS1")
    private static final class TestClass2
    {
        public final List<String> calls = new ArrayList<>();

        @Pass ("PASS3") // Overrides PASS1 (above)
        @After ("A")
        public void execute1 (final Sexpr<?> node)
        {
            calls.add("A = " + node.toString());
        }

        @Pass ("PASS2") // Overrides PASS1 (above)
        @After ("B")
        public void execute2 (final Sexpr<?> node)
        {
            calls.add("B = " + node.toString());
        }

        @After ("C")
        public void execute3 (final Sexpr<?> node)
        {
            calls.add("C = " + node.toString());
        }
    };

    /**
     * Test: 20170711025550465390
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Overridden Translation Pass for After-Action
     * </p>
     */
    @Test
    public void test20170711025550465390 ()
    {
        System.out.println("Test: 20170711025550465390");

        final TestClass2 object = new TestClass2();

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root = (seq (atom X) (ref A) (ref B) (ref C) (atom X))) (A = (atom 13)) (B = (atom 17)) (C = (atom 19))")
                .defineViaAnnotations(object)
                .pass("PASS1")
                .pass("PASS2")
                .pass("PASS3")
                .build();

        final SList input1 = SList.parse("N/A", "X 13 17 19 X");

        assertTrue(schema.match(input1));

        assertEquals(3, object.calls.size());
        assertEquals("C = 19", object.calls.get(0));
        assertEquals("B = 17", object.calls.get(1));
        assertEquals("A = 13", object.calls.get(2));
    }

    /**
     * Test: 20180902012950783308
     *
     * <p>
     * Case: Order Of Actions.
     * </p>
     */
    @Test
    public void test20180902012950783308 ()
    {
        System.out.println("Test: 20180902012950783308");

        final List<String> list = new ArrayList<>();

        final Object object = new Object()
        {
            @Pass ("PASS1")
            @Before ("A")
            public void action1 (final Sexpr<?> node)
            {
                list.add("P1BA");
            }

            @Pass ("PASS1")
            @After ("A")
            public void action2 (final Sexpr<?> node)
            {
                list.add("P1AA");
            }

            @Pass ("PASS1")
            @Before ("B")
            public void action3 (final Sexpr<?> node)
            {
                list.add("P1BB");
            }

            @Pass ("PASS1")
            @After ("B")
            public void action4 (final Sexpr<?> node)
            {
                list.add("P1AB");
            }

            @Pass ("PASS1")
            @Before ("C")
            public void action5 (final Sexpr<?> node)
            {
                list.add("P1BC");
            }

            @Pass ("PASS1")
            @After ("C")
            public void action6 (final Sexpr<?> node)
            {
                list.add("P1AC");
            }

            @Pass ("PASS2")
            @Before ("A")
            public void action7 (final Sexpr<?> node)
            {
                list.add("P2BA");
            }

            @Pass ("PASS2")
            @After ("A")
            public void action8 (final Sexpr<?> node)
            {
                list.add("P2AA");
            }

            @Pass ("PASS2")
            @Before ("B")
            public void action9 (final Sexpr<?> node)
            {
                list.add("P2BB");
            }

            @Pass ("PASS2")
            @After ("B")
            public void action10 (final Sexpr<?> node)
            {
                list.add("P2AB");
            }

            @Pass ("PASS2")
            @Before ("C")
            public void action11 (final Sexpr<?> node)
            {
                list.add("P2BC");
            }

            @Pass ("PASS2")
            @After ("C")
            public void action12 (final Sexpr<?> node)
            {
                list.add("P2AC");
            }
        };

        final SexprSchema schema = SexprSchema
                .fromString("N/A", "(root A) (A = (seq (atom X) (ref B) (atom X))) (B = (seq (atom Y) (ref C) (atom Y))) (C = (atom Z))")
                .pass("PASS1")
                .pass("PASS2")
                .defineViaAnnotations(object)
                .build();

        final SList input = SList.parse("N/A", "X (Y Z Y) X");

        assertTrue(schema.match(input));

        // Pass #1
        assertEquals("P1BA", list.get(0));
        assertEquals("P1BB", list.get(1));
        assertEquals("P1BC", list.get(2));
        assertEquals("P1AC", list.get(3));
        assertEquals("P1AB", list.get(4));
        assertEquals("P1AA", list.get(5));

        // Pass #2
        assertEquals("P2BA", list.get(6));
        assertEquals("P2BB", list.get(7));
        assertEquals("P2BC", list.get(8));
        assertEquals("P2AC", list.get(9));
        assertEquals("P2AB", list.get(10));
        assertEquals("P2AA", list.get(11));
    }
}
