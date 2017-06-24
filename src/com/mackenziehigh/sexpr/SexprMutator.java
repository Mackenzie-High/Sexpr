package com.mackenziehigh.sexpr;

import java.util.Objects;

/**
 * An instance of this interface simplifies the modification
 * of symbolic-expressions, since they are immutable.
 *
 * @param <R>
 */
public final class SexprMutator<R extends Sexpr<R>>
{
    private final SexprMutator<SList> below;

    private final Sexpr node;

    public SexprMutator (final Sexpr node)
    {
        this(null, node);
    }

    private SexprMutator (final SexprMutator<SList> below,
                          final Sexpr node)
    {
        this.below = below;
        this.node = Objects.requireNonNull(node);
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final boolean value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final char value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final byte value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final short value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final int value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final long value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final float value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final double value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final Class value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final byte[] value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the value of the selected atom.
     *
     * @param value is the new value for the atom.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node() is not an atom.
     */
    public SexprMutator<R> set (final String value)
    {
        return set(new SAtom(node().location(), value));
    }

    /**
     * This method sets the selected node to the given value.
     *
     * @param value will replace the selected node.
     * @return a new instance that encodes this action.
     */
    public SexprMutator<R> set (Sexpr value)
    {
        return null;
    }

    /**
     * This method selects the first element in the currently selected list.
     *
     * @return a new instance that encodes this action.
     * @throws IndexOutOfBoundsException if node() is not a list.
     * @throws java.util.NoSuchElementException if the list is empty.
     */
    public SexprMutator<R> first ()
    {
        return get(0);
    }

    /**
     * This method selects the last element in the currently selected list.
     *
     * @return this.
     * @throws IndexOutOfBoundsException if node() is not a list.
     * @throws java.util.NoSuchElementException if the list is empty.
     */
    public SexprMutator<R> last ()
    {
        return get(-1);
    }

    /**
     * This method selects an element in the currently selected list.
     *
     * <p>
     * This method supports negative indexing.
     * For example, (-1) can be used to reference the last element.
     * </p>
     *
     * @param index is the index of the element to select.
     * @return a new instance that encodes this action.
     * @throws IndexOutOfBoundsException if node() is not a list.
     * @throws java.util.NoSuchElementException if the list is empty.
     */
    public SexprMutator<R> get (final int index)
    {
        if (node.isList())
        {
            return null;
        }
        else
        {
            throw new IllegalStateException("get(int) requires that node() be a SList.");
        }
    }

    /**
     * This method retrieves the currently selected node.
     *
     * @return the selected node.
     */
    public Sexpr node ()
    {
        return node;
    }

    /**
     * This method builds a symbolic-expression based
     * on the modifications described herein.
     *
     * @return the newly built symbolic-expression.
     */
    public R build ()
    {
        return null;
    }
}
