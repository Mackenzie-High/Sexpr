package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SourceLocation;
import high.mackenzie.snowflake.ITreeNode;
import high.mackenzie.snowflake.LinesAndColumns;
import high.mackenzie.snowflake.NewlineStyles;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

/**
 *
 * @author mackenzie
 */
final class Visitor
        extends AbstractVisitor
{
    private final Object SLIST_START = new Object();

    public final Stack<Object> stack = new Stack<>();

    private final String source;

    private final LinesAndColumns locator;

    public Visitor (final String source,
                    final String input)
    {
        this.source = Objects.requireNonNull(source);
        this.locator = new LinesAndColumns(input.toCharArray(), NewlineStyles.fromSystem());
    }

    private void visitChildren (ITreeNode node)
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

    private SourceLocation locate (final ITreeNode node)
    {

        return new SourceLocation()
        {
            @Override
            public int column ()
            {
                return locator.columnNumbers()[node.start()];
            }

            @Override
            public int line ()
            {
                return locator.lineNumbers()[node.start()];
            }

            @Override
            public String source ()
            {
                return source;
            }

        };
    }

    private void createAtom (final ITreeNode node,
                             final boolean expand)
    {
        final String text = new String(expand ? Escaper.instance.expand(node.text()) : node.text().toCharArray());
        final BaseAtom atom = new BaseAtom(text)
        {
            @Override
            public SourceLocation location ()
            {
                return locate(node);
            }
        };
        stack.push(atom);
    }

    @Override
    protected void visit_slist_start (ITreeNode node)
    {
        stack.push(SLIST_START);
    }

    @Override
    protected void visit_slist (ITreeNode node)
    {
        /**
         * Get the elements off of the stack.
         * Be sure to keep them in the correct order.
         */
        final LinkedList<Sexpr> elements = new LinkedList<>();
        while (stack.peek().equals(SLIST_START) == false)
        {
            elements.addFirst((Sexpr) stack.pop());
        }
        stack.pop(); // SLIST_START

        /**
         * Create the Symbolic List.
         */
        final SList list = new BaseList(elements)
        {
            @Override
            public SourceLocation location ()
            {
                return locate(node);
            }
        };
        stack.push(list);
    }

    @Override
    protected void visit_satom_form5_text (ITreeNode node)
    {
        createAtom(node, false);
    }

    @Override
    protected void visit_satom_form4_text (ITreeNode node)
    {
        createAtom(node, true);
    }

    @Override
    protected void visit_satom_form3_text (ITreeNode node)
    {
        createAtom(node, true);
    }

    @Override
    protected void visit_satom_form2_text (ITreeNode node)
    {
        createAtom(node, false);
    }

    @Override
    protected void visit_satom_form1_text (ITreeNode node)
    {
        createAtom(node, false);
    }

}
