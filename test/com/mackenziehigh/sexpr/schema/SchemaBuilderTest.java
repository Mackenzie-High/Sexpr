package com.mackenziehigh.sexpr.schema;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class SchemaBuilderTest
{
    /**
     * Test: 20170625075521926237
     *
     * <p>
     * Case:
     * </p>
     */
    @Test
    public void test20170625075521926237 ()
    {
        System.out.println("Test: 20170625075521926237");

        final SchemaBuilder g = SchemaBuilder.create();

        g.define("rZ").leaf().requireBoolean();
        g.define("rC").leaf().requireChar();
        g.define("rS").leaf().requireShort();
        g.define("rI").leaf().requireInt();
        g.define("rJ").leaf().requireLong();
        g.define("rF").leaf().requireFloat();
        g.define("rD").leaf().requireDouble();
        g.define("rX").leaf().requireClass();

        fail();
    }
}
