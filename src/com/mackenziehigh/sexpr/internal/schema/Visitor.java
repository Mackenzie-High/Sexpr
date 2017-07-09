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

import com.mackenziehigh.sexpr.internal.Escaper;
import com.mackenziehigh.sexpr.internal.schema.Schema.Rule;
import com.mackenziehigh.sexpr.internal.schema.Schema.SequenceElement;
import high.mackenzie.snowflake.ITreeNode;
import high.mackenzie.snowflake.TreeNode;
import java.util.LinkedList;
import java.util.Stack;

/**
 * An instance of this class is used to convert the parse-tree
 * representation of a schema into the object representation thereof.
 */
final class Visitor
        extends AbstractVisitor
{
    private static final Object OR_START = new Object();

    private static final Object AND_START = new Object();

    private static final Object SEQ_START = new Object();

    public final Schema g = new Schema();

    private final Stack<Object> operands = new Stack<>();

    private void visitChildren (final ITreeNode node)
    {
        for (ITreeNode child : node.children())
        {
            visit(child);
        }
    }

    @Override
    public void visitUnknown (ITreeNode node)
    {
        visitChildren(node);
    }

    @Override
    protected void visit_zoo_modifier (ITreeNode node)
    {
        operands.push(0);
        operands.push(1);
    }

    @Override
    protected void visit_zom_modifier (ITreeNode node)
    {
        operands.push(0);
        operands.push(Integer.MAX_VALUE);
    }

    @Override
    protected void visit_single_string (ITreeNode node)
    {
        final String text = new String(Escaper.instance.expand(TreeNode.find(node, "SINGLE_STRING_BODY").text()));
        operands.push(text);
    }

    @Override
    protected void visit_double_string (ITreeNode node)
    {
        final String text = new String(Escaper.instance.expand(TreeNode.find(node, "DOUBLE_STRING_BODY").text()));
        operands.push(text);
    }

    @Override
    protected void visit_rule_reference (ITreeNode node)
    {
        visitChildren(node);
        final String operand = (String) operands.pop();
        operands.push(g.defineReference(operand));
    }

    @Override
    protected void visit_root_directive (ITreeNode node)
    {
        visitChildren(node);
        final String operand = (String) operands.pop();
        g.defineRoot(operand);
    }

    @Override
    protected void visit_regex_rule (ITreeNode node)
    {
        visitChildren(node);
        final String regex = (String) operands.pop();
        operands.push(g.defineRegexRule(regex));
    }

    @Override
    protected void visit_range_rule (ITreeNode node)
    {
        visitChildren(node);
        final double maximum = (Double) operands.pop();
        final boolean maximumInclusive = (Boolean) operands.pop();
        operands.pop();
        final boolean minimumInclusive = (Boolean) operands.pop();
        final double minimum = (Double) operands.pop();
        operands.push(g.defineRangeRule(minimum, minimumInclusive, maximum, maximumInclusive));
    }

    @Override
    protected void visit_predicate_rule (ITreeNode node)
    {
        visitChildren(node);
        final String name = (String) operands.pop();
        operands.push(g.definePredicateRule(name));
    }

    @Override
    protected void visit_or_rule (ITreeNode node)
    {
        visitChildren(node);

        final LinkedList<String> options = new LinkedList<>();

        while (operands.peek() != OR_START)
        {
            options.addFirst(((Rule) operands.pop()).name());
        }
        operands.pop();

        operands.push(g.defineOrRule(options));
    }

    @Override
    protected void visit_oom_modifier (ITreeNode node)
    {
        operands.push(1);
        operands.push(Integer.MAX_VALUE);
    }

    @Override
    protected void visit_not_rule (ITreeNode node)
    {
        visitChildren(node);

        final Rule operand = (Rule) operands.pop();
        operands.push(g.defineNotRule(operand.name()));
    }

    @Override
    protected void visit_minimum (ITreeNode node)
    {
        final String text = node.text();
        final int minimum = Integer.parseInt(text);
        operands.push(minimum);
    }

    @Override
    protected void visit_maximum (ITreeNode node)
    {
        final String text = node.text();
        final int maximum = Integer.parseInt(text);
        operands.push(maximum);
    }

    @Override
    protected void visit_float (ITreeNode node)
    {
        final String text = TreeNode.find(node, "FLOAT").text();
        final double value = Double.parseDouble(text);
        operands.push(value);
    }

    @Override
    protected void visit_exact_modifier (ITreeNode node)
    {
        visitChildren(node);
        final int exact = (Integer) operands.pop();
        final int minimum = exact;
        final int maximum = exact;
        operands.push(minimum);
        operands.push(maximum);
    }

    @Override
    protected void visit_constant_rule (ITreeNode node)
    {
        visitChildren(node);
        final String text = (String) operands.pop();
        operands.push(g.defineConstantRule(text));
    }

    @Override
    protected void visit_assignment (ITreeNode node)
    {
        visitChildren(node);
        final Rule value = (Rule) operands.pop();
        final String assignee = (String) operands.pop();
        operands.add(g.defineNamedRule(assignee, value.name()));
    }

    @Override
    protected void visit_and_rule (ITreeNode node)
    {
        visitChildren(node);

        final LinkedList<String> options = new LinkedList<>();

        while (operands.peek() != AND_START)
        {
            options.addFirst(((Rule) operands.pop()).name());
        }
        operands.pop();

        operands.push(g.defineAndRule(options));
    }

    @Override
    protected void visit_name (ITreeNode node)
    {
        operands.push(node.text());
    }

    @Override
    protected void visit_sequence_rule (ITreeNode node)
    {
        visitChildren(node);

        final LinkedList<SequenceElement> elements = new LinkedList<>();

        while (operands.peek() != SEQ_START)
        {
            elements.addFirst((SequenceElement) operands.pop());
        }
        operands.pop();

        operands.push(g.defineSequenceRule(elements));
    }

    @Override
    protected void visit_or_start (ITreeNode node)
    {
        operands.push(OR_START);
    }

    @Override
    protected void visit_one_modifier (ITreeNode node)
    {
        operands.push(1);
        operands.push(1);
    }

    @Override
    protected void visit_minimum_inclusive (ITreeNode node)
    {
        operands.push(true);
    }

    @Override
    protected void visit_minimum_exclusive (ITreeNode node)
    {
        operands.push(false);
    }

    @Override
    protected void visit_maximum_inclusive (ITreeNode node)
    {
        operands.push(true);
    }

    @Override
    protected void visit_maximum_exclusive (ITreeNode node)
    {
        operands.push(false);
    }

    @Override
    protected void visit_and_start (ITreeNode node)
    {
        operands.push(AND_START);
    }

    @Override
    protected void visit_element_modifier (ITreeNode node)
    {
        visitChildren(node);

        final int maximum = (Integer) operands.pop();
        final int minimum = (Integer) operands.pop();
        final String rule = ((Rule) operands.pop()).name();
        final SequenceElement element = new Schema.SequenceElement()
        {
            @Override
            public String element ()
            {
                return rule;
            }

            @Override
            public int maximum ()
            {
                return maximum;
            }

            @Override
            public int minimum ()
            {
                return minimum;
            }
        };

        operands.push(element);
    }

    @Override
    protected void visit_sequence_start (ITreeNode node)
    {
        operands.push(SEQ_START);
    }
}
