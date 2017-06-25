package com.mackenziehigh.sexpr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;
import static org.junit.Assert.*;
import org.junit.Test;

public class SListTest
{
    /**
     * Test: 20170617093030549525
     *
     * <p>
     * Method: <code>get(*)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093030549525 ()
    {
        System.out.println("Test: 20170617093030549525");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list = SList.of(e1, e2, e3);

        assertEquals(e1, list.get(0));
        assertEquals(e2, list.get(1));
        assertEquals(e3, list.get(2));
    }

    /**
     * Test: 20170617093030549582
     *
     * <p>
     * Method: <code>get(*)</code>
     * </p>
     *
     * <p>
     * Case: out of bounds (too large)
     * </p>
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void test20170617093030549582 ()
    {
        System.out.println("Test: 20170617093030549582");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list = SList.of(e1, e2, e3);

        list.get(3);
    }

    /**
     * Test: 20170617093030549603
     *
     * <p>
     * Method: <code>get(*)</code>
     * </p>
     *
     * <p>
     * Case: out of bounds (too small)
     * </p>
     */
    @Test (expected = IndexOutOfBoundsException.class)
    public void test20170617093030549603 ()
    {
        System.out.println("Test: 20170617093030549603");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list = SList.of(e1, e2, e3);

        list.get(-1);
    }

    /**
     * Test: 20170617093030549621
     *
     * <p>
     * Method: <code>size()</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093030549621 ()
    {
        System.out.println("Test: 20170617093030549621");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list = SList.of(e1, e2, e3);

        assertEquals(3, list.size());
    }

    /**
     * Test: 20170617093030549637
     *
     * <p>
     * Method: <code>toString()</code>
     * </p>
     *
     * <p>
     * Case: all cases()
     * </p>
     */
    @Test
    public void test20170617093030549637 ()
    {
        System.out.println("Test: 20170617093030549637");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list0 = SList.of();
        final SList list1 = SList.of(e1);
        final SList list2 = SList.of(e1, e2);
        final SList list3 = SList.of(e1, e2, e3);
        assertEquals("()", list0.toString());
        assertEquals("(100)", list1.toString());
        assertEquals("(100 200)", list2.toString());
        assertEquals("(100 200 300)", list3.toString());

    }

    /**
     * Test: 20170617093030549653
     *
     * <p>
     * Method: <code>first()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617093030549653 ()
    {
        System.out.println("Test: 20170617093030549653");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list0 = SList.of();
        final SList list1 = SList.of(e1);
        final SList list2 = SList.of(e1, e2);
        final SList list3 = SList.of(e1, e2, e3);
        assertNull(list0.first());
        assertEquals(e1, list1.first());
        assertEquals(e1, list2.first());
        assertEquals(e1, list3.first());
    }

    /**
     * Test: 20170617093030549670
     *
     * <p>
     * Method: <code>last()</code>
     * </p>
     *
     * <p>
     * Case: all cases
     * </p>
     */
    @Test
    public void test20170617093030549670 ()
    {
        System.out.println("Test: 20170617093030549670");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list0 = SList.of();
        final SList list1 = SList.of(e1);
        final SList list2 = SList.of(e1, e2);
        final SList list3 = SList.of(e1, e2, e3);
        assertNull(list0.last());
        assertEquals(e1, list1.last());
        assertEquals(e2, list2.last());
        assertEquals(e3, list3.last());
    }

    /**
     * Test: 20170617093030549687
     *
     * <p>
     * Method: <code>tail()</code>
     * </p>
     *
     * <p>
     * Case: all cases()
     * </p>
     */
    @Test
    public void test20170617093030549687 ()
    {
        System.out.println("Test: 20170617093030549687");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list0 = SList.of();
        final SList list1 = SList.of(e1);
        final SList list2 = SList.of(e1, e2);
        final SList list3 = SList.of(e1, e2, e3);
        assertTrue(list0.tail().isEmpty());
        assertTrue(list1.tail().isEmpty());
        assertEquals(e2, list2.tail().first());
        assertEquals(e2, list2.tail().last());
        assertEquals(1, list2.tail().size());
        assertEquals(e2, list3.tail().first());
        assertEquals(e3, list3.tail().last());
        assertEquals(2, list3.tail().size());

        /**
         * Make sure that the tail() has the same location()
         */
        final SourceLocation location = new SourceLocation("Vulcan", 2, 3);
        assertEquals(location, SList.of(location, e1, e2, e3).tail().location());
    }

    /**
     * Test: 20170617093030549703
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
    public void test20170617093030549703 ()
    {
        System.out.println("Test: 20170617093030549703");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list = SList.of(e1, e2, e3);

        assertFalse(list.isAtom());
    }

    /**
     * Test: 20170617093030549718
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
    public void test20170617093030549718 ()
    {
        System.out.println("Test: 20170617093030549718");

        final SAtom e1 = new SAtom("100");
        final SAtom e2 = new SAtom("200");
        final SAtom e3 = new SAtom("300");
        final SList list = SList.of(e1, e2, e3);

        assertTrue(list.isList());
    }

    /**
     * Test: 20170624065446286330
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
    public void test20170624065446286330 ()
    {
        System.out.println("Test: 20170624065446286330");

        /**
         * Create a tree based on an image from Wikipedia.
         */
        // https://commons.wikimedia.org/wiki/File:Depth-first-tree.svg
        final SAtom N04 = new SAtom("4");
        final SAtom N05 = new SAtom("5");
        final SAtom N06 = new SAtom("6");
        final SAtom N07 = new SAtom("7");
        final SAtom N10 = new SAtom("10");
        final SAtom N11 = new SAtom("11");
        final SAtom N12 = new SAtom("12");
        final SList N03 = SList.of(N04, N05);
        final SList N02 = SList.of(N03, N06);
        final SList N09 = SList.of(N10, N11);
        final SList N08 = SList.of(N09, N12);
        final SList N01 = SList.of(N02, N07, N08);
        final SList tree = N01;

        /**
         * Perform a full tree transversal and record the visitation order.
         */
        final List<Sexpr> nodes = new ArrayList<>();
        final Predicate<Sexpr> visitor1 = x ->
        {
            nodes.add(x);
            return false;
        };
        assertFalse(tree.dfs(visitor1));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(12, nodes.size());
        assertEquals(N01, nodes.get(0));
        assertEquals(N02, nodes.get(1));
        assertEquals(N03, nodes.get(2));
        assertEquals(N04, nodes.get(3));
        assertEquals(N05, nodes.get(4));
        assertEquals(N06, nodes.get(5));
        assertEquals(N07, nodes.get(6));
        assertEquals(N08, nodes.get(7));
        assertEquals(N09, nodes.get(8));
        assertEquals(N10, nodes.get(9));
        assertEquals(N11, nodes.get(10));
        assertEquals(N12, nodes.get(11));

        /**
         * Perform a partial tree transversal and record the visitation order.
         */
        nodes.clear();
        final Predicate<Sexpr> visitor2 = x ->
        {
            nodes.add(x);
            return x.equals(N07);
        };
        assertTrue(tree.dfs(visitor2));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(7, nodes.size());
        assertEquals(N01, nodes.get(0));
        assertEquals(N02, nodes.get(1));
        assertEquals(N03, nodes.get(2));
        assertEquals(N04, nodes.get(3));
        assertEquals(N05, nodes.get(4));
        assertEquals(N06, nodes.get(5));
        assertEquals(N07, nodes.get(6));
    }

    /**
     * Test: 20170624065446286459
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
    public void test20170624065446286459 ()
    {
        System.out.println("Test: 20170624065446286459");

        /**
         * Create a tree based on an image from Wikipedia.
         */
        // https://en.wikipedia.org/wiki/Breadth-first_search#/media/File:Breadth-first-tree.svg
        final SAtom N09 = new SAtom("9");
        final SAtom N10 = new SAtom("10");
        final SAtom N06 = new SAtom("6");
        final SAtom N03 = new SAtom("3");
        final SAtom N11 = new SAtom("11");
        final SAtom N12 = new SAtom("12");
        final SAtom N08 = new SAtom("8");
        final SList N05 = SList.of(N09, N10);
        final SList N02 = SList.of(N05, N06);
        final SList N07 = SList.of(N11, N12);
        final SList N04 = SList.of(N07, N08);
        final SList N01 = SList.of(N02, N03, N04);
        final SList tree = N01;

        /**
         * Perform a full tree transversal and record the visitation order.
         */
        final List<Sexpr> nodes = new ArrayList<>();
        final Predicate<Sexpr> visitor1 = x ->
        {
            nodes.add(x);
            return false;
        };
        assertFalse(tree.bfs(visitor1));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(12, nodes.size());
        assertEquals(N01, nodes.get(0));
        assertEquals(N02, nodes.get(1));
        assertEquals(N03, nodes.get(2));
        assertEquals(N04, nodes.get(3));
        assertEquals(N05, nodes.get(4));
        assertEquals(N06, nodes.get(5));
        assertEquals(N07, nodes.get(6));
        assertEquals(N08, nodes.get(7));
        assertEquals(N09, nodes.get(8));
        assertEquals(N10, nodes.get(9));
        assertEquals(N11, nodes.get(10));
        assertEquals(N12, nodes.get(11));

        /**
         * Perform a partial tree transversal and record the visitation order.
         */
        nodes.clear();
        final Predicate<Sexpr> visitor2 = x ->
        {
            nodes.add(x);
            return x.equals(N07);
        };
        assertTrue(tree.bfs(visitor2));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(7, nodes.size());
        assertEquals(N01, nodes.get(0));
        assertEquals(N02, nodes.get(1));
        assertEquals(N03, nodes.get(2));
        assertEquals(N04, nodes.get(3));
        assertEquals(N05, nodes.get(4));
        assertEquals(N06, nodes.get(5));
        assertEquals(N07, nodes.get(6));
    }

    /**
     * Test: 20170624065446286488
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
    public void test20170624065446286488 ()
    {
        System.out.println("Test: 20170624065446286488");

        /**
         * Create a tree based on an image from Wikipedia.
         * preorder is equivalent to DFS.
         */
        // https://commons.wikimedia.org/wiki/File:Depth-first-tree.svg
        final SAtom N04 = new SAtom("4");
        final SAtom N05 = new SAtom("5");
        final SAtom N06 = new SAtom("6");
        final SAtom N07 = new SAtom("7");
        final SAtom N10 = new SAtom("10");
        final SAtom N11 = new SAtom("11");
        final SAtom N12 = new SAtom("12");
        final SList N03 = SList.of(N04, N05);
        final SList N02 = SList.of(N03, N06);
        final SList N09 = SList.of(N10, N11);
        final SList N08 = SList.of(N09, N12);
        final SList N01 = SList.of(N02, N07, N08);
        final SList tree = N01;

        /**
         * Perform a full tree transversal and record the visitation order.
         */
        final List<Sexpr> nodes = new ArrayList<>();
        final Predicate<Sexpr> visitor1 = x ->
        {
            nodes.add(x);
            return false;
        };
        assertFalse(tree.preorder(visitor1));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(12, nodes.size());
        assertEquals(N01, nodes.get(0));
        assertEquals(N02, nodes.get(1));
        assertEquals(N03, nodes.get(2));
        assertEquals(N04, nodes.get(3));
        assertEquals(N05, nodes.get(4));
        assertEquals(N06, nodes.get(5));
        assertEquals(N07, nodes.get(6));
        assertEquals(N08, nodes.get(7));
        assertEquals(N09, nodes.get(8));
        assertEquals(N10, nodes.get(9));
        assertEquals(N11, nodes.get(10));
        assertEquals(N12, nodes.get(11));

        /**
         * Perform a partial tree transversal and record the visitation order.
         */
        nodes.clear();
        final Predicate<Sexpr> visitor2 = x ->
        {
            nodes.add(x);
            return x.equals(N07);
        };
        assertTrue(tree.preorder(visitor2));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(7, nodes.size());
        assertEquals(N01, nodes.get(0));
        assertEquals(N02, nodes.get(1));
        assertEquals(N03, nodes.get(2));
        assertEquals(N04, nodes.get(3));
        assertEquals(N05, nodes.get(4));
        assertEquals(N06, nodes.get(5));
        assertEquals(N07, nodes.get(6));
    }

    /**
     * Test: 20170624160310474603
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
    public void test20170624160310474603 ()
    {
        System.out.println("Test: 20170624160310474603");

        /**
         * Create a tree based on an image from Wikipedia.
         * preorder is equivalent to DFS.
         */
        // https://en.wikipedia.org/wiki/Tree_traversal#/media/File:Sorted_binary_tree_postorder.svg
        final SAtom A = new SAtom("A");
        final SAtom C = new SAtom("C");
        final SAtom E = new SAtom("E");
        final SAtom H = new SAtom("H");
        final SList D = SList.of(C, E);
        final SList B = SList.of(A, D);
        final SList I = SList.of(H);
        final SList G = SList.of(I);
        final SList F = SList.of(B, G);
        final SList tree = F;

        /**
         * Perform a full tree transversal and record the visitation order.
         */
        final List<Sexpr> nodes = new ArrayList<>();
        final Predicate<Sexpr> visitor1 = x ->
        {
            nodes.add(x);
            return false;
        };
        assertFalse(tree.postorder(visitor1));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(9, nodes.size());
        assertEquals(A, nodes.get(0));
        assertEquals(C, nodes.get(1));
        assertEquals(E, nodes.get(2));
        assertEquals(D, nodes.get(3));
        assertEquals(B, nodes.get(4));
        assertEquals(H, nodes.get(5));
        assertEquals(I, nodes.get(6));
        assertEquals(G, nodes.get(7));
        assertEquals(F, nodes.get(8));

        /**
         * Perform a partial tree transversal and record the visitation order.
         */
        nodes.clear();
        final Predicate<Sexpr> visitor2 = x ->
        {
            nodes.add(x);
            return x.equals(B);
        };
        assertTrue(tree.postorder(visitor2));

        /**
         * Verify that the visitation order was correct.
         */
        assertEquals(5, nodes.size());
        assertEquals(A, nodes.get(0));
        assertEquals(C, nodes.get(1));
        assertEquals(E, nodes.get(2));
        assertEquals(D, nodes.get(3));
        assertEquals(B, nodes.get(4));
    }

    /**
     * Test: 20170624212934872671
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
    public void test20170624212934872671 ()
    {
        System.out.println("Test: 20170624212934872671");

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        SList list1;
        Object list2;

        /**
         * Case: same object
         */
        list1 = SList.of(X, Y, Z);
        list2 = list1;
        assertTrue(list1.equals(list2));

        /**
         * Case: null
         */
        list1 = SList.of(X, Y, Z);
        list2 = null;
        assertFalse(list1.equals(list2));

        /**
         * Case: different type
         * Note: The equals method is optimized to check the hash first.
         * If the hash differs, the type-check will not occur.
         */
        list1 = SList.of(X, Y, Z);
        final int hash = list1.hashCode();
        list2 = new Object()
        {
            @Override
            public boolean equals (Object obj)
            {
                return true;
            }

            @Override
            public int hashCode ()
            {
                return hash;
            }
        };
        assertFalse(list1.equals(list2));

        // Case: different values
        list1 = SList.of(X, Y);
        list2 = SList.of(Y, X);
        assertFalse(list1.equals(list2));

        // Case: same values
        list1 = SList.of(X, Y, Z);
        list2 = SList.of(X, Y, Z);
        assertTrue(list1.equals(list2));
    }

    /**
     * Test: 20170624214936561260
     *
     * <p>
     * Method: <code>hashCode</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624214936561260 ()
    {
        System.out.println("Test: 20170624214936561260");

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        /**
         * This is the expected hash-code,
         * per the List interface contract.
         */
        final int expected = 31 * X.hashCode() + 31 * Y.hashCode() + 31 * Z.hashCode();

        final int actual = SList.of(X, Y, Z).hashCode();

        assertEquals(expected, actual);
    }

    /**
     * Test: 20170624220535323107
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
    public void test20170624220535323107 ()
    {
        System.out.println("Test: 20170624220535323107");

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        assertEquals("()", SList.of().toString());
        assertEquals("(X)", SList.of(X).toString());
        assertEquals("(X Y)", SList.of(X, Y).toString());
        assertEquals("(X Y Z)", SList.of(X, Y, Z).toString());
    }

    /**
     * Test: 20170624220836295508
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
    public void test20170624220836295508 ()
    {
        System.out.println("Test: 20170624220836295508");

        final SList tree = SList.parse("N/A", "(1 2) (3 (4 5) 6)");

        assertEquals("((1 2) (3 (4 5) 6))", tree.toString());

        assertEquals(10, tree.treeSize());
    }

    /**
     * Test: 20170624220836295583
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
    public void test20170624220836295583 ()
    {
        System.out.println("Test: 20170624220836295583");

        final SList tree = SList.parse("N/A", "(1 2) (3 (4 5) 6)");

        assertEquals("((1 2) (3 (4 5) 6))", tree.toString());

        assertEquals(6, tree.treeLeafCount());
    }

    /**
     * Test: 20170624220836295609
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
    public void test20170624220836295609 ()
    {
        System.out.println("Test: 20170624220836295609");

        final SList tree = SList.parse("N/A", "(1 2) (3 (4 5) 6)");

        assertEquals("((1 2) (3 (4 5) 6))", tree.toString());

        assertEquals(4, tree.treeHeight());
    }

    /**
     * Test: 20170624224035232548
     *
     * <p>
     * Method: <code>of(Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232548 ()
    {
        System.out.println("Test: 20170624224035232548");

        final SourceLocation location = SourceLocation.DEFAULT;

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.of();
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.of(X);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.of(X, Y);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.of(X, Y, Z);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224035232572
     *
     * <p>
     * Method: <code>of(SourceLocation, Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232572 ()
    {
        System.out.println("Test: 20170624224035232572");

        final SourceLocation location = new SourceLocation("Pluto", 7, 9);

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.of(location);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.of(location, X);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.of(location, X, Y);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.of(location, X, Y, Z);
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224035232596
     *
     * <p>
     * Method: <code>copyOf(Iterable)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232596 ()
    {
        System.out.println("Test: 20170624224035232596");

        final SourceLocation location = SourceLocation.DEFAULT;

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.copyOf(new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.copyOf(new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.copyOf(new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.copyOf(new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224035232620
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterable)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232620 ()
    {
        System.out.println("Test: 20170624224035232620");

        final SourceLocation location = new SourceLocation("Pluto", 7, 9);

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.copyOf(location, new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.copyOf(location, new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.copyOf(location, new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.copyOf(location, new LinkedList<>(expected));
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224035232639
     *
     * <p>
     * Method: <code>copyOf(Stream)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232639 ()
    {
        System.out.println("Test: 20170624224035232639");

        final SourceLocation location = SourceLocation.DEFAULT;

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.copyOf(new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.copyOf(new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.copyOf(new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.copyOf(new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224035232658
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Stream)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232658 ()
    {
        System.out.println("Test: 20170624224035232658");

        final SourceLocation location = new SourceLocation("Pluto", 7, 9);

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.copyOf(location, new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.copyOf(location, new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.copyOf(location, new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.copyOf(location, new LinkedList<>(expected).stream());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224035232678
     *
     * <p>
     * Method: <code>copyOf(Iterator)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224035232678 ()
    {
        System.out.println("Test: 20170624224035232678");

        final SourceLocation location = SourceLocation.DEFAULT;

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.copyOf(new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.copyOf(new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.copyOf(new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.copyOf(new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624224408147298
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterator)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624224408147298 ()
    {
        System.out.println("Test: 20170624224408147298");

        final SourceLocation location = new SourceLocation("Pluto", 7, 9);

        final SAtom X = new SAtom("X");
        final SAtom Y = new SAtom("Y");
        final SAtom Z = new SAtom("Z");

        List<Sexpr> expected;
        SList actual;

        // Size = 0
        expected = Arrays.asList();
        actual = SList.copyOf(location, new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(1, actual.treeHeight());
        assertEquals(1, actual.treeSize());
        assertEquals(0, actual.treeLeafCount());

        // Size = 1
        expected = Arrays.asList(X);
        actual = SList.copyOf(location, new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(2, actual.treeSize());
        assertEquals(1, actual.treeLeafCount());

        // Size = 2
        expected = Arrays.asList(X, Y);
        actual = SList.copyOf(location, new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(3, actual.treeSize());
        assertEquals(2, actual.treeLeafCount());

        // Size = 3
        expected = Arrays.asList(X, Y, Z);
        actual = SList.copyOf(location, new LinkedList<>(expected).iterator());
        assertEquals(expected, actual);
        assertEquals(location, actual.location());
        assertEquals(2, actual.treeHeight());
        assertEquals(4, actual.treeSize());
        assertEquals(3, actual.treeLeafCount());
    }

    /**
     * Test: 20170624230706505708
     *
     * <p>
     * Method: <code>of(Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: null array
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505708 ()
    {
        System.out.println("Test: 20170624230706505708");

        SList.of((Sexpr[]) null);
    }

    /**
     * Test: 20170624230706505836
     *
     * <p>
     * Method: <code>of(Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505836 ()
    {
        System.out.println("Test: 20170624230706505836");

        SList.of((Sexpr) null);
    }

    /**
     * Test: 20170624230706505869
     *
     * <p>
     * Method: <code>of(SourcLocation, Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: null location
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505869 ()
    {
        System.out.println("Test: 20170624230706505869");

        final SourceLocation location = null;
        SList.of(location);
    }

    /**
     * Test: 20170624230706505899
     *
     * <p>
     * Method: <code>of(SourceLocation, Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: null array
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505899 ()
    {
        System.out.println("Test: 20170624230706505899");

        SList.of(SourceLocation.DEFAULT, (Sexpr[]) null);
    }

    /**
     * Test: 20170624230706505930
     *
     * <p>
     * Method: <code>of(SourceLocation, Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505930 ()
    {
        System.out.println("Test: 20170624230706505930");

        SList.of(SourceLocation.DEFAULT, (Sexpr) null);
    }

    /**
     * Test: 20170624230706505957
     *
     * <p>
     * Method: <code>copyOf(Iterable)</code>
     * </p>
     *
     * <p>
     * Case: null iterable
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505957 ()
    {
        System.out.println("Test: 20170624230706505957");

        SList.copyOf((Iterable) null);
    }

    /**
     * Test: 20170624230706505983
     *
     * <p>
     * Method: <code>copyOf(Iterable)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706505983 ()
    {
        System.out.println("Test: 20170624230706505983");

        SList.copyOf((Iterable) Arrays.asList((Sexpr) null));
    }

    /**
     * Test: 20170624230706506008
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterable)</code>
     * </p>
     *
     * <p>
     * Case: null location
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506008 ()
    {
        System.out.println("Test: 20170624230706506008");

        final SourceLocation location = null;
        SList.copyOf(location, Collections.emptyList());
    }

    /**
     * Test: 20170624230706506031
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterable)</code>
     * </p>
     *
     * <p>
     * Case: null iterable
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506031 ()
    {
        System.out.println("Test: 20170624230706506031");

        SList.copyOf(SourceLocation.DEFAULT, (Iterable) null);
    }

    /**
     * Test: 20170624230706506051
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterable)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506051 ()
    {
        System.out.println("Test: 20170624230706506051");

        SList.copyOf(SourceLocation.DEFAULT, (Iterable) Arrays.asList((Sexpr) null));
    }

    /**
     * Test: 20170624230706506071
     *
     * <p>
     * Method: <code>copyOf(Stream)</code>
     * </p>
     *
     * <p>
     * Case: null stream
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506071 ()
    {
        System.out.println("Test: 20170624230706506071");

        SList.copyOf((Stream) null);
    }

    /**
     * Test: 20170624230706506091
     *
     * <p>
     * Method: <code>copyOf(Stream)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506091 ()
    {
        System.out.println("Test: 20170624230706506091");

        SList.copyOf(Arrays.asList((Sexpr) null).stream());
    }

    /**
     * Test: 20170624230706506112
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Stream)</code>
     * </p>
     *
     * <p>
     * Case: null location
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506112 ()
    {
        System.out.println("Test: 20170624230706506112");

        final SourceLocation location = null;
        SList.copyOf(location, (Stream) Collections.emptyList().stream());
    }

    /**
     * Test: 20170624230706506138
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Stream)</code>
     * </p>
     *
     * <p>
     * Case: null stream
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506138 ()
    {
        System.out.println("Test: 20170624230706506138");

        SList.copyOf(SourceLocation.DEFAULT, (Stream) null);
    }

    /**
     * Test: 20170624230706506159
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Stream)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506159 ()
    {
        System.out.println("Test: 20170624230706506159");

        SList.copyOf(SourceLocation.DEFAULT, Arrays.asList((Sexpr) null).stream());
    }

    /**
     * Test: 20170624230706506181
     *
     * <p>
     * Method: <code>copyOf(Iterator)</code>
     * </p>
     *
     * <p>
     * Case: null iterator
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506181 ()
    {
        System.out.println("Test: 20170624230706506181");

        SList.copyOf((Iterator) null);
    }

    /**
     * Test: 20170624230706506204
     *
     * <p>
     * Method: <code>copyOf(Iterator)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506204 ()
    {
        System.out.println("Test: 20170624230706506204");

        SList.copyOf(Arrays.asList((Sexpr) null).iterator());
    }

    /**
     * Test: 20170624230706506225
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterator)</code>
     * </p>
     *
     * <p>
     * Case: null location
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506225 ()
    {
        System.out.println("Test: 20170624230706506225");

        final SourceLocation location = null;
        SList.copyOf(location, (Iterator) Collections.emptyList().iterator());
    }

    /**
     * Test: 20170624230706506244
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterator)</code>
     * </p>
     *
     * <p>
     * Case: null iterator
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506244 ()
    {
        System.out.println("Test: 20170624230706506244");

        SList.copyOf(SourceLocation.DEFAULT, (Iterator) null);
    }

    /**
     * Test: 20170624230706506262
     *
     * <p>
     * Method: <code>copyOf(SourceLocation, Iterator)</code>
     * </p>
     *
     * <p>
     * Case: null element
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170624230706506262 ()
    {
        System.out.println("Test: 20170624230706506262");

        SList.copyOf(SourceLocation.DEFAULT, (Iterator) Arrays.asList((Sexpr) null).iterator());
    }

    /**
     * Test: 20170624235918686534
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624235918686534 ()
    {
        System.out.println("Test: 20170624235918686534");

        final SourceLocation location = new SourceLocation("Europe", 3, 5);

        final Map<Sexpr, Sexpr> map = new LinkedHashMap<>();
        map.put(new SAtom("Alicia"), new SAtom("Vikander"));
        map.put(new SAtom("Emma"), new SAtom("Watson"));
        map.put(new SAtom("Kate"), new SAtom("Beckinsale"));

        final String expected = "((Alicia Vikander) (Emma Watson) (Kate Beckinsale))";

        final SList actual = SList.fromMap(location, map);

        assertEquals(expected, actual.toString());

        assertEquals(location, actual.location());
        assertEquals(location, actual.get(0).location());
        assertEquals(location, actual.get(1).location());
        assertEquals(location, actual.get(2).location());

        assertEquals(map, actual.asMap().get());
    }

    /**
     * Test: 20170624235918686595
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map, Sexpr)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624235918686595 ()
    {
        System.out.println("Test: 20170624235918686595");

        final SourceLocation location = new SourceLocation("Europe", 3, 5);

        final Map<Sexpr, Sexpr> map = new LinkedHashMap<>();
        map.put(new SAtom("Alicia"), new SAtom("Vikander"));
        map.put(new SAtom("Emma"), new SAtom("Watson"));
        map.put(new SAtom("Kate"), new SAtom("Beckinsale"));

        final String expected = "((Alicia => Vikander) (Emma => Watson) (Kate => Beckinsale))";

        final SList actual = SList.fromMap(location, map, new SAtom("=>"));

        assertEquals(expected, actual.toString());

        assertEquals(location, actual.location());
        assertEquals(location, actual.get(0).location());
        assertEquals(location, actual.get(1).location());
        assertEquals(location, actual.get(2).location());

        assertEquals(map, actual.asMap().get());
    }

    /**
     * Test: 20170624235918686632
     *
     * <p>
     * Method: <code>asMap</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170624235918686632 ()
    {
        System.out.println("Test: 20170624235918686632");

        final Map<Sexpr, Sexpr> expected = new HashMap<>();
        SList input;

        // Case: empty entry (i.e. too short)
        // Note: parse() adds a wrapper SList.
        input = SList.parse("N/A", "()");
        assertFalse(input.asMap().isPresent());

        // Case: single part entry (i.e. too short)
        input = SList.parse("N/A", "(100)");
        assertFalse(input.asMap().isPresent());

        // Case: two part entry (i.e. key-value pair)
        input = SList.parse("N/A", "(100 200)");
        expected.clear();
        expected.put(new SAtom("100"), new SAtom("200"));
        assertEquals(expected, input.asMap().get());

        // Case: three part entry (i.e. key-separator-value pair)
        input = SList.parse("N/A", "(300 => 400)");
        expected.clear();
        expected.put(new SAtom("300"), new SAtom("400"));
        assertEquals(expected, input.asMap().get());

        // Case: multiple part entry (i.e. key-junk-value pair)
        input = SList.parse("N/A", "(500 X Y Z 600)");
        expected.clear();
        expected.put(new SAtom("500"), new SAtom("600"));
        assertEquals(expected, input.asMap().get());

        // Case: mutliple entries
        input = SList.parse("N/A", "(100 200) (300 400) (500 600)");
        expected.clear();
        expected.put(new SAtom("100"), new SAtom("200"));
        expected.put(new SAtom("300"), new SAtom("400"));
        expected.put(new SAtom("500"), new SAtom("600"));
        assertEquals(expected, input.asMap().get());

        // Case: duplicate keys
        input = SList.parse("N/A", "(100 200) (300 400) (500 600) (100 700)");
        expected.clear();
        expected.put(new SAtom("100"), new SAtom("700"));
        expected.put(new SAtom("300"), new SAtom("400"));
        expected.put(new SAtom("500"), new SAtom("600"));
        assertEquals(expected, input.asMap().get());

        // Case: non sub-list entry (i.e. 300 is not a valid entry)
        input = SList.parse("N/A", "(100 200) 300 (400 500)");
        assertFalse(input.asMap().isPresent());
    }

    /**
     * Test: 20170625003321085598
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map)</code>
     * </p>
     *
     * <p>
     * Case: null location
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085598 ()
    {
        System.out.println("Test: 20170625003321085598");

        final SourceLocation location = null;
        SList.fromMap(location, new TreeMap<>());
    }

    /**
     * Test: 20170625003321085719
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map)</code>
     * </p>
     *
     * <p>
     * Case: null map
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085719 ()
    {
        System.out.println("Test: 20170625003321085719");

        final SourceLocation location = SourceLocation.DEFAULT;
        SList.fromMap(location, (Map) null);
    }

    /**
     * Test: 20170625003321085752
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map)</code>
     * </p>
     *
     * <p>
     * Case: null key
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085752 ()
    {
        System.out.println("Test: 20170625003321085752");

        final SourceLocation location = SourceLocation.DEFAULT;
        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(null, new SAtom(2));
        SList.fromMap(location, map);
    }

    /**
     * Test: 20170625003321085782
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map)</code>
     * </p>
     *
     * <p>
     * Case: null value
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085782 ()
    {
        System.out.println("Test: 20170625003321085782");

        final SourceLocation location = SourceLocation.DEFAULT;
        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(new SAtom(1), null);
        SList.fromMap(location, map);
    }

    /**
     * Test: 20170625003321085813
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map, Sexpr)</code>
     * </p>
     *
     * <p>
     * Case: null location
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085813 ()
    {
        System.out.println("Test: 20170625003321085813");

        final SourceLocation location = null;
        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(new SAtom(1), new SAtom(2));
        SList.fromMap(location, map, new SAtom("="));
    }

    /**
     * Test: 20170625003321085838
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map, Sexpr)</code>
     * </p>
     *
     * <p>
     * Case: null map
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085838 ()
    {
        System.out.println("Test: 20170625003321085838");

        final SourceLocation location = SourceLocation.DEFAULT;
        final Map<Sexpr, Sexpr> map = null;;
        SList.fromMap(location, map, new SAtom("="));
    }

    /**
     * Test: 20170625003321085863
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map, Sexpr)</code>
     * </p>
     *
     * <p>
     * Case: null key
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085863 ()
    {
        System.out.println("Test: 20170625003321085863");

        final SourceLocation location = SourceLocation.DEFAULT;
        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(null, new SAtom(2));
        SList.fromMap(location, map, new SAtom("="));
    }

    /**
     * Test: 20170625003321085886
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map, Sexpr)</code>
     * </p>
     *
     * <p>
     * Case: null value
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085886 ()
    {
        System.out.println("Test: 20170625003321085886");

        final SourceLocation location = SourceLocation.DEFAULT;
        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(new SAtom(1), null);
        SList.fromMap(location, map, new SAtom("="));
    }

    /**
     * Test: 20170625003321085909
     *
     * <p>
     * Method: <code>fromMap(SourceLocation, Map, Sexpr)</code>
     * </p>
     *
     * <p>
     * Case: null separator
     * </p>
     */
    @Test (expected = NullPointerException.class)
    public void test20170625003321085909 ()
    {
        System.out.println("Test: 20170625003321085909");

        final SourceLocation location = SourceLocation.DEFAULT;
        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(new SAtom(1), new SAtom(2));
        SList.fromMap(location, map, null);
    }

    /**
     * Test: 20170625004837569787
     *
     * <p>
     * Method: <code>parse</code>
     * </p>
     *
     * <p>
     * Case: basic functionality
     * </p>
     */
    @Test
    public void test20170625004837569787 ()
    {
        System.out.println("Test: 20170625004837569787");

        final SList tree = SList.parse("Venus", "(A B)\n(X Y)");
        assertEquals("((A B) (X Y))", tree.toString());
        final SAtom A = ((SAtom) ((SList) tree.get(0)).get(0));
        final SAtom B = ((SAtom) ((SList) tree.get(0)).get(1));
        final SAtom X = ((SAtom) ((SList) tree.get(1)).get(0));
        final SAtom Y = ((SAtom) ((SList) tree.get(1)).get(1));

        assertEquals("A", A.content());
        assertEquals("B", B.content());
        assertEquals("X", X.content());
        assertEquals("Y", Y.content());

        assertFalse(tree.dfs(x -> !x.location().source().equals("Venus")));

        assertEquals(1, A.location().line());
        assertEquals(1, B.location().line());
        assertEquals(2, X.location().line());
        assertEquals(2, Y.location().line());

        assertEquals(2, A.location().column());
        assertEquals(4, B.location().column());
        assertEquals(2, X.location().column());
        assertEquals(4, Y.location().column());
    }

    /**
     * Test: 20170625011212166422
     *
     * <p>
     * Method: <code>parse</code>
     * </p>
     *
     * <p>
     * Case: comments
     * </p>
     */
    @Test
    public void test20170625011212166422 ()
    {
        System.out.println("Test: 20170625011212166422");

        assertEquals("()", SList.parse("Venus", "").toString());
        assertEquals("()", SList.parse("Venus", "(* *)").toString());
        assertEquals("()", SList.parse("Venus", "(* (* *) *)").toString());
        assertEquals("()", SList.parse("Venus", "(* (* (* *) *) *)").toString());
    }

    /**
     * Test: 20170625011212166489
     *
     * <p>
     * Method: <code>parse</code>
     * </p>
     *
     * <p>
     * Case: strings and escape sequences
     * </p>
     */
    @Test
    public void test20170625011212166489 ()
    {
        System.out.println("Test: 20170625011212166489");

        /**
         * Empty String Literals.
         */
        assertEquals("('')", SList.parse("Venus", "''"));
        assertEquals("('')", SList.parse("Venus", "\"\""));
        assertEquals("('')", SList.parse("Venus", ""));
        assertEquals("('')", SList.parse("Venus", "@''"));
        assertEquals("('')", SList.parse("Venus", "@\"\""));

        /**
         * Do escape sequences cause problems in string literals?
         */
        assertEquals("('\\'')", SList.parse("Venus", "'\\''"));
        assertEquals("('\\\"')", SList.parse("Venus", "\"\\\"\""));
        assertEquals("('\\'')", SList.parse("Venus", "@'\\''"));
        assertEquals("('\\\"')", SList.parse("Venus", "@\"\\\"\""));

        /**
         * Escape Sequences.
         */
        assertEquals("\u1234\b\t\n\f\r\\\'\"", ((SAtom) SList.parse("Venus", "'''\\u1234\\b\\t\\n\\f\\r\\\\\\'\\\"'''").get(0)).content());
        assertEquals("\u1234\b\t\n\f\r\\\'\"", ((SAtom) SList.parse("Venus", "\"\\u1234\\b\\t\\n\\f\\r\\\\\\'\\\"\"").get(0)).content());
    }

    /**
     * Test: 20170625011212166514
     *
     * <p>
     * Method: <code>parse</code>
     * </p>
     *
     * <p>
     * Case: syntax errors
     * </p>
     */
    @Test
    public void test20170625011212166514 ()
    {
        System.out.println("Test: 20170625011212166514");

        final Parser p = new Parser("test20170625011212166514");

        try
        {
            p.parse("(");
            fail();
        }
        catch (IllegalArgumentException ex)
        {
            final String message = ex.getMessage();
            assertTrue(message.matches("Parsing Failed At Line: [0-9]+, Column: [0-9]+, Source: test20170625011212166514"));
        }
    }

    /**
     * Test: 20170625013405187918
     *
     * <p>
     * Method: <code>toList</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625013405187918 ()
    {
        System.out.println("Test: 20170625013405187918");

        final SList original = SList.of();
        final SList result = original.toList();
        assertTrue(original == result); // identity
    }

    /**
     * Test: 20170625013405187994
     *
     * <p>
     * Method: <code>toAtom</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test (expected = ClassCastException.class)
    public void test20170625013405187994 ()
    {
        System.out.println("Test: 20170625013405187994");

        SList.of().toAtom();
    }

    /**
     * Test: 20170625032925499124
     *
     * <p>
     * Method: <code>mutator</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170625032925499124 ()
    {
        System.out.println("Test: 20170625032925499124");

        assertTrue(SList.of().mutator() instanceof TreeMutator);
    }
}
