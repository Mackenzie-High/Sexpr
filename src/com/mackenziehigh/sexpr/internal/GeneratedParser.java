package com.mackenziehigh.sexpr.internal;

import high.mackenzie.snowflake.Grammar;
import high.mackenzie.snowflake.GrammarBuilder;
import high.mackenzie.snowflake.IParser;
import high.mackenzie.snowflake.ParserOutput;

/**
 * This class was auto-generated using the Snowflake parser-generator.
 *
 * <p>
 * Generated On: Sun Jun 25 07:41:13 EDT 2017</p>
 */
public final class GeneratedParser
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
        g.range("@class0", (char) 64, (char) 64);
        g.range("@class1", (char) 39, (char) 39);
        g.combine("@class2", "@class1");
        g.negate("@class3", "@class2");
        g.range("@class4", (char) 64, (char) 64);
        g.range("@class5", (char) 34, (char) 34);
        g.range("@class6", (char) 34, (char) 34);
        g.range("@class7", (char) 34, (char) 34);
        g.range("@class8", (char) 34, (char) 34);
        g.combine("@class9", "@class8");
        g.negate("@class10", "@class9");
        g.range("@class11", (char) 39, (char) 39);
        g.combine("@class12", "@class11");
        g.negate("@class13", "@class12");
        g.range("@class14", (char) 34, (char) 34);
        g.range("@class15", (char) 34, (char) 34);
        g.range("@class16", (char) 34, (char) 34);
        g.range("@class17", (char) 34, (char) 34);
        g.combine("@class18", "@class17");
        g.negate("@class19", "@class18");
        g.range("@class20", (char) 32, (char) 32);
        g.range("@class21", (char) 9, (char) 9);
        g.range("@class22", (char) 10, (char) 10);
        g.range("@class23", (char) 11, (char) 11);
        g.range("@class24", (char) 12, (char) 12);
        g.range("@class25", (char) 13, (char) 13);
        g.range("@class26", (char) 34, (char) 34);
        g.range("@class27", (char) 39, (char) 39);
        g.range("@class28", (char) 35, (char) 35);
        g.range("@class29", (char) 40, (char) 40);
        g.range("@class30", (char) 41, (char) 41);
        g.combine("@class31", "@class20", "@class21", "@class22", "@class23", "@class24", "@class25", "@class26", "@class27", "@class28", "@class29", "@class30");
        g.negate("@class32", "@class31");
        g.range("@class33", (char) 40, (char) 40);
        g.range("@class34", (char) 42, (char) 42);
        g.range("@class35", (char) 42, (char) 42);
        g.range("@class36", (char) 41, (char) 41);
        g.range("@class37", (char) 0, (char) 65535);
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

        // Grammar Rules
        g.choose("@20", "ESCAPE", "@19");
        g.choose("@27", "ESCAPE", "@26");
        g.choose("@52", "SP", "COMMENT");
        g.choose("COMMENT_TEXT", "COMMENT", "@58");
        g.choose("ESCAPE", "ESCAPE_B", "ESCAPE_T", "ESCAPE_N", "ESCAPE_F", "ESCAPE_R", "ESCAPE_SL", "ESCAPE_SQ", "ESCAPE_DQ", "ESCAPE_U");
        g.choose("satom", "satom_form1", "satom_form2", "satom_form3", "satom_form4", "satom_form5");
        g.choose("sexpr", "satom", "slist");
        g.chr("@0", "@class0");
        g.chr("@10", "@class7");
        g.chr("@12", "@class10");
        g.chr("@18", "@class13");
        g.chr("@21", "@class14");
        g.chr("@22", "@class15");
        g.chr("@23", "@class16");
        g.chr("@25", "@class19");
        g.chr("@28", "@class32");
        g.chr("@29", "@class33");
        g.chr("@30", "@class34");
        g.chr("@32", "@class35");
        g.chr("@34", "@class36");
        g.chr("@35", "@class38");
        g.chr("@36", "@class39");
        g.chr("@37", "@class40");
        g.chr("@38", "@class41");
        g.chr("@39", "@class42");
        g.chr("@40", "@class43");
        g.chr("@41", "@class44");
        g.chr("@42", "@class45");
        g.chr("@43", "@class46");
        g.chr("@44", "@class47");
        g.chr("@45", "@class48");
        g.chr("@46", "@class49");
        g.chr("@47", "@class50");
        g.chr("@49", "@class51");
        g.chr("@5", "@class3");
        g.chr("@50", "@class52");
        g.chr("@51", "@class53");
        g.chr("@7", "@class4");
        g.chr("@8", "@class5");
        g.chr("@9", "@class6");
        g.chr("ANY_CHAR", "@class37");
        g.chr("HEX_DIGIT", "@class56");
        g.chr("SP", "@class63");
        g.not("@11", "@10");
        g.not("@17", "@16");
        g.not("@24", "@23");
        g.not("@31", "@30");
        g.not("@33", "@32");
        g.not("@4", "@3");
        g.not("@57", "@56");
        g.repeat("@54", "COMMENT_TEXT", 0, 2147483647);
        g.repeat("WS", "@52", 0, 2147483647);
        g.repeat("satom_form1_text", "@6", 0, 2147483647);
        g.repeat("satom_form2_text", "@13", 0, 2147483647);
        g.repeat("satom_form3_text", "@20", 0, 2147483647);
        g.repeat("satom_form4_text", "@27", 0, 2147483647);
        g.repeat("satom_form5_text", "@28", 1, 2147483647);
        g.repeat("slist_elements", "slist_element", 0, 2147483647);
        g.sequence("@13", "@11", "@12");
        g.sequence("@19", "@17", "@18");
        g.sequence("@26", "@24", "@25");
        g.sequence("@58", "@57", "ANY_CHAR");
        g.sequence("@6", "@4", "@5");
        g.sequence("COMMENT", "@53", "@54", "@55");
        g.sequence("ESCAPE_B", "@35", "@36");
        g.sequence("ESCAPE_DQ", "@49", "@50");
        g.sequence("ESCAPE_F", "@41", "@42");
        g.sequence("ESCAPE_N", "@39", "@40");
        g.sequence("ESCAPE_R", "@43", "@44");
        g.sequence("ESCAPE_SL", "@45", "@46");
        g.sequence("ESCAPE_SQ", "@47", "@48");
        g.sequence("ESCAPE_T", "@37", "@38");
        g.sequence("ESCAPE_U", "@51", "HEX_DIGIT", "HEX_DIGIT", "HEX_DIGIT", "HEX_DIGIT");
        g.sequence("input", "WS", "slist_body", "WS", "END");
        g.sequence("satom_form1", "@0", "@1", "satom_form1_text", "@2", "WS");
        g.sequence("satom_form2", "@7", "@8", "satom_form2_text", "@9", "WS");
        g.sequence("satom_form3", "@14", "satom_form3_text", "@15", "WS");
        g.sequence("satom_form4", "@21", "satom_form4_text", "@22", "WS");
        g.sequence("satom_form5", "satom_form5_text", "WS");
        g.sequence("slist", "@29", "@31", "slist_body", "@33", "@34", "WS");
        g.sequence("slist_body", "slist_start", "WS", "slist_elements", "WS", "slist_end", "WS");
        g.sequence("slist_element", "sexpr");
        g.str("@1", "'");
        g.str("@14", "'");
        g.str("@15", "'");
        g.str("@16", "'");
        g.str("@2", "'");
        g.str("@3", "'");
        g.str("@48", "'");
        g.str("@53", "(*");
        g.str("@55", "*)");
        g.str("@56", "*)");
        g.str("slist_end", "");
        g.str("slist_start", "");

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
