package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Unit Test.
 */
public class SchemaBuilderTest
{

    private Optional<MatchNode> parse (final String schema,
                                       final String expression)
    {
        /**
         * Remember, parse(*) adds an implicit pair of parentheses.
         * Remove the extra parentheses for the sake of simplicity.
         */
        final Sexpr tree = SList.parse(SchemaBuilderTest.class.getName(), expression).toList().get(0);

        /**
         * Parse the schema.
         */
        final SchemaParser parser = new SchemaParser();
        final Schema grammar = parser.parse(SchemaBuilderTest.class.getName(), schema);

        /**
         * Attempt to match the symbolic-expression using the schema.
         */
        final AtomicReference<Optional<MatchNode>> ref = new AtomicReference<>();
        grammar.match(tree, x -> ref.set(Optional.of(x)), x -> ref.set(Optional.empty()));

        return ref.get();
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
        fail();
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

        assertTrue(parse("root = '123';", "123").isPresent());
        assertTrue(parse("root = \"123\";", "123").isPresent());
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

        assertTrue(parse("root = matches 'X[0-9]*Y';", "XY").isPresent());
        assertTrue(parse("root = matches 'X[0-9]*Y';", "X1Y").isPresent());
        assertTrue(parse("root = matches 'X[0-9]*Y';", "X12Y").isPresent());
        assertTrue(parse("root = matches 'X[0-9]*Y';", "X123Y").isPresent());

        assertFalse(parse("root = matches 'X[0-9]+Y';", "XYZ").isPresent());
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

        assertFalse(parse("root = range 100 < X < 200;", "99").isPresent());
        assertFalse(parse("root = range 100 < X < 200;", "100").isPresent());
        assertTrue(parse(" root = range 100 < X < 200;", "101").isPresent());
        assertTrue(parse(" root = range 100 < X < 200;", "199").isPresent());
        assertFalse(parse("root = range 100 < X < 200;", "200").isPresent());
        assertFalse(parse("root = range 100 < X < 200;", "201").isPresent());

        assertFalse(parse("root = range 100 < X <= 200;", "99").isPresent());
        assertFalse(parse("root = range 100 < X <= 200;", "100").isPresent());
        assertTrue(parse(" root = range 100 < X <= 200;", "101").isPresent());
        assertTrue(parse(" root = range 100 < X <= 200;", "199").isPresent());
        assertTrue(parse(" root = range 100 < X <= 200;", "200").isPresent());
        assertFalse(parse("root = range 100 < X <= 200;", "201").isPresent());

        assertFalse(parse("root = range 100 <= X < 200;", "99").isPresent());
        assertTrue(parse(" root = range 100 <= X < 200;", "100").isPresent());
        assertTrue(parse(" root = range 100 <= X < 200;", "101").isPresent());
        assertTrue(parse(" root = range 100 <= X < 200;", "199").isPresent());
        assertFalse(parse("root = range 100 <= X < 200;", "200").isPresent());
        assertFalse(parse("root = range 100 <= X < 200;", "201").isPresent());

        assertFalse(parse("root = range 100 <= X <= 200;", "99").isPresent());
        assertTrue(parse(" root = range 100 <= X <= 200;", "100").isPresent());
        assertTrue(parse(" root = range 100 <= X <= 200;", "101").isPresent());
        assertTrue(parse(" root = range 100 <= X <= 200;", "199").isPresent());
        assertTrue(parse(" root = range 100 <= X <= 200;", "200").isPresent());
        assertFalse(parse("root = range 100 <= X <= 200;", "201").isPresent());
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

        assertTrue(parse("root = matches 'X[0-9]+.' & matches '.[0-9]+Y';", "X123Y").isPresent());

        assertFalse(parse("root = matches 'X[0-9]+.' & matches '.[0-9]+Y';", "X123Z").isPresent());
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

        assertTrue(parse("root = '12' / '34' / '56';", "12").isPresent());
        assertTrue(parse("root = '12' / '34' / '56';", "34").isPresent());
        assertTrue(parse("root = '12' / '34' / '56';", "56").isPresent());

        assertFalse(parse("root = '12' / '34' / '56';", "78").isPresent());
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

        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "0").isPresent());
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "1").isPresent());
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "2").isPresent());
        assertFalse(parse("root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "3").isPresent());
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "4").isPresent());
        assertFalse(parse("root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "5").isPresent());
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "6").isPresent());
        assertFalse(parse("root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "7").isPresent());
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "8").isPresent());
        assertTrue(parse(" root = matches '[0-9]' & ! '3' & ! '5' & ! '7';", "9").isPresent());

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

        assertTrue(parse("root = [];", "()").isPresent());
        assertTrue(parse("root = [ '12' ];", "(12)").isPresent());
        assertTrue(parse("root = [ '12' , '34' ];", "(12 34)").isPresent());
        assertTrue(parse("root = [ '12' , '34' , '56' ];", "(12 34 56)").isPresent());

        assertFalse(parse("root = [ '12' , '34' ];", "(12 34 56)").isPresent());
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

        assertTrue(parse(" root = [ 'X' , 'Y' ? , 'Z' ];", "(X Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' ? , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' ? , 'Z' ];", "(X Y Y Z)").isPresent());
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

        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' * , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' * , 'Z' ];", "(X M Z)").isPresent());
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

        assertFalse(parse("root = [ 'X' , 'Y' + , 'Z' ];", "(X Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' + , 'Z' ];", "(X Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' + , 'Z' ];", "(X Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' + , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' + , 'Z' ];", "(X M Z)").isPresent());
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

        assertTrue(parse(" root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 0 } , 'Z' ];", "(X M Z)").isPresent());

        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 1 } , 'Z' ];", "(X M Z)").isPresent());

        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 2 } , 'Z' ];", "(X M Z)").isPresent());

        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 } , 'Z' ];", "(X M Z)").isPresent());

        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 4 } , 'Z' ];", "(X M Z)").isPresent());
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
        assertTrue(parse(" root = [ 'X' , 'Y' { 0 , 0 } , 'Y' , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 0 , 0 } , 'Y' , 'Z' ];", "(X Y Y Z)").isPresent());

        /**
         * The match must stop at the maximum.
         */
        assertFalse(parse("root = [ 'X' , 'Y' { 0 , 1 } , 'Y' , 'Z' ];", "(X Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 0 , 1 } , 'Y' , 'Z' ];", "(X Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 0 , 1 } , 'Y' , 'Z' ];", "(X Y Y Y Z)").isPresent());

        /**
         * The minimum must be reached.
         */
        assertFalse(parse("root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 1 , 2 } , 'Y' , 'Z' ];", "(X Y Y Y Y Z)").isPresent());

        /**
         * Wider range.
         */
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Z)").isPresent());
        assertTrue(parse(" root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Y Y Z)").isPresent());
        assertFalse(parse("root = [ 'X' , 'Y' { 3 , 7 } , 'Z' ];", "(X Y Y Y Y Y Y Y Y Y Z)").isPresent());
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
        assertTrue(parse("root = 'X';", "X").isPresent());
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
    @Test (expected = IllegalArgumentException.class)
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
}
