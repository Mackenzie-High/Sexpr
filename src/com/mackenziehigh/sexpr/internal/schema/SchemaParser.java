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
package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.exceptions.InvalidSchemaException;
import com.mackenziehigh.sexpr.internal.schema.Schema.Rule;
import com.mackenziehigh.sexpr.internal.schema.Schema.SequenceElement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class SchemaParser
{
    private final Schema g = new Schema();

    private final Schema b = new Schema();

    private final Stack<Object> stack = new Stack<>();

    public SchemaParser ()
    {
        g.defineRoot("ROOT");

        // (ROOT = (seq (star (ref STATEMENT))))
        seq("ROOT", star(ref("STATEMENT")));

        // (STATEMENT = (either (ref ROOT_DECLARATION)
        //                      (ref ASSIGNMENT)
        //                      (ref RULE)))
        either("STATEMENT",
               ref("ROOT_DECLARATION"),
               ref("ASSIGNMENT"));

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

        /**
         * Bind Translation Actions.
         */
        final String TRANSLATE = "TRANSLATE";
        g.definePass(TRANSLATE);
        g.defineAfterAction(TRANSLATE, "ROOT_DECLARATION", this::translateRoot);
        g.defineAfterAction(TRANSLATE, "ASSIGNMENT", this::translateAssign);
        g.defineAfterAction(TRANSLATE, "SEQUENCE", this::translateSeq);
        g.defineAfterAction(TRANSLATE, "OPTION", this::translateOption);
        g.defineAfterAction(TRANSLATE, "STAR", this::translateStar);
        g.defineAfterAction(TRANSLATE, "PLUS", this::translatePlus);
        g.defineAfterAction(TRANSLATE, "REPEAT", this::translateRepeat);
        g.defineAfterAction(TRANSLATE, "OR", this::translateEither);
        g.defineAfterAction(TRANSLATE, "AND", this::translateAnd);
        g.defineAfterAction(TRANSLATE, "NOT", this::translateNot);
        g.defineAfterAction(TRANSLATE, "ATOM", this::translateAtom);
        g.defineAfterAction(TRANSLATE, "KEYWORD", this::translateKeyword);
        g.defineAfterAction(TRANSLATE, "PREDICATE", this::translatePredicate);
        g.defineAfterAction(TRANSLATE, "REF", this::translateRef);
    }

    private Rule seq (final String name,
                      final SequenceElement... elements)
    {
        final Rule anonRule = g.defineSequenceRule(Arrays.asList(elements));
        final Rule namedRule = g.defineNamedRule(name, anonRule.name());
        return namedRule;
    }

    private Rule seq (final SequenceElement... elements)
    {
        return g.defineSequenceRule(Arrays.asList(elements));
    }

    private Rule assign (final String name,
                         final Rule value)
    {
        return either(name, value);
    }

    private Rule either (final String name,
                         final Rule... options)
    {
        final Rule anonRule = g.defineOrRule(Arrays.asList(options).stream().map(x -> x.name()).collect(Collectors.toList()));
        final Rule namedRule = g.defineNamedRule(name, anonRule.name());
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
        return g.defineRegexRule(regex);
    }

    private Rule ref (final String name)
    {
        final Rule anonRule = g.defineReference(name);
        final Rule namedRule = g.defineNamedRule(name + "_" + anonRule.name(), anonRule.name());
        return namedRule;
    }

    private void translateRoot (final Sexpr<?> node)
    {
        final String name = node.asList().get(1).toString();
        b.defineRoot(name);
    }

    private void translateAssign (final Sexpr<?> node)
    {
        final String name = node.asList().get(0).toString();
        final Rule value = (Rule) stack.pop();
        b.defineNamedRule(name, value.name());
    }

    private void translateSeq (final Sexpr<?> node)
    {
        final int elementCount = node.asList().size() - 1;

        final LinkedList<SequenceElement> elements = new LinkedList<>();

        for (int i = 0; i < elementCount; i++)
        {
            final SequenceElement element = convertToSequenceElement(stack.pop());
            elements.addFirst(element);
        }

        final Rule rule = b.defineSequenceRule(elements);
        stack.push(rule);
    }

    private SequenceElement convertToSequenceElement (final Object object)
    {
        if (object instanceof SequenceElement)
        {
            return (SequenceElement) object;
        }

        final Rule element = (Rule) object;

        return new SequenceElement()
        {
            @Override
            public String element ()
            {
                return element.name();
            }

            @Override
            public int minimum ()
            {
                return 1;
            }

            @Override
            public int maximum ()
            {
                return 1;
            }
        };
    }

    private void translateOption (final Sexpr<?> node)
    {
        final Rule element = (Rule) stack.pop();
        translateRepeat(element, 0, 1);
    }

    private void translateStar (final Sexpr<?> node)
    {
        final Rule element = (Rule) stack.pop();
        translateRepeat(element, 0, Integer.MAX_VALUE);
    }

    private void translatePlus (final Sexpr<?> node)
    {
        final Rule element = (Rule) stack.pop();
        translateRepeat(element, 1, Integer.MAX_VALUE);
    }

    private void translateRepeat (final Sexpr<?> node)
    {
        final Rule element = (Rule) stack.pop();
        final int minimum = Integer.parseInt(node.asList().get(2).toString());
        final int maximum = Integer.parseInt(node.asList().get(3).toString());
        translateRepeat(element, minimum, maximum);
    }

    private void translateRepeat (final Rule rule,
                                  final int minimum,
                                  final int maximum)
    {
        final SequenceElement seqelm = new SequenceElement()
        {
            @Override
            public String element ()
            {
                return rule.name();
            }

            @Override
            public int minimum ()
            {
                return minimum;
            }

            @Override
            public int maximum ()
            {
                return maximum;
            }
        };

        stack.push(seqelm);
    }

    private void translateEither (final Sexpr<?> node)
    {
        final int elementCount = node.asList().size() - 1;

        final LinkedList<String> elements = new LinkedList<>();

        for (int i = 0; i < elementCount; i++)
        {
            final Rule element = (Rule) stack.pop();
            elements.addFirst(element.name());
        }

        final Rule rule = b.defineOrRule(elements);
        stack.push(rule);
    }

    private void translateAnd (final Sexpr<?> node)
    {
        final int elementCount = node.asList().size() - 1;

        final LinkedList<String> elements = new LinkedList<>();

        for (int i = 0; i < elementCount; i++)
        {
            final Rule element = (Rule) stack.pop();
            elements.addFirst(element.name());
        }

        final Rule rule = b.defineAndRule(elements);
        stack.push(rule);
    }

    private void translateNot (final Sexpr<?> node)
    {
        final Rule operand = (Rule) stack.pop();
        final Rule rule = b.defineNotRule(operand.name());
        stack.push(rule);
    }

    private void translateAtom (final Sexpr<?> node)
    {
        final String regex = node.asList().size() == 1 ? ".*" : node.asList().get(1).toString();
        final Rule rule = b.defineRegexRule(regex);
        stack.push(rule);
    }

    private void translateKeyword (final Sexpr<?> node)
    {
        final String keyword = node.asList().get(1).toString();
        final Rule rule = b.defineConstantRule(keyword);
        stack.push(rule);
    }

    private void translatePredicate (final Sexpr<?> node)
    {
        final String name = node.asList().get(1).toString();
        final Rule rule = b.definePredicateRule(name);
        stack.push(rule);
    }

    private void translateRef (final Sexpr<?> node)
    {
        final String name = node.asList().get(1).toString();
        final Rule rule = b.defineReference(name);
        stack.push(rule);
    }

    public static Schema parse (final String location,
                                final String schema)
    {
        final Consumer<Optional<Sexpr>> onError = last ->
        {
            // TODO: Improve location reporting
            final String message = String.format("Last Successful Match = %s", last);
            throw new InvalidSchemaException(message);
        };

        final SchemaParser parser = new SchemaParser();
        parser.g.setFailureHandler(onError);

        final SList objectSchema = SList.parse(location, schema);

        parser.g.match(objectSchema);

        final Schema result = parser.b;
        return result;
    }
}
