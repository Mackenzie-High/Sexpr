package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.internal.schema.Schema.Rule;
import com.mackenziehigh.sexpr.internal.schema.Schema.SequenceElement;
import java.util.Arrays;
import java.util.stream.Collectors;

public final class SchemaDev
{
    private final Schema s = new Schema();

    public SchemaDev ()
    {
        s.defineRoot("ROOT");

        // (ROOT = (seq (star (ref STATEMENT))))
        seq("ROOT", star(ref("STATEMENT")));

        // (STATEMENT = (either (ref ROOT_DECLARATION)
        //                      (ref ASSIGNMENT)
        //                      (ref RULE)))
        either("STATEMENT",
               ref("ROOT_DECLARATION"),
               ref("ASSIGNMENT"),
               ref("RULE"));

        // (RULE = (either (ref SEQUENCE)
        //                 (ref OR)
        //                 (ref AND)
        //                 (ref NOT)
        //                 (ref REF)
        //                 (ref ATOM)
        //                 (ref KEYWORD)
        //                 (ref PREDICATE)))
        either("RULE",
               ref("SEQUENCE"),
               ref("OR"),
               ref("AND"),
               ref("NOT"),
               ref("REF"),
               ref("ATOM"),
               ref("KEYWORD"),
               ref("PREDICATE"));

        // (ROOT_DECLARATION = (seq (keyword 'root') (ref NAME)))
        assign("ROOT_DECLARATION", seq(once(atom("root")), once(ref("NAME"))));

        // (ASSIGNMENT = (seq (ref NAME) (keyword '=') (ref RULE)))
        assign("ASSIGNMENT", seq(once(ref("NAME")), once(atom("[=]")), once(ref("RULE"))));

        // (SEQUENCE = (seq (keyword 'seq') (star (ref ELEMENT))))
        assign("SEQUENCE", seq(once(atom("seq")), star(ref("ELEMENT"))));

        // (ELEMENT = (either (ref OPTION)
        //                    (ref STAR)
        //                    (ref PLUS)
        //                    (ref REPEAT)
        //                    (ref RULE)))
        either("ELEMENT",
               ref("OPTION"),
               ref("STAR"),
               ref("PLUS"),
               ref("REPEAT"),
               ref("RULE"));

        // (OPTION = (seq (keyword 'option') (ref RULE)))
        assign("OPTION", seq(once(atom("option")), once(ref("RULE"))));

        // (STAR = (seq (keyword 'star') (ref RULE)))
        assign("STAR", seq(once(atom("star")), once(ref("RULE"))));

        // (PLUS = (seq (keyword 'plus') (ref RULE)))
        assign("PLUS", seq(once(atom("plus")), once(ref("RULE"))));

        // (REPEAT = (seq (keyword 'repeat') (ref RULE) (atom '[0-9]+') (atom '[0-9]+')))
        assign("REPEAT", seq(once(atom("repeat")),
                             once(ref("RULE")),
                             once(atom("[0-9]+")),
                             once(atom("[0-9]+"))));

        // (OR = (seq (keyword 'either') (star (ref RULE))))
        assign("OR", seq(once(atom("either")), star(ref("RULE"))));

        // (AND = (seq (keyword 'and') (star (ref RULE))))
        assign("AND", seq(once(atom("and")), star(ref("RULE"))));

        // (NOT = (seq (keyword 'not') (ref RULE)))
        assign("NOT", seq(once(atom("not")), once(ref("RULE"))));

        // (REF = (seq (keyword 'ref') (ref NAME)))
        assign("REF", seq(once(atom("ref")), once(ref("NAME"))));

        // (ATOM = (seq (keyword 'atom') (option (atom))))
        assign("ATOM", seq(once(atom("atom")), option(atom(".*"))));

        // (KEYWORD = (seq (keyword 'keyword') (atom)))
        assign("KEYWORD", seq(once(atom("keyword")), once(atom(".*"))));

        // (PREDICATE = (seq (keyword 'predicate') (ref NAME)))
        assign("PREDICATE", seq(once(atom("predicate")), once(ref("NAME"))));

        // (NAME = (atom '[A-Za-z_][A-Za-z_0-9]*'))
        assign("NAME", atom(".*"));
    }

    private Rule seq (final String name,
                      final SequenceElement... elements)
    {
        final Rule anonRule = s.defineSequenceRule(Arrays.asList(elements));
        final Rule namedRule = s.defineNamedRule(name, anonRule.name());
        return namedRule;
    }

    private Rule seq (final SequenceElement... elements)
    {
        return s.defineSequenceRule(Arrays.asList(elements));
    }

    private Rule assign (final String name,
                         final Rule value)
    {
        return either(name, value);
    }

    private Rule either (final String name,
                         final Rule... options)
    {
        final Rule anonRule = s.defineOrRule(Arrays.asList(options).stream().map(x -> x.name()).collect(Collectors.toList()));
        final Rule namedRule = s.defineNamedRule(name, anonRule.name());
        return namedRule;
    }

    private SequenceElement repeat (final Rule rule,
                                    final int min,
                                    final int max)
    {
        final String element = rule.name();

        return new SequenceElement()
        {
            @Override
            public String element ()
            {
                return element;
            }

            @Override
            public int minimum ()
            {
                return min;
            }

            @Override
            public int maximum ()
            {
                return max;
            }
        };
    }

    private SequenceElement once (final Rule rule)
    {
        return repeat(rule, 1, 1);
    }

    private SequenceElement option (final Rule rule)
    {
        return repeat(rule, 0, 1);
    }

    private SequenceElement star (final Rule rule)
    {
        return repeat(rule, 0, Integer.MAX_VALUE);
    }

    private Rule atom (final String regex)
    {
        return s.defineRegexRule(regex);
    }

    private Rule ref (final String name)
    {
        final Rule anonRule = s.defineReference(name);
        final Rule namedRule = s.defineNamedRule(name + "_" + anonRule.name(), anonRule.name());
        return namedRule;
    }

    public static void main (String[] args)
    {
        final SchemaDev dev = new SchemaDev();

        dev.s.definePass("TRANSLATE");
        dev.s.defineBeforeAction("TRANSLATE", "ATOM", x -> System.out.println("XX = " + x));

        final SList input = SList.parse("", "(x = (either (atom) (keyword 'X')))");

        System.out.println("IN = " + input);
        System.out.println(dev.s.match(input));

    }

}
