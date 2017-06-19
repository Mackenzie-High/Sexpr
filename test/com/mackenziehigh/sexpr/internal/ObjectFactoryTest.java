package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SexprFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class ObjectFactoryTest
{
    private final ObjectFactory factory = new ObjectFactory();

    /**
     * Test: 20170617093329727186
     *
     * <p>
     * Method: <code>from(boolean)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727186 ()
    {
        System.out.println("Test: 20170617093329727186");

        assertTrue(factory.from(true) instanceof BaseAtom);
        assertTrue(factory.from(true).isSAtom());
        assertEquals(true, factory.from(true).asBoolean().get());
        assertEquals(false, factory.from(false).asBoolean().get());
    }

    /**
     * Test: 20170617093329727253
     *
     * <p>
     * Method: <code>from(long)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727253 ()
    {
        System.out.println("Test: 20170617093329727253");

        assertTrue(factory.from(123L) instanceof BaseAtom);
        assertTrue(factory.from(123L).isSAtom());
        assertEquals(123L, (long) factory.from(123L).asInteger().get());
    }

    /**
     * Test: 20170617093329727278
     *
     * <p>
     * Method: <code>from(double)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727278 ()
    {
        System.out.println("Test: 20170617093329727278");

        assertTrue(factory.from(12.345) instanceof BaseAtom);
        assertTrue(factory.from(12.345).isSAtom());
        assertEquals(12.345, factory.from(12.345).asFloat().get(), 0.1);
    }

    /**
     * Test: 20170617093329727300
     *
     * <p>
     * Method: <code>from(String)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727300 ()
    {
        System.out.println("Test: 20170617093329727300");

        assertTrue(factory.from("2995") instanceof BaseAtom);
        assertTrue(factory.from("2995").isSAtom());
        assertEquals("2995", factory.from("2995").content());
        assertEquals(2995L, (long) factory.from("2995").asInteger().get());
        assertEquals(2995.0, factory.from("2995").asFloat().get(), 0.01);

        assertTrue(factory.from("1") instanceof BaseAtom);
        assertTrue(factory.from("1").isSAtom());
        assertEquals("1", factory.from("1").content());
        assertEquals(1L, (long) factory.from("1").asInteger().get());
        assertEquals(1.0, factory.from("1").asFloat().get(), 0.01);
        assertEquals(true, factory.from("1").asBoolean().get());

        assertTrue(factory.from("0") instanceof BaseAtom);
        assertTrue(factory.from("0").isSAtom());
        assertEquals("0", factory.from("0").content());
        assertEquals(0L, (long) factory.from("0").asInteger().get());
        assertEquals(0.0, factory.from("0").asFloat().get(), 0.01);
        assertEquals(false, factory.from("0").asBoolean().get());
    }

    /**
     * Test: 20170617093329727321
     *
     * <p>
     * Method: <code>from(Class)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727321 ()
    {
        System.out.println("Test: 20170617093329727321");

        assertTrue(factory.from(String.class) instanceof BaseAtom);
        assertTrue(factory.from(String.class).isSAtom());
        assertEquals(String.class, factory.from(String.class).asClass().get());
    }

    /**
     * Test: 20170617093329727339
     *
     * <p>
     * Method: <code>from(Sexpr...)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727339 ()
    {
        System.out.println("Test: 20170617093329727339");

        final Sexpr e1 = factory.from(123);
        final Sexpr e2 = factory.from(456);
        final Sexpr e3 = factory.from(789);

        assertTrue(factory.from(e1, e2, e3) instanceof BaseList);
        assertTrue(factory.from(e1, e2, e3).isSList());
        assertEquals(3, factory.from(e1, e2, e3).size());
        assertEquals(e1, factory.from(e1, e2, e3).get(0));
        assertEquals(e2, factory.from(e1, e2, e3).get(1));
        assertEquals(e3, factory.from(e1, e2, e3).get(2));
    }

    /**
     * Test: 20170617093329727360
     *
     * <p>
     * Method: <code>from(Iterable)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727360 ()
    {
        System.out.println("Test: 20170617093329727360");

        final Sexpr e1 = factory.from(123);
        final Sexpr e2 = factory.from(456);
        final Sexpr e3 = factory.from(789);

        final List<Sexpr> list = Arrays.asList(e1, e2, e3);

        assertTrue(factory.from(list) instanceof BaseList);
        assertTrue(factory.from(list).isSList());
        assertEquals(3, factory.from(list).size());
        assertEquals(e1, factory.from(list).get(0));
        assertEquals(e2, factory.from(list).get(1));
        assertEquals(e3, factory.from(list).get(2));
    }

    /**
     * Test: 20170617093329727381
     *
     * <p>
     * Method: <code>from(Map)</code>
     * </p>
     *
     * <p>
     * Case: normal
     * </p>
     */
    @Test
    public void test20170617093329727381 ()
    {
        System.out.println("Test: 20170617093329727381");

        final Sexpr e1 = factory.from(12);
        final Sexpr e2 = factory.from(34);
        final Sexpr e3 = factory.from(56);
        final Sexpr e4 = factory.from(78);

        final Map<Sexpr, Sexpr> map = new HashMap<>();
        map.put(e1, e2);
        map.put(e3, e4);

        assertTrue(factory.from(map) instanceof BaseList);
        assertTrue(factory.from(map).isSList());
        assertEquals(2, factory.from(map).size());
        assertEquals("((12 34) (56 78))", factory.from(map).toString());
    }

    /**
     * Test: 20170617093329727401
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
    public void test20170617093329727401 ()
    {
        System.out.println("Test: 20170617093329727401");

        final SList list = factory.parse("test20170617093329727401", "(12 34)");

        assertTrue(list instanceof BaseList);
        assertEquals("test20170617093329727401", list.location().source());
        assertEquals(1, list.location().line());
        assertEquals(1, list.location().column());
    }

    /**
     * Test: 20170617093329727419
     *
     * <p>
     * Case: Singleton Exists
     * </p>
     */
    @Test
    public void test20170617093329727419 ()
    {
        System.out.println("Test: 20170617093329727419");

        assertTrue(SexprFactory.instance instanceof ObjectFactory);
    }
}
