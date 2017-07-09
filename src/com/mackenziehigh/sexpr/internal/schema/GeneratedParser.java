package com.mackenziehigh.sexpr.internal.schema;

import high.mackenzie.snowflake.Grammar;
import high.mackenzie.snowflake.GrammarBuilder;
import high.mackenzie.snowflake.IParser;
import high.mackenzie.snowflake.ParserOutput;

/**
 * This class was auto-generated using the Snowflake parser-generator.
 *
 * <p>
 * Generated On: Sat Jul 08 20:03:48 EDT 2017</p>
 */
final class GeneratedParser
        implements IParser
{

    private static final Grammar grammar = grammar();

    /**
     * This method constructs the grammar object.
     */
    private static Grammar grammar ()
    {
        final GrammarBuilder g = new GrammarBuilder();

        // Character Classes
        g.range("@class0", (char) 59, (char) 59);
        g.range("@class1", (char) 61, (char) 61);
        g.range("@class2", (char) 59, (char) 59);
        g.range("@class3", (char) 60, (char) 60);
        g.range("@class4", (char) 60, (char) 60);
        g.range("@class5", (char) 43, (char) 43);
        g.range("@class6", (char) 45, (char) 45);
        g.range("@class7", (char) 46, (char) 46);
        g.range("@class8", (char) 69, (char) 69);
        g.range("@class9", (char) 101, (char) 101);
        g.range("@class10", (char) 43, (char) 43);
        g.range("@class11", (char) 45, (char) 45);
        g.range("@class12", (char) 38, (char) 38);
        g.range("@class13", (char) 47, (char) 47);
        g.range("@class14", (char) 91, (char) 91);
        g.range("@class15", (char) 93, (char) 93);
        g.range("@class16", (char) 44, (char) 44);
        g.range("@class17", (char) 40, (char) 40);
        g.range("@class18", (char) 41, (char) 41);
        g.range("@class19", (char) 65, (char) 90);
        g.range("@class20", (char) 97, (char) 122);
        g.range("@class21", (char) 95, (char) 95);
        g.range("@class22", (char) 45, (char) 45);
        g.range("@class23", (char) 36, (char) 36);
        g.range("@class24", (char) 46, (char) 46);
        g.range("@class25", (char) 48, (char) 57);
        g.combine("@class26", "@class19", "@class20", "@class21", "@class22", "@class23", "@class24", "@class25");
        g.range("@class27", (char) 1, (char) 38);
        g.range("@class28", (char) 40, (char) 65535);
        g.combine("@class29", "@class27", "@class28");
        g.range("@class30", (char) 34, (char) 34);
        g.range("@class31", (char) 34, (char) 34);
        g.range("@class32", (char) 1, (char) 33);
        g.range("@class33", (char) 35, (char) 65535);
        g.combine("@class34", "@class32", "@class33");
        g.range("@class35", (char) 0, (char) 65535);
        g.range("@class36", (char) 48, (char) 57);
        g.combine("@class37", "@class36");
        g.range("@class38", (char) 92, (char) 92);
        g.range("@class39", (char) 98, (char) 98);
        g.range("@class40", (char) 92, (char) 92);
        g.range("@class41", (char) 116, (char) 116);
        g.range("@class42", (char) 92, (char) 92);
        g.range("@class43", (char) 110, (char) 110);
        g.range("@class44", (char) 92, (char) 92);
        g.range("@class45", (char) 102, (char) 102);
        g.range("@class46", (char) 92, (char) 92);
        g.range("@class47", (char) 114, (char) 114);
        g.range("@class48", (char) 92, (char) 92);
        g.range("@class49", (char) 92, (char) 92);
        g.range("@class50", (char) 92, (char) 92);
        g.range("@class51", (char) 92, (char) 92);
        g.range("@class52", (char) 34, (char) 34);
        g.range("@class53", (char) 117, (char) 117);
        g.range("@class54", (char) 48, (char) 57);
        g.range("@class55", (char) 65, (char) 70);
        g.combine("@class56", "@class54", "@class55");
        g.range("@class57", (char) 32, (char) 32);
        g.range("@class58", (char) 9, (char) 9);
        g.range("@class59", (char) 10, (char) 10);
        g.range("@class60", (char) 11, (char) 11);
        g.range("@class61", (char) 12, (char) 12);
        g.range("@class62", (char) 13, (char) 13);
        g.combine("@class63", "@class57", "@class58", "@class59", "@class60", "@class61", "@class62");
        g.range("@class64", (char) 10, (char) 10);
        g.range("@class65", (char) 13, (char) 13);
        g.combine("@class66", "@class64", "@class65");

        // Grammar Rules
        g.choose("@10", "@7", "@8", "@9");
        g.choose("@15", "@12", "@13", "@14");
        g.choose("@19", "@16", "@17", "@18");
        g.choose("@71", "SP", "SINGLE_COMMENT", "MULTI_COMMENT");
        g.choose("DOUBLE_STRING_ELEMENT", "ESCAPE", "DOUBLE_STRING_CHAR");
        g.choose("ESCAPE", "ESCAPE_B", "ESCAPE_T", "ESCAPE_N", "ESCAPE_F", "ESCAPE_R", "ESCAPE_SL", "ESCAPE_SQ", "ESCAPE_DQ", "ESCAPE_U");
        g.choose("MULTI_COMMENT_TEXT", "MULTI_COMMENT", "@82");
        g.choose("SINGLE_STRING_ELEMENT", "ESCAPE", "SINGLE_STRING_CHAR");
        g.choose("atom_rule", "range_rule", "regex_rule", "constant_rule", "rule_reference");
        g.choose("element_modifier", "zoo_modifier", "zom_modifier", "oom_modifier", "minmax_modifier", "exact_modifier", "one_modifier");
        g.choose("maximum_operator", "maximum_inclusive", "maximum_exclusive");
        g.choose("minimum_operator", "minimum_inclusive", "minimum_exclusive");
        g.choose("operand", "nested_rule", "atom_rule", "not_rule", "rule_reference");
        g.choose("rule", "and_rule", "or_rule", "not_rule", "sequence_rule", "predicate_rule", "atom_rule");
        g.choose("statement", "root_directive", "assignment");
        g.choose("string", "single_string", "double_string");
        g.chr("@11", "@class7");
        g.chr("@12", "@class8");
        g.chr("@13", "@class9");
        g.chr("@16", "@class10");
        g.chr("@17", "@class11");
        g.chr("@2", "@class0");
        g.chr("@24", "@class12");
        g.chr("@27", "@class13");
        g.chr("@3", "@class1");
        g.chr("@32", "@class14");
        g.chr("@34", "@class15");
        g.chr("@35", "@class16");
        g.chr("@4", "@class2");
        g.chr("@46", "@class17");
        g.chr("@47", "@class18");
        g.chr("@48", "@class26");
        g.chr("@51", "@class30");
        g.chr("@52", "@class31");
        g.chr("@53", "@class37");
        g.chr("@54", "@class38");
        g.chr("@55", "@class39");
        g.chr("@56", "@class40");
        g.chr("@57", "@class41");
        g.chr("@58", "@class42");
        g.chr("@59", "@class43");
        g.chr("@60", "@class44");
        g.chr("@61", "@class45");
        g.chr("@62", "@class46");
        g.chr("@63", "@class47");
        g.chr("@64", "@class48");
        g.chr("@65", "@class49");
        g.chr("@66", "@class50");
        g.chr("@68", "@class51");
        g.chr("@69", "@class52");
        g.chr("@7", "@class5");
        g.chr("@70", "@class53");
        g.chr("@73", "@class66");
        g.chr("@8", "@class6");
        g.chr("ANY_CHAR", "@class35");
        g.chr("DOUBLE_STRING_CHAR", "@class34");
        g.chr("HEX_DIGIT", "@class56");
        g.chr("SINGLE_STRING_CHAR", "@class29");
        g.chr("SP", "@class63");
        g.chr("maximum_exclusive", "@class4");
        g.chr("minimum_exclusive", "@class3");
        g.not("@74", "@73");
        g.not("@81", "@80");
        g.repeat("@0", "statement", 0, 2147483647);
        g.repeat("@21", "@20", 0, 1);
        g.repeat("@23", "@22", 0, 1);
        g.repeat("@26", "@25", 1, 2147483647);
        g.repeat("@29", "@28", 1, 2147483647);
        g.repeat("@33", "elements", 0, 1);
        g.repeat("@37", "@36", 0, 2147483647);
        g.repeat("@76", "@75", 0, 2147483647);
        g.repeat("@78", "MULTI_COMMENT_TEXT", 0, 2147483647);
        g.repeat("DIGITS", "@53", 1, 2147483647);
        g.repeat("DOUBLE_STRING_BODY", "DOUBLE_STRING_ELEMENT", 0, 2147483647);
        g.repeat("SINGLE_STRING_BODY", "SINGLE_STRING_ELEMENT", 0, 2147483647);
        g.repeat("WS", "@71", 0, 2147483647);
        g.repeat("name", "@48", 1, 2147483647);
        g.sequence("@20", "@15", "@19", "DIGITS");
        g.sequence("@22", "@11", "DIGITS", "@21");
        g.sequence("@25", "WS", "@24", "WS", "operand", "WS");
        g.sequence("@28", "WS", "@27", "WS", "operand", "WS");
        g.sequence("@36", "WS", "@35", "WS", "element");
        g.sequence("@75", "@74", "ANY_CHAR");
        g.sequence("@82", "@81", "ANY_CHAR");
        g.sequence("ESCAPE_B", "@54", "@55");
        g.sequence("ESCAPE_DQ", "@68", "@69");
        g.sequence("ESCAPE_F", "@60", "@61");
        g.sequence("ESCAPE_N", "@58", "@59");
        g.sequence("ESCAPE_R", "@62", "@63");
        g.sequence("ESCAPE_SL", "@64", "@65");
        g.sequence("ESCAPE_SQ", "@66", "@67");
        g.sequence("ESCAPE_T", "@56", "@57");
        g.sequence("ESCAPE_U", "@70", "HEX_DIGIT", "HEX_DIGIT", "HEX_DIGIT", "HEX_DIGIT");
        g.sequence("FLOAT", "@10", "DIGITS", "@23");
        g.sequence("MULTI_COMMENT", "@77", "@78", "@79");
        g.sequence("SINGLE_COMMENT", "@72", "@76");
        g.sequence("and_rule", "and_start", "WS", "operand", "@26");
        g.sequence("assignment", "name", "WS", "@3", "WS", "rule", "WS", "@4", "WS");
        g.sequence("constant_rule", "string", "WS");
        g.sequence("double_string", "@51", "DOUBLE_STRING_BODY", "@52", "WS");
        g.sequence("element", "operand", "WS", "element_modifier");
        g.sequence("elements", "element", "@37");
        g.sequence("exact_modifier", "@44", "WS", "maximum", "WS", "@45", "WS");
        g.sequence("float", "FLOAT", "WS");
        g.sequence("input", "WS", "@0", "WS", "END");
        g.sequence("maximum", "DIGITS");
        g.sequence("minimum", "DIGITS");
        g.sequence("minmax_modifier", "@41", "WS", "minimum", "WS", "@42", "WS", "maximum", "WS", "@43", "WS");
        g.sequence("nested_rule", "@46", "WS", "rule", "WS", "@47", "WS");
        g.sequence("not_rule", "@30", "WS", "operand", "WS");
        g.sequence("number", "float");
        g.sequence("oom_modifier", "@40", "WS");
        g.sequence("or_rule", "or_start", "WS", "operand", "@29");
        g.sequence("predicate_rule", "@31", "WS", "name", "WS");
        g.sequence("range_rule", "@6", "WS", "number", "WS", "minimum_operator", "WS", "name", "WS", "maximum_operator", "WS", "number", "WS");
        g.sequence("regex_rule", "@5", "WS", "string", "WS");
        g.sequence("root_directive", "@1", "WS", "name", "WS", "@2", "WS");
        g.sequence("rule_reference", "name", "WS");
        g.sequence("sequence_rule", "sequence_start", "WS", "@32", "WS", "@33", "WS", "@34", "WS");
        g.sequence("single_string", "@49", "SINGLE_STRING_BODY", "@50", "WS");
        g.sequence("zom_modifier", "@39", "WS");
        g.sequence("zoo_modifier", "@38", "WS");
        g.str("@1", "root");
        g.str("@14", "");
        g.str("@18", "");
        g.str("@30", "!");
        g.str("@31", "require");
        g.str("@38", "?");
        g.str("@39", "*");
        g.str("@40", "+");
        g.str("@41", "{");
        g.str("@42", ",");
        g.str("@43", "}");
        g.str("@44", "{");
        g.str("@45", "}");
        g.str("@49", "'");
        g.str("@5", "matches");
        g.str("@50", "'");
        g.str("@6", "range");
        g.str("@67", "'");
        g.str("@72", "#");
        g.str("@77", "(*");
        g.str("@79", "*)");
        g.str("@80", "*)");
        g.str("@9", "");
        g.str("and_start", "");
        g.str("maximum_inclusive", "<=");
        g.str("minimum_inclusive", "<=");
        g.str("one_modifier", "");
        g.str("or_start", "");
        g.str("sequence_start", "");

        // Specify which rule is the root of the grammar.
        g.setRoot("input");

        // Specify the number of tracing records to store concurrently.
        g.setTraceCount(1024);

        // Perform the actual construction of the grammar object.
        return g.build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParserOutput parse (final char[] input)
    {
        return grammar.newParser().parse(input);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParserOutput parse (final String input)
    {
        return parse(input.toCharArray());
    }
}
