package com.mackenziehigh.sexpr.internal.schema;

import high.mackenzie.snowflake.Grammar;
import high.mackenzie.snowflake.GrammarBuilder;
import high.mackenzie.snowflake.IParser;
import high.mackenzie.snowflake.ParserOutput;

/**
 * This class was auto-generated using the Snowflake parser-generator.
 *
 * <p>
 * Generated On: Tue Jul 04 20:20:49 EDT 2017</p>
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
        g.range("@class1", (char) 59, (char) 59);
        g.range("@class2", (char) 59, (char) 59);
        g.range("@class3", (char) 59, (char) 59);
        g.range("@class4", (char) 59, (char) 59);
        g.range("@class5", (char) 59, (char) 59);
        g.range("@class6", (char) 61, (char) 61);
        g.range("@class7", (char) 59, (char) 59);
        g.range("@class8", (char) 60, (char) 60);
        g.range("@class9", (char) 60, (char) 60);
        g.range("@class10", (char) 43, (char) 43);
        g.range("@class11", (char) 45, (char) 45);
        g.range("@class12", (char) 46, (char) 46);
        g.range("@class13", (char) 69, (char) 69);
        g.range("@class14", (char) 101, (char) 101);
        g.range("@class15", (char) 43, (char) 43);
        g.range("@class16", (char) 45, (char) 45);
        g.range("@class17", (char) 38, (char) 38);
        g.range("@class18", (char) 47, (char) 47);
        g.range("@class19", (char) 91, (char) 91);
        g.range("@class20", (char) 93, (char) 93);
        g.range("@class21", (char) 44, (char) 44);
        g.range("@class22", (char) 40, (char) 40);
        g.range("@class23", (char) 41, (char) 41);
        g.range("@class24", (char) 65, (char) 90);
        g.range("@class25", (char) 97, (char) 122);
        g.range("@class26", (char) 95, (char) 95);
        g.range("@class27", (char) 45, (char) 45);
        g.range("@class28", (char) 36, (char) 36);
        g.range("@class29", (char) 46, (char) 46);
        g.range("@class30", (char) 48, (char) 57);
        g.combine("@class31", "@class24", "@class25", "@class26", "@class27", "@class28", "@class29", "@class30");
        g.range("@class32", (char) 1, (char) 38);
        g.range("@class33", (char) 40, (char) 65535);
        g.combine("@class34", "@class32", "@class33");
        g.range("@class35", (char) 34, (char) 34);
        g.range("@class36", (char) 34, (char) 34);
        g.range("@class37", (char) 1, (char) 33);
        g.range("@class38", (char) 35, (char) 65535);
        g.combine("@class39", "@class37", "@class38");
        g.range("@class40", (char) 0, (char) 65535);
        g.range("@class41", (char) 48, (char) 57);
        g.combine("@class42", "@class41");
        g.range("@class43", (char) 92, (char) 92);
        g.range("@class44", (char) 98, (char) 98);
        g.range("@class45", (char) 92, (char) 92);
        g.range("@class46", (char) 116, (char) 116);
        g.range("@class47", (char) 92, (char) 92);
        g.range("@class48", (char) 110, (char) 110);
        g.range("@class49", (char) 92, (char) 92);
        g.range("@class50", (char) 102, (char) 102);
        g.range("@class51", (char) 92, (char) 92);
        g.range("@class52", (char) 114, (char) 114);
        g.range("@class53", (char) 92, (char) 92);
        g.range("@class54", (char) 92, (char) 92);
        g.range("@class55", (char) 92, (char) 92);
        g.range("@class56", (char) 92, (char) 92);
        g.range("@class57", (char) 34, (char) 34);
        g.range("@class58", (char) 117, (char) 117);
        g.range("@class59", (char) 48, (char) 57);
        g.range("@class60", (char) 65, (char) 70);
        g.combine("@class61", "@class59", "@class60");
        g.range("@class62", (char) 32, (char) 32);
        g.range("@class63", (char) 9, (char) 9);
        g.range("@class64", (char) 10, (char) 10);
        g.range("@class65", (char) 11, (char) 11);
        g.range("@class66", (char) 12, (char) 12);
        g.range("@class67", (char) 13, (char) 13);
        g.combine("@class68", "@class62", "@class63", "@class64", "@class65", "@class66", "@class67");
        g.range("@class69", (char) 10, (char) 10);
        g.range("@class70", (char) 13, (char) 13);
        g.combine("@class71", "@class69", "@class70");

        // Grammar Rules
        g.choose("@28", "@25", "@26", "@27");
        g.choose("@33", "@30", "@31", "@32");
        g.choose("@37", "@34", "@35", "@36");
        g.choose("@90", "SP", "SINGLE_COMMENT", "MULTI_COMMENT");
        g.choose("DOUBLE_STRING_ELEMENT", "ESCAPE", "DOUBLE_STRING_CHAR");
        g.choose("ESCAPE", "ESCAPE_B", "ESCAPE_T", "ESCAPE_N", "ESCAPE_F", "ESCAPE_R", "ESCAPE_SL", "ESCAPE_SQ", "ESCAPE_DQ", "ESCAPE_U");
        g.choose("MULTI_COMMENT_TEXT", "MULTI_COMMENT", "@101");
        g.choose("SINGLE_STRING_ELEMENT", "ESCAPE", "SINGLE_STRING_CHAR");
        g.choose("action_directive", "setup_action", "close_action", "before_action", "after_action");
        g.choose("atom_rule", "range_rule", "regex_rule", "constant_rule", "rule_reference");
        g.choose("element_modifier", "zoo_modifier", "zom_modifier", "oom_modifier", "minmax_modifier", "exact_modifier", "one_modifier");
        g.choose("maximum_operator", "maximum_inclusive", "maximum_exclusive");
        g.choose("minimum_operator", "minimum_inclusive", "minimum_exclusive");
        g.choose("operand", "nested_rule", "atom_rule", "not_rule", "rule_reference");
        g.choose("rule", "and_rule", "or_rule", "not_rule", "sequence_rule", "predicate_rule", "atom_rule");
        g.choose("statement", "root_directive", "pass_directive", "action_directive", "assignment");
        g.choose("string", "single_string", "double_string");
        g.chr("@12", "@class3");
        g.chr("@16", "@class4");
        g.chr("@2", "@class0");
        g.chr("@20", "@class5");
        g.chr("@21", "@class6");
        g.chr("@22", "@class7");
        g.chr("@25", "@class10");
        g.chr("@26", "@class11");
        g.chr("@29", "@class12");
        g.chr("@30", "@class13");
        g.chr("@31", "@class14");
        g.chr("@34", "@class15");
        g.chr("@35", "@class16");
        g.chr("@4", "@class1");
        g.chr("@42", "@class17");
        g.chr("@45", "@class18");
        g.chr("@51", "@class19");
        g.chr("@53", "@class20");
        g.chr("@54", "@class21");
        g.chr("@65", "@class22");
        g.chr("@66", "@class23");
        g.chr("@67", "@class31");
        g.chr("@70", "@class35");
        g.chr("@71", "@class36");
        g.chr("@72", "@class42");
        g.chr("@73", "@class43");
        g.chr("@74", "@class44");
        g.chr("@75", "@class45");
        g.chr("@76", "@class46");
        g.chr("@77", "@class47");
        g.chr("@78", "@class48");
        g.chr("@79", "@class49");
        g.chr("@8", "@class2");
        g.chr("@80", "@class50");
        g.chr("@81", "@class51");
        g.chr("@82", "@class52");
        g.chr("@83", "@class53");
        g.chr("@84", "@class54");
        g.chr("@85", "@class55");
        g.chr("@87", "@class56");
        g.chr("@88", "@class57");
        g.chr("@89", "@class58");
        g.chr("@92", "@class71");
        g.chr("ANY_CHAR", "@class40");
        g.chr("DOUBLE_STRING_CHAR", "@class39");
        g.chr("HEX_DIGIT", "@class61");
        g.chr("SINGLE_STRING_CHAR", "@class34");
        g.chr("SP", "@class68");
        g.chr("maximum_exclusive", "@class9");
        g.chr("minimum_exclusive", "@class8");
        g.not("@100", "@99");
        g.not("@93", "@92");
        g.repeat("@0", "statement", 0, 2147483647);
        g.repeat("@39", "@38", 0, 1);
        g.repeat("@41", "@40", 0, 1);
        g.repeat("@44", "@43", 1, 2147483647);
        g.repeat("@47", "@46", 1, 2147483647);
        g.repeat("@52", "elements", 0, 1);
        g.repeat("@56", "@55", 0, 2147483647);
        g.repeat("@95", "@94", 0, 2147483647);
        g.repeat("@97", "MULTI_COMMENT_TEXT", 0, 2147483647);
        g.repeat("DIGITS", "@72", 1, 2147483647);
        g.repeat("DOUBLE_STRING_BODY", "DOUBLE_STRING_ELEMENT", 0, 2147483647);
        g.repeat("SINGLE_STRING_BODY", "SINGLE_STRING_ELEMENT", 0, 2147483647);
        g.repeat("WS", "@90", 0, 2147483647);
        g.repeat("name", "@67", 1, 2147483647);
        g.sequence("@101", "@100", "ANY_CHAR");
        g.sequence("@38", "@33", "@37", "DIGITS");
        g.sequence("@40", "@29", "DIGITS", "@39");
        g.sequence("@43", "WS", "@42", "WS", "operand", "WS");
        g.sequence("@46", "WS", "@45", "WS", "operand", "WS");
        g.sequence("@55", "WS", "@54", "WS", "element");
        g.sequence("@94", "@93", "ANY_CHAR");
        g.sequence("ESCAPE_B", "@73", "@74");
        g.sequence("ESCAPE_DQ", "@87", "@88");
        g.sequence("ESCAPE_F", "@79", "@80");
        g.sequence("ESCAPE_N", "@77", "@78");
        g.sequence("ESCAPE_R", "@81", "@82");
        g.sequence("ESCAPE_SL", "@83", "@84");
        g.sequence("ESCAPE_SQ", "@85", "@86");
        g.sequence("ESCAPE_T", "@75", "@76");
        g.sequence("ESCAPE_U", "@89", "HEX_DIGIT", "HEX_DIGIT", "HEX_DIGIT", "HEX_DIGIT");
        g.sequence("FLOAT", "@28", "DIGITS", "@41");
        g.sequence("MULTI_COMMENT", "@96", "@97", "@98");
        g.sequence("SINGLE_COMMENT", "@91", "@95");
        g.sequence("after_action", "@17", "WS", "name", "WS", "@18", "WS", "name", "WS", "@19", "WS", "name", "WS", "@20", "WS");
        g.sequence("and_rule", "and_start", "WS", "operand", "@44");
        g.sequence("any_rule", "@49", "WS");
        g.sequence("assignment", "name", "WS", "@21", "WS", "rule", "WS", "@22", "WS");
        g.sequence("before_action", "@13", "WS", "name", "WS", "@14", "WS", "name", "WS", "@15", "WS", "name", "WS", "@16", "WS");
        g.sequence("close_action", "@9", "WS", "@10", "WS", "@11", "WS", "name", "WS", "@12", "WS");
        g.sequence("constant_rule", "string", "WS");
        g.sequence("double_string", "@70", "DOUBLE_STRING_BODY", "@71", "WS");
        g.sequence("element", "operand", "WS", "element_modifier");
        g.sequence("elements", "element", "@56");
        g.sequence("exact_modifier", "@63", "WS", "maximum", "WS", "@64", "WS");
        g.sequence("float", "FLOAT", "WS");
        g.sequence("input", "WS", "@0", "WS", "END");
        g.sequence("maximum", "DIGITS");
        g.sequence("minimum", "DIGITS");
        g.sequence("minmax_modifier", "@60", "WS", "minimum", "WS", "@61", "WS", "maximum", "WS", "@62", "WS");
        g.sequence("nested_rule", "@65", "WS", "rule", "WS", "@66", "WS");
        g.sequence("not_rule", "@48", "WS", "operand", "WS");
        g.sequence("number", "float");
        g.sequence("oom_modifier", "@59", "WS");
        g.sequence("or_rule", "or_start", "WS", "operand", "@47");
        g.sequence("pass_directive", "@3", "WS", "name", "WS", "@4", "WS");
        g.sequence("predicate_rule", "@50", "WS", "name", "WS");
        g.sequence("range_rule", "@24", "WS", "number", "WS", "minimum_operator", "WS", "name", "WS", "maximum_operator", "WS", "number", "WS");
        g.sequence("regex_rule", "@23", "WS", "string", "WS");
        g.sequence("root_directive", "@1", "WS", "name", "WS", "@2", "WS");
        g.sequence("rule_reference", "name", "WS");
        g.sequence("sequence_rule", "sequence_start", "WS", "@51", "WS", "@52", "WS", "@53", "WS");
        g.sequence("setup_action", "@5", "WS", "@6", "WS", "@7", "WS", "name", "WS", "@8", "WS");
        g.sequence("single_string", "@68", "SINGLE_STRING_BODY", "@69", "WS");
        g.sequence("zom_modifier", "@58", "WS");
        g.sequence("zoo_modifier", "@57", "WS");
        g.str("@1", "root");
        g.str("@10", "close");
        g.str("@11", "call");
        g.str("@13", "during");
        g.str("@14", "before");
        g.str("@15", "call");
        g.str("@17", "during");
        g.str("@18", "after");
        g.str("@19", "call");
        g.str("@23", "matches");
        g.str("@24", "range");
        g.str("@27", "");
        g.str("@3", "pass");
        g.str("@32", "");
        g.str("@36", "");
        g.str("@48", "!");
        g.str("@49", "any");
        g.str("@5", "during");
        g.str("@50", "require");
        g.str("@57", "?");
        g.str("@58", "*");
        g.str("@59", "+");
        g.str("@6", "setup");
        g.str("@60", "{");
        g.str("@61", ",");
        g.str("@62", "}");
        g.str("@63", "{");
        g.str("@64", "}");
        g.str("@68", "'");
        g.str("@69", "'");
        g.str("@7", "call");
        g.str("@86", "'");
        g.str("@9", "during");
        g.str("@91", "#");
        g.str("@96", "(*");
        g.str("@98", "*)");
        g.str("@99", "*)");
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
