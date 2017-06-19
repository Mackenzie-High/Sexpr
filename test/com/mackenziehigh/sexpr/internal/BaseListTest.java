package com.mackenziehigh.sexpr.internal;

import java.util.Arrays;
import static org.junit.Assert.*;
import org.junit.Test;

public class BaseListTest
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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list = new BaseList(Arrays.asList(e1, e2, e3));

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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list = new BaseList(Arrays.asList(e1, e2, e3));

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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list = new BaseList(Arrays.asList(e1, e2, e3));

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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list = new BaseList(Arrays.asList(e1, e2, e3));

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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list0 = new BaseList(Arrays.asList());
        final BaseList list1 = new BaseList(Arrays.asList(e1));
        final BaseList list2 = new BaseList(Arrays.asList(e1, e2));
        final BaseList list3 = new BaseList(Arrays.asList(e1, e2, e3));
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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list0 = new BaseList(Arrays.asList());
        final BaseList list1 = new BaseList(Arrays.asList(e1));
        final BaseList list2 = new BaseList(Arrays.asList(e1, e2));
        final BaseList list3 = new BaseList(Arrays.asList(e1, e2, e3));
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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list0 = new BaseList(Arrays.asList());
        final BaseList list1 = new BaseList(Arrays.asList(e1));
        final BaseList list2 = new BaseList(Arrays.asList(e1, e2));
        final BaseList list3 = new BaseList(Arrays.asList(e1, e2, e3));
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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list0 = new BaseList(Arrays.asList());
        final BaseList list1 = new BaseList(Arrays.asList(e1));
        final BaseList list2 = new BaseList(Arrays.asList(e1, e2));
        final BaseList list3 = new BaseList(Arrays.asList(e1, e2, e3));
        assertTrue(list0.tail().isEmpty());
        assertTrue(list1.tail().isEmpty());
        assertEquals(e2, list2.tail().first());
        assertEquals(e2, list2.tail().last());
        assertEquals(1, list2.size());
        assertEquals(e2, list3.tail().first());
        assertEquals(e3, list3.tail().last());
        assertEquals(2, list3.size());
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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list = new BaseList(Arrays.asList(e1, e2, e3));

        assertFalse(list.isSAtom());
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

        final BaseAtom e1 = new BaseAtom("100");
        final BaseAtom e2 = new BaseAtom("200");
        final BaseAtom e3 = new BaseAtom("300");
        final BaseList list = new BaseList(Arrays.asList(e1, e2, e3));

        assertTrue(list.isSList());
    }
}
