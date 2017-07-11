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

import com.mackenziehigh.sexpr.internal.schema.Schema;
import com.mackenziehigh.sexpr.internal.schema.SchemaParser;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class SexprSchemaTest
{

    private boolean parse (final String schema,
                           final String expression)
    {
        /**
         * Remember, parse(*) adds an implicit pair of parentheses.
         * Remove the extra parentheses for the sake of simplicity.
         */
        final Sexpr tree = SList.parse(SexprSchemaTest.class.getName(), expression).toList().get(0);

        /**
         * Parse the schema.
         */
        final SchemaParser parser = new SchemaParser();
        final Schema grammar = parser.parse(SexprSchemaTest.class.getName(), schema);

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

        assertTrue(parse(" root = $BOOLEAN;", "true"));
        assertTrue(parse(" root = $BOOLEAN;", "false"));
        assertFalse(parse("root = $BOOLEAN;", "XYZ"));

        assertTrue(parse(" root = $BYTE;", "100"));
        assertTrue(parse(" root = $BYTE;", "-100"));
        assertFalse(parse("root = $BYTE;", "30.0"));
        assertFalse(parse("root = $BYTE;", "XYZ"));

        assertTrue(parse(" root = $SHORT;", "1000"));
        assertTrue(parse(" root = $SHORT;", "-200"));
        assertFalse(parse("root = $SHORT;", "30.0"));
        assertFalse(parse("root = $SHORT;", "XYZ"));

        assertTrue(parse(" root = $INT;", "1000"));
        assertTrue(parse(" root = $INT;", "-200"));
        assertFalse(parse("root = $INT;", "30.0"));
        assertFalse(parse("root = $INT;", "XYZ"));

        assertTrue(parse(" root = $LONG;", "1000"));
        assertTrue(parse(" root = $LONG;", "-200"));
        assertFalse(parse("root = $LONG;", "30.0"));
        assertFalse(parse("root = $LONG;", "XYZ"));
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

        assertTrue(parse("root = '123';", "123"));
        assertTrue(parse("root = \"123\";", "123"));
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

        assertTrue(parse("root = matches 'X[0-9]*Y';", "XY"));
        assertTrue(parse("root = matches 'X[0-9]*Y';", "X1Y"));
        assertTrue(parse("root = matches 'X[0-9]*Y';", "X12Y"));
        assertTrue(parse("root = matches 'X[0-9]*Y';", "X123Y"));

        assertFalse(parse("root = matches 'X[0-9]+Y';", "XYZ"));
    }

    /**
     * Test: 20170703020824718979
     *
     * <p>
     * Case: Range Rules
     * </p>
     */
    @Test
    public void test20170703020824718979 ()
    {
        System.out.println("Test: 20170703020824718979");

        assertFalse(parse("root = range 100 < X < 200;", "99"));
        assertFalse(parse("root = range 100 < X < 200;", "100"));
        assertTrue(parse(" root = range 100 < X < 200;", "101"));
        assertTrue(parse(" root = range 100 < X < 200;", "199"));
        assertFalse(parse("root = range 100 < X < 200;", "200"));
        assertFalse(parse("root = range 100 < X < 200;", "201"));

        assertFalse(parse("root = range 100 < X <= 200;", "99"));
        assertFalse(parse("root = range 100 < X <= 200;", "100"));
        assertTrue(parse(" root = range 100 < X <= 200;", "101"));
        assertTrue(parse(" root = range 100 < X <= 200;", "199"));
        assertTrue(parse(" root = range 100 < X <= 200;", "200"));
        assertFalse(parse("root = range 100 < X <= 200;", "201"));

        assertFalse(parse("root = range 100 <= X < 200;", "99"));
        assertTrue(parse(" root = range 100 <= X < 200;", "100"));
        assertTrue(parse(" root = range 100 <= X < 200;", "101"));
        assertTrue(parse(" root = range 100 <= X < 200;", "199"));
        assertFalse(parse("root = range 100 <= X < 200;", "200"));
        assertFalse(parse("root = range 100 <= X < 200;", "201"));

        assertFalse(parse("root = range 100 <= X <= 200;", "99"));
        assertTrue(parse(" root = range 100 <= X <= 200;", "100"));
        assertTrue(parse(" root = range 100 <= X <= 200;", "101"));
        assertTrue(parse(" root = range 100 <= X <= 200;", "199"));
        assertTrue(parse(" root = range 100 <= X <= 200;", "200"));
        assertFalse(parse("root = range 100 <= X <= 200;", "201"));
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

        assertTrue(parse("root = matches 'X[0-9]+.' & matches '.[0-9]+Y';", "X123Y"));

        assertFalse(parse("root = matches 'X[0-9]+.' & matches '.[0-9]+Y';", "X123Z"));
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

        assertTrue(parse("root = '12' / '34' / '56';", "12"));
        assertTrue(parse("root = '12' / '34' / '56';", "34"));
        assertTrue(parse("root = '12' / '34' / '56';", "56"));

        assertFalse(parse("root = '12' / '34' / '56';", "78"));
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

        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "0"));
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "1"));
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "2"));
        assertFalse(parse("root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "3"));
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "4"));
        assertFalse(parse("root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "5"));
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "6"));
        assertFalse(parse("root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "7"));
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "8"));
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "9"));

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

        assertTrue(parse("root = [];", "()"));
        assertTrue(parse("root = [ '12' ];", "(12)"));
        assertTrue(parse("root = [ '12' , '34' ];", "(12 34)"));
        assertTrue(parse("root = [ '12' , '34' , '56' ];", "(12 34 56)"));

        assertFalse(parse("root = [ '12' , '34' ];", "(12 34 56)"));
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

        assertTrue(parse(" root = [ 'X' , 'Y' ? , 'Z' ];", "(X Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' ? , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' ? , 'Z' ];", "(X Y Y Z)"));
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

        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' * , 'Z' ];", "(X M Z)"));
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

        assertFalse(parse("root = [ 'X' , 'Y' + , 'Z' ];", "(X Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' + , 'Z' ];", "(X Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' + , 'Z' ];", "(X Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' + , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' + , 'Z' ];", "(X M Z)"));
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

        assertTrue(parse(" root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X M Z)"));

        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X M Z)"));

        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X M Z)"));

        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X M Z)"));

        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X M Z)"));
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
        assertTrue(parse(" root = [ 'X' , 'Y' { 0 , 0 } , 'Y' , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 0 , 0 } , 'Y' , 'Z' ];", "(X Y Y Z)"));

        /**
         * The match must stop at the maximum.
         */
        assertFalse(parse("root = [ 'X' , 'Y' { 0 , 1 } , 'Y' , 'Z' ];", "(X Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 0 , 1 } , 'Y' , 'Z' ];", "(X Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 0 , 1 } , 'Y' , 'Z' ];", "(X Y Y Y Z)"));

        /**
         * The minimum must be reached.
         */
        assertFalse(parse("root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Y Y Y Z)"));

        /**
         * Wider range.
         */
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Z)"));
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Y Y Z)"));
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Y Y Y Z)"));
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
        assertTrue(parse("root = 'X';", "X"));
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

        parse("root = [ x * ]; x = 'X'; x = 'Y';", "(X)");
    }

    /**
     * Test: 20170703022350691694
     *
     * <p>
     * Case: Undefined Rules in And Rules
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170703022350691694 ()
    {
        System.out.println("Test: 20170703022350691694");

        parse("root = 'X' & Y & 'Z';", "X");
    }

    /**
     * Test: 20170703022350691715
     *
     * <p>
     * Case: Undefined Rules in Or Rules
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170703022350691715 ()
    {
        System.out.println("Test: 20170703022350691715");

        parse("root = 'X' / Y / 'Z';", "X");
    }

    /**
     * Test: 20170703022749118188
     *
     * <p>
     * Case: Undefined Rules in Not Rules
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170703022749118188 ()
    {
        System.out.println("Test: 20170703022749118188");

        parse("root = ! X;", "X");
    }

    /**
     * Test: 20170703022749118314
     *
     * <p>
     * Case: Undefined Rules in Sequence Rules
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170703022749118314 ()
    {
        System.out.println("Test: 20170703022749118314");

        parse("root = [ 'X' , Y , 'Z' ];", "X");
    }

    /**
     * Test: 20170703022749118285
     *
     * <p>
     * Case: Impossible Range Rules (Max less than Min)
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170703022749118285 ()
    {
        System.out.println("Test: 20170703022749118285");

        parse("root = range 100.001 < X < 100.000;", "X");
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

        parse("root = [ 'X' , 'Y' { 3 , 2 } , 'Z' ];", "X");
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
        parse("root = matches '[';", "X");
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
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
        fail();
    }

    /**
     * Test: 20170711024605706505
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: No Such Translation Pass for Predicate-Rule Condition
     * </p>
     */
    @Test (expected = IllegalArgumentException.class)
    public void test20170711024605706505 ()
    {
        System.out.println("Test: 20170711024605706505");
        fail();
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
    @Test (expected = IllegalArgumentException.class)
    public void test20170711024605706536 ()
    {
        System.out.println("Test: 20170711024605706536");
        fail();
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
    @Test (expected = IllegalArgumentException.class)
    public void test20170711024605706563 ()
    {
        System.out.println("Test: 20170711024605706563");
        fail();
    }

    /**
     * Test: 20170711024605706593
     *
     * <p>
     * Method: <code>defineViaAnnotations</code>
     * </p>
     *
     * <p>
     * Case: Overridden Translation Pass for Predicate-Rule Condition
     * </p>
     */
    @Test
    public void test20170711024605706593 ()
    {
        System.out.println("Test: 20170711024605706593");
        fail();
    }

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
        fail();
    }

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
        fail();
    }
}
