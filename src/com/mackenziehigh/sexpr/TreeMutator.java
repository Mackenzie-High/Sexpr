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
package com.mackenziehigh.sexpr;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * An instance of this interface simplifies the modification
 * of symbolic-expressions, since they are immutable.
 */
public final class TreeMutator
{
    /**
     * The mutator in this field relates to a node() closer the root of the tree.
     * If this field is null, then this mutator relates to the root of the tree.
     */
    private final TreeMutator below;

    /**
     * This is the node in the tree that this mutator relates to.
     */
    private final Sexpr node;

    /**
     * This is the index of the node() in the immediately enclosing list.
     * If the node() is the root of the tree, then this field is irrelevant.
     */
    private final int index;

    /**
     * Sole Public Constructor.
     *
     * @param node is the root of a symbolic-expression tree.
     */
    public TreeMutator (final SList node)
    {
        this(null, node, 0);
    }

    private TreeMutator (final TreeMutator below,
                         final Sexpr node,
                         final int index)
    {
        this.below = below;
        this.node = Objects.requireNonNull(node);
        this.index = index;
    }

    /**
     * This method appends the given value onto the selected node,
     * if the selected node is a symbolic-list.
     *
     * @param value is the value to append onto the node().
     * @return a modified copy of the symbolic-expression.
     * @throws IllegalStateException if node().isList() is false.
     */
    public SList append (final Sexpr value)
    {
        if (node.isList())
        {
            final LinkedList<Sexpr> elements = new LinkedList<>(node.toList());
            elements.addLast(Objects.requireNonNull(value));
            final SList modified = SList.copyOf(elements);
            return set(modified);
        }
        else
        {
            throw new IllegalStateException("append(Sexpr) cannot be used on th tree root.");
        }
    }

    /**
     * This method prepends the given value onto the selected node,
     * if the selected node is a symbolic-list.
     *
     * @param value is the value to prepend onto the node().
     * @return a modified copy of the symbolic-expression.
     * @throws IllegalStateException if node().isList() is false.
     */
    public SList prepend (final Sexpr value)
    {
        if (node.isList())
        {
            final LinkedList<Sexpr> elements = new LinkedList<>(node.toList());
            elements.addFirst(Objects.requireNonNull(value));
            final SList modified = SList.copyOf(elements);
            return set(modified);
        }
        else
        {
            throw new IllegalStateException("prepend(Sexpr) cannot be used on th tree root.");
        }
    }

    /**
     * This method sets the selected node to the given value.
     *
     * @param value will replace the node() in the tree.
     * @return a modified copy of the symbolic-expression.
     * @throws IllegalStateException if the selected node is the root of the tree.
     */
    public SList set (final Sexpr value)
    {
        if (below == null)
        {
            throw new IllegalStateException("set(Sexpr) cannot be used on th tree root.");
        }
        else
        {
            return below.rebuild(Objects.requireNonNull(value), index);
        }
    }

    /**
     * This method selects the first element in the currently selected list node().
     *
     * @return a new instance of this class, which encodes this action.
     * @throws IllegalStateException if node().isList() is false.
     * @throws IndexOutOfBoundsException if node() is the list is empty.
     */
    public TreeMutator first ()
    {
        return get(0);
    }

    /**
     * This method selects the last element in the currently selected list node().
     *
     * @return a new instance of this class, which encodes this action.
     * @throws IllegalStateException if node().isList() is false.
     * @throws IndexOutOfBoundsException if node() is the list is empty.
     */
    public TreeMutator last ()
    {
        if (node.isList())
        {
            return get(node.toList().size() - 1);
        }
        else
        {
            throw new IllegalStateException("last() requires that node() be a SList.");
        }
    }

    /**
     * This method selects an element in the currently selected list node().
     *
     * @param index is the index of the element to select.
     * @return a new instance that encodes this action.
     * @throws IllegalStateException if node().isList() is false.
     * @throws IndexOutOfBoundsException if node() is the list is empty.
     */
    public TreeMutator get (final int index)
    {
        if (node.isList())
        {
            final Sexpr element = node.toList().get(index);
            final TreeMutator step = new TreeMutator(this, element, index);
            return step;
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
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return node.toString();
    }

    /**
     * This method propagates changes out towards the root of the tree.
     *
     * @param value will replace an element in the current node().
     * @param position is the position of the element to replace().
     * @return the root of the modified tree.
     */
    private SList rebuild (final Sexpr value,
                           final int position)
    {
        /**
         * Replace the element at the given position with the new value.
         * Since the list is immutable, we create a modified copy.
         */
        final List<Sexpr> elements = new ArrayList<>(node.toList());
        elements.set(position, value);
        final SList modified = SList.copyOf(elements);

        /**
         * Recursively work our way towards the root of the tree.
         */
        final SList result = below == null ? modified : below.rebuild(modified, index);

        /**
         * Return the modified symbolic-expression tree.
         */
        return result;
    }
}
