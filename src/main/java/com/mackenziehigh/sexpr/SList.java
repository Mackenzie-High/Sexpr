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

import com.mackenziehigh.sexpr.internal.Parser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Symbolic List.
 *
 * <p>
 * Textually, a SList consists of an opening parenthesis,
 * a the series of elements separated by spaces,
 * followed by a closing parenthesis.
 * </p>
 *
 * <p>
 * All instances of this interface are immutable.
 * </p>
 */
public final class SList
        extends AbstractList<Sexpr<?>>
        implements Sexpr<SList>
{
    private final ArrayList<Sexpr> elements;

    private final SourceLocation location;

    private final int treeLeafCount;

    private final int treeHeight;

    private final int treeSize;

    /**
     * Pre-computed hashCode(), per the contract in the List interface.
     */
    private final int hash;

    /**
     * Sole Constructor.
     *
     * @param location will be the location() of this list.
     * @param elements will be the elements in this list.
     */
    private SList (final SourceLocation location,
                   final Iterator<? extends Sexpr<?>> elements)
    {
        this.location = Objects.requireNonNull(location);
        this.elements = new ArrayList<>();
        elements.forEachRemaining(x -> this.elements.add(Objects.requireNonNull(x, "A symbolic-list cannot contain null.")));
        this.elements.trimToSize();
        this.treeSize = this.elements.stream().mapToInt(x -> x.treeSize()).sum() + 1;
        this.treeHeight = this.elements.stream().mapToInt(x -> x.treeHeight()).max().orElse(0) + 1;
        this.treeLeafCount = this.elements.stream().mapToInt(x -> x.treeLeafCount()).sum();
        this.hash = 31 * this.elements.stream().mapToInt(x -> x.hashCode()).sum();
    }

    /**
     * Factory Method.
     *
     * @param elements will be the elements in this list.
     * @return the new symbolic-list.
     */
    public static SList of (final Sexpr<?>... elements)
    {
        return new SList(SourceLocation.DEFAULT, Arrays.asList(elements).iterator());
    }

    /**
     * Factory Method.
     *
     * @param location will be the location() of this list.
     * @param elements will be the elements in this list.
     * @return the new symbolic-list.
     */
    public static SList of (final SourceLocation location,
                            final Sexpr<?>... elements)
    {
        return new SList(location, Arrays.asList(elements).iterator());
    }

    /**
     * Factory Method.
     *
     * @param list contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final Iterable<? extends Sexpr<?>> list)
    {
        return new SList(SourceLocation.DEFAULT, list.iterator());
    }

    /**
     * Factory Method.
     *
     * @param location will be the location() of this list.
     * @param list contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final SourceLocation location,
                                final Iterable<? extends Sexpr<?>> list)
    {
        return new SList(location, list.iterator());
    }

    /**
     * Factory Method.
     *
     * @param stream contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final Stream<? extends Sexpr<?>> stream)
    {
        return new SList(SourceLocation.DEFAULT, stream.iterator());
    }

    /**
     * Factory Method.
     *
     * @param location will be the location() of this list.
     * @param stream contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final SourceLocation location,
                                final Stream<? extends Sexpr<?>> stream)
    {
        return new SList(location, stream.iterator());
    }

    /**
     * Factory Method.
     *
     * @param stream contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final Iterator<? extends Sexpr<?>> stream)
    {
        return new SList(SourceLocation.DEFAULT, stream);
    }

    /**
     * Factory Method.
     *
     * @param location will be the location() of this list.
     * @param stream contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final SourceLocation location,
                                final Iterator<? extends Sexpr<?>> stream)
    {
        return new SList(location, stream);
    }

    /**
     * This method creates a new two-dimensional list from the given map.
     *
     * <p>
     * The result will be a list of lists.
     * Each inner list will correspond to a single entry in the map.
     * Each inner list will have exactly two elements.
     * The first element is the key from the map entry.
     * The second element is the value from the map entry.
     * </p>
     *
     * @param location will be the location() of this list.
     * @param map contains the entries to add to the list.
     * @return the new symbolic-list.
     */
    public static SList fromMap (final SourceLocation location,
                                 final Map<? extends Sexpr<?>, ? extends Sexpr<?>> map)
    {
        return copyOf(location, createMap(location, map));
    }

    /**
     * This method creates a new two-dimensional list from the given map.
     *
     * <p>
     * The result will be a list of lists.
     * Each inner list will correspond to a single entry in the map.
     * Each inner list will have exactly three elements.
     * The first element will be the key from the map entry.
     * the second element will be the given separator.
     * The third element will be the value from the map entry.
     * </p>
     *
     * @param location will be the location() of this list.
     * @param map contains the entries to add to the list.
     * @param separator will be used as the key-value separator.
     * @return the new symbolic-list.
     */
    public static SList fromMap (final SourceLocation location,
                                 final Map<? extends Sexpr<?>, ? extends Sexpr<?>> map,
                                 final Sexpr<?> separator)
    {
        return copyOf(location, createMap(location, map, separator));
    }

    private static List<Sexpr<?>> createMap (final SourceLocation location,
                                             final Map<? extends Sexpr<?>, ? extends Sexpr<?>> map)
    {
        final List<Sexpr<?>> outer = new LinkedList<>();
        map.forEach((x, y) -> outer.add(SList.of(location, x, y)));
        return outer;
    }

    private static List<Sexpr<?>> createMap (final SourceLocation location,
                                             final Map<? extends Sexpr<?>, ? extends Sexpr<?>> map,
                                             final Sexpr<?> separator)
    {
        final List<Sexpr<?>> outer = new LinkedList<>();
        map.forEach((x, y) -> outer.add(SList.of(location, x, separator, y)));
        return outer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sexpr get (final int i)
    {
        return elements.get(i);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAtom ()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isList ()
    {
        return true;
    }

    /**
     * This method retrieves the first element of this list.
     *
     * @return the first element, or null, if the list is empty.
     */
    public Sexpr first ()
    {
        return isEmpty() ? null : get(0);
    }

    /**
     * This method retrieves the last element of this list.
     *
     * @return the last element, or null, if the list is empty.
     */
    public Sexpr last ()
    {
        return isEmpty() ? null : get(size() - 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SourceLocation location ()
    {
        return location;
    }

    /**
     * This method obtains a mutator that can be used to
     * non-destructively modify the tree rooted at this node.
     *
     * @return the mutator.
     */
    public Mutator mutator ()
    {
        return new Mutator(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size ()
    {
        return elements.size();
    }

    /**
     * This method retrieves the sub-list containing
     * all of the elements of this list,
     * except for the first element.
     *
     * @return the tail sub-list.
     */
    public SList tail ()
    {
        return isEmpty() ? this : copyOf(location, subList(1, size()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean bfs (final Predicate<Sexpr<?>> condition)
    {
        final Queue<Sexpr> queue = new LinkedList<>();

        queue.add(this);

        while (queue.isEmpty() == false)
        {
            final Sexpr element = queue.remove();

            if (condition.test(element))
            {
                return true;
            }
            else if (element.isList())
            {
                queue.addAll((SList) element);
            }
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean dfs (final Predicate<Sexpr<?>> condition)
    {
        return preorder(condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preorder (final Predicate<Sexpr<?>> condition)
    {
        return condition.test(this) || stream().anyMatch(x -> x.dfs(condition));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean postorder (final Predicate<Sexpr<?>> condition)
    {
        return stream().anyMatch(x -> x.postorder(condition)) || condition.test(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void traverse (final Consumer<Sexpr<?>> before,
                          final Consumer<Sexpr<?>> after)
    {
        before.accept(this);
        stream().forEach(x -> x.traverse(before, after));
        after.accept(this);
    }

    /**
     * This method retrieves this value, as a map.
     *
     * <p>
     * This list must be a list of sub-lists.
     * Each sub-list will correspond to an entry in the new map.
     * The first element in the sub-list will be used as a map-key.
     * The last element in the sub-list will be used as a map-value.
     * If the same map-key occurs multiple times, the last entry will prevail.
     * </p>
     *
     * @return the immutable value, if possible.
     */
    public Optional<Map<Sexpr<?>, Sexpr<?>>> asMap ()
    {
        final Map<Sexpr<?>, Sexpr<?>> map = new TreeMap<>();

        for (Sexpr<?> element : this)
        {
            if (element.isList() == false)
            {
                return Optional.empty();
            }
            else if (((SList) element).size() < 2)
            {
                return Optional.empty();
            }
            else
            {
                final SList entry = (SList) element;
                map.put(entry.first(), entry.last());
            }
        }

        return Optional.of(Collections.unmodifiableMap(map));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int treeHeight ()
    {
        return treeHeight;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int treeLeafCount ()
    {
        return treeLeafCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int treeSize ()
    {
        return treeSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final boolean equals (final Object other)
    {
        if (other == this)
        {
            return true;
        }
        else if (other == null)
        {
            return false;
        }
        else if (hash != other.hashCode())
        {
            return false;
        }
        else if (other instanceof SList == false)
        {
            return false;
        }
        else
        {
            final SList otherList = (SList) other;
            final boolean result = elements.equals(otherList.elements);
            return result;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode ()
    {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        final StringBuilder str = new StringBuilder();
        str.append('(');
        IntStream.range(0, size() - 1).forEach(i -> str.append(get(i)).append(" "));
        str.append(isEmpty() ? "" : last());
        str.append(')');
        return str.toString();
    }

    /**
     * This method converts the textual representation of a SList
     * to an actual corresponding SList object.
     *
     * <p>
     * This method inserts an implicit symbolic-list into the input.
     * For example, the input "(1 2) (3 4)" will produce a SList equivalent to "((1 2) (3 4))".
     * </p>
     *
     * @param location is a human-readable string indicating where the input came form.
     * @param input is the input to parse.
     * @return the resulting symbolic-list.
     */
    public static SList parse (final String location,
                               final String input)
    {
        final SList root = Parser.parse(location, input);
        return root;
    }

    /**
     * This method converts the textual representation of a SList
     * to an actual corresponding SList object.
     *
     * <p>
     * This method inserts an implicit symbolic-list into the input.
     * For example, the input "(1 2) (3 4)" will produce a SList equivalent to "((1 2) (3 4))".
     * </p>
     *
     * @param input is the input to parse.
     * @return the resulting symbolic-list.
     */
    public static SList parse (final String input)
    {
        final SList root = Parser.parse("null", input);
        return root;
    }

    /**
     * This method converts the textual representation of a resource file
     * to an actual corresponding SList object.
     *
     * <p>
     * See method parse(*) for more parsing details.
     * </p>
     *
     * @param path is the path to the resource file.
     * @return the new symbolic-list.
     * @throws IOException if the resource cannot be read.
     */
    public static SList parseResource (final String path)
            throws IOException
    {
        final StringBuilder text = new StringBuilder();

        try (InputStream in = SList.class.getResourceAsStream(path);
             BufferedInputStream bin = new BufferedInputStream(in);
             Scanner scanner = new Scanner(bin))
        {
            while (scanner.hasNextLine())
            {
                text.append(scanner.nextLine()).append('\n');
            }
        }
        catch (IOException | RuntimeException ex)
        {
            throw ex;
        }

        return parse(path, text.toString());
    }

    /**
     * This method converts the textual representation of a text file
     * to an actual corresponding SList object.
     *
     * <p>
     * See method parse(*) for more parsing details.
     * </p>
     *
     * @param file is the path to the file.
     * @return the new symbolic-list.
     * @throws IOException if the resource cannot be read.
     */
    public static SList parseFile (final File file)
            throws IOException
    {
        final String source = file.toString();
        final StringBuilder content = new StringBuilder();
        Files.readAllLines(file.toPath(), Charset.forName("UTF-8"))
                .forEach(line -> content.append(line).append('\n'));
        return parse(source, content.toString());
    }

    /**
     * An instance of this interface simplifies the modification
     * of symbolic-expressions, since they are immutable.
     */
    public final class Mutator
    {
        /**
         * The mutator in this field relates to a node() closer the root of the tree.
         * If this field is null, then this mutator relates to the root of the tree.
         */
        private final Mutator below;

        /**
         * This is the node in the tree that this mutator relates to.
         */
        private final Sexpr<?> node;

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
        public Mutator (final SList node)
        {
            this(null, node, 0);
        }

        private Mutator (final Mutator below,
                             final Sexpr<?> node,
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
        public SList append (final Sexpr<?> value)
        {
            if (node.isList())
            {
                final LinkedList<Sexpr<?>> elements = new LinkedList<>(node.asList());
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
        public SList prepend (final Sexpr<?> value)
        {
            if (node.isList())
            {
                final LinkedList<Sexpr<?>> elements = new LinkedList<>(node.asList());
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
        public SList set (final Sexpr<?> value)
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
        public Mutator first ()
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
        public Mutator last ()
        {
            if (node.isList())
            {
                return get(node.asList().size() - 1);
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
        public Mutator get (final int index)
        {
            if (node.isList())
            {
                final Sexpr element = node.asList().get(index);
                final Mutator step = new Mutator(this, element, index);
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
        private SList rebuild (final Sexpr<?> value,
                               final int position)
        {
            /**
             * Replace the element at the given position with the new value.
             * Since the list is immutable, we create a modified copy.
             */
            final List<Sexpr<?>> elements = new ArrayList<>(node.asList());
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
}
