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

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class TreeMutatorTest
{
    /**
     * Test: 20170625033301237647
     *
     * <p>
     * Method: <code>append</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237647 ()
    {
        System.out.println("Test: 20170625033301237647");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        final SList actual = tree.mutator().get(1).append(SAtom.fromString("X"));
        assertEquals("((1 2) (3 4 X) (5 6))", actual.toString());
    }

    /**
     * Test: 20170625033301237730
     *
     * <p>
     * Method: <code>prepend</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237730 ()
    {
        System.out.println("Test: 20170625033301237730");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        final SList actual = tree.mutator().get(1).prepend(SAtom.fromString("X"));
        assertEquals("((1 2) (X 3 4) (5 6))", actual.toString());
    }

    /**
     * Test: 20170625033301237756
     *
     * <p>
     * Method: <code>set</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237756 ()
    {
        System.out.println("Test: 20170625033301237756");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        final SList actual1 = tree.mutator().get(1).set(SAtom.fromString("X"));
        assertEquals("((1 2) X (5 6))", actual1.toString());

        final SList actual2 = tree.mutator().get(2).get(0).set(SAtom.fromString("X"));
        assertEquals("((1 2) (3 4) (X 6))", actual2.toString());
    }

    /**
     * Test: 20170625033301237781
     *
     * <p>
     * Method: <code>get</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237781 ()
    {
        System.out.println("Test: 20170625033301237781");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        assertEquals("(1 2)", tree.mutator().get(0).toString());
        assertEquals("(3 4)", tree.mutator().get(1).toString());
        assertEquals("(5 6)", tree.mutator().get(2).toString());

        assertEquals("1", tree.mutator().get(0).get(0).toString());
        assertEquals("2", tree.mutator().get(0).get(1).toString());
        assertEquals("3", tree.mutator().get(1).get(0).toString());
        assertEquals("4", tree.mutator().get(1).get(1).toString());
        assertEquals("5", tree.mutator().get(2).get(0).toString());
        assertEquals("6", tree.mutator().get(2).get(1).toString());
    }

    /**
     * Test: 20170625033301237805
     *
     * <p>
     * Method: <code>first() and last()</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237805 ()
    {
        System.out.println("Test: 20170625033301237805");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        assertEquals("(1 2)", tree.mutator().get(0).toString());
        assertEquals("(3 4)", tree.mutator().get(1).toString());
        assertEquals("(5 6)", tree.mutator().get(2).toString());

        assertEquals("1", tree.mutator().first().first().toString());
        assertEquals("2", tree.mutator().first().last().toString());
        assertEquals("3", tree.mutator().get(1).first().toString());
        assertEquals("4", tree.mutator().get(1).last().toString());
        assertEquals("5", tree.mutator().last().first().toString());
        assertEquals("6", tree.mutator().last().last().toString());
    }

    /**
     * Test: 20170625033301237845
     *
     * <p>
     * Method: <code>node</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237845 ()
    {
        System.out.println("Test: 20170625033301237845");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        assertEquals("(1 2)", tree.mutator().get(0).node().toString());
        assertEquals("(3 4)", tree.mutator().get(1).node().toString());
        assertEquals("(5 6)", tree.mutator().get(2).node().toString());

        assertEquals("1", tree.mutator().first().first().node().toString());
        assertEquals("2", tree.mutator().first().last().node().toString());
        assertEquals("3", tree.mutator().get(1).first().node().toString());
        assertEquals("4", tree.mutator().get(1).last().node().toString());
        assertEquals("5", tree.mutator().last().first().node().toString());
        assertEquals("6", tree.mutator().last().last().node().toString());
    }

    /**
     * Test: 20170625033301237865
     *
     * <p>
     * Method: <code>toString</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625033301237865 ()
    {
        System.out.println("Test: 20170625033301237865");

        final SList tree = SList.parse("test", "(1 2) (3 4) (5 6)");
        assertEquals("((1 2) (3 4) (5 6))", tree.toString());

        assertEquals("(1 2)", tree.mutator().get(0).toString());
        assertEquals("(3 4)", tree.mutator().get(1).toString());
        assertEquals("(5 6)", tree.mutator().get(2).toString());

        assertEquals("1", tree.mutator().first().first().toString());
        assertEquals("2", tree.mutator().first().last().toString());
        assertEquals("3", tree.mutator().get(1).first().toString());
        assertEquals("4", tree.mutator().get(1).last().toString());
        assertEquals("5", tree.mutator().last().first().toString());
        assertEquals("6", tree.mutator().last().last().toString());
    }

    /**
     * Test: 20170625061152972231
     *
     * <p>
     * Method: <code>append</code>
     * </p>
     *
     * <p>
     * Case: not a list
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170625061152972231 ()
    {
        System.out.println("Test: 20170625061152972231");

        final SList tree = SList.parse("test", "5");
        assertEquals("(5)", tree.toString());

        tree.mutator().get(0).append(SAtom.fromInt(0));
    }

    /**
     * Test: 20170625061152972298
     *
     * <p>
     * Method: <code>prepend</code>
     * </p>
     *
     * <p>
     * Case: not a list
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170625061152972298 ()
    {
        System.out.println("Test: 20170625061152972298");

        final SList tree = SList.parse("test", "5");
        assertEquals("(5)", tree.toString());

        tree.mutator().get(0).prepend(SAtom.fromInt(0));
    }

    /**
     * Test: 20170625061152972324
     *
     * <p>
     * Method: <code>set</code>
     * </p>
     *
     * <p>
     * Case: root node
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170625061152972324 ()
    {
        System.out.println("Test: 20170625061152972324");

        final SList tree = SList.parse("test", "5");
        assertEquals("(5)", tree.toString());

        tree.mutator().set(SAtom.fromInt(0));
    }

    /**
     * Test: 20170625061152972389
     *
     * <p>
     * Method: <code>first</code>
     * </p>
     *
     * <p>
     * Case: not a list
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170625061152972389 ()
    {
        System.out.println("Test: 20170625061152972389");

        final SList tree = SList.parse("test", "5");
        assertEquals("(5)", tree.toString());

        tree.mutator().get(0).first();
    }

    /**
     * Test: 20170625061152972407
     *
     * <p>
     * Method: <code>first</code>
     * </p>
     *
     * <p>
     * Case: empty list
     * </p>
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void test20170625061152972407 ()
    {
        System.out.println("Test: 20170625061152972407");

        final SList tree = SList.parse("test", "");
        assertEquals("()", tree.toString());

        tree.mutator().first();
    }

    /**
     * Test: 20170625061152972427
     *
     * <p>
     * Method: <code>last</code>
     * </p>
     *
     * <p>
     * Case: not a list
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170625061152972427 ()
    {
        System.out.println("Test: 20170625061152972427");

        final SList tree = SList.parse("test", "5");
        assertEquals("(5)", tree.toString());

        tree.mutator().get(0).last();
    }

    /**
     * Test: 20170625061152972449
     *
     * <p>
     * Method: <code>last</code>
     * </p>
     *
     * <p>
     * Case: empty list
     * </p>
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void test20170625061152972449 ()
    {
        System.out.println("Test: 20170625061152972449");

        final SList tree = SList.parse("test", "");
        assertEquals("()", tree.toString());

        tree.mutator().last();
    }

    /**
     * Test: 20170625061426634882
     *
     * <p>
     * Method: <code>get</code>
     * </p>
     *
     * <p>
     * Case: not a list
     * </p>
     */
    @Test (expected = IllegalStateException.class)
    public void test20170625061426634882 ()
    {
        System.out.println("Test: 20170625061426634882");

        final SList tree = SList.parse("test", "5");
        assertEquals("(5)", tree.toString());

        tree.mutator().get(0).get(0);
    }

    /**
     * Test: 20170625061426634939
     *
     * <p>
     * Method: <code>get</code>
     * </p>
     *
     * <p>
     * Case: index too large
     * </p>
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void test20170625061426634939 ()
    {
        System.out.println("Test: 20170625061426634939");

        final SList tree = SList.parse("test", "1 2 3");
        assertEquals("(1 2 3)", tree.toString());

        tree.mutator().get(3);
    }

    /**
     * Test: 20170625061426634960
     *
     * <p>
     * Method: <code>get</code>
     * </p>
     *
     * <p>
     * Case: negative index
     * </p>
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void test20170625061426634960 ()
    {
        System.out.println("Test: 20170625061426634960");

        final SList tree = SList.parse("test", "1 2 3");
        assertEquals("(1 2 3)", tree.toString());

        tree.mutator().get(-1);
    }
}
