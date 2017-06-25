package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SourceLocation;
import com.mackenziehigh.sexpr.internal.AbstractVisitor;
import high.mackenzie.snowflake.ITreeNode;
import high.mackenzie.snowflake.LinesAndColumns;
import high.mackenzie.snowflake.NewlineStyles;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Stack;

/**
 * An instance of this class is used to visit nodes in an AST.
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
        final int column = locator.columnNumbers()[node.start()];
        final int line = locator.lineNumbers()[node.start()];
        return new SourceLocation(source, line, column);
    }

    private void createAtom (final ITreeNode node,
                             final boolean expand)
    {
        final String text = new String(expand ? Escaper.instance.expand(node.text()) : node.text().toCharArray());
        final SAtom atom = new SAtom(locate(node), text);
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
        visitChildren(node);

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
        final SList list = SList.copyOf(locate(node), elements);
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
