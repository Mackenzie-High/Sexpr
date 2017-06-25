package com.mackenziehigh.sexpr;

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
import java.util.TreeMap;
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
        extends AbstractList<Sexpr>
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
                   final Iterator<? extends Sexpr> elements)
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
    public static SList of (final Sexpr... elements)
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
                            final Sexpr... elements)
    {
        return new SList(location, Arrays.asList(elements).iterator());
    }

    /**
     * Factory Method.
     *
     * @param list contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final Iterable<? extends Sexpr> list)
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
                                final Iterable<? extends Sexpr> list)
    {
        return new SList(location, list.iterator());
    }

    /**
     * Factory Method.
     *
     * @param stream contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final Stream<? extends Sexpr> stream)
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
                                final Stream<? extends Sexpr> stream)
    {
        return new SList(location, stream.iterator());
    }

    /**
     * Factory Method.
     *
     * @param stream contains the elements for the new list.
     * @return the new symbolic-list.
     */
    public static SList copyOf (final Iterator<? extends Sexpr> stream)
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
                                final Iterator<? extends Sexpr> stream)
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
                                 final Map<? extends Sexpr, ? extends Sexpr> map)
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
                                 final Map<? extends Sexpr, ? extends Sexpr> map,
                                 final Sexpr separator)
    {
        return copyOf(location, createMap(location, map, separator));
    }

    private static List<Sexpr> createMap (final SourceLocation location,
                                          final Map<? extends Sexpr, ? extends Sexpr> map)
    {
        final List<Sexpr> outer = new LinkedList<>();
        map.forEach((x, y) -> outer.add(SList.of(location, x, y)));
        return outer;
    }

    private static List<Sexpr> createMap (final SourceLocation location,
                                          final Map<? extends Sexpr, ? extends Sexpr> map,
                                          final Sexpr separator)
    {
        final List<Sexpr> outer = new LinkedList<>();
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
     * {@inheritDoc}
     */
    @Override
    public SexprMutator<SList> mutator ()
    {
        return new SexprMutator(this);
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
    public boolean bfs (final Predicate<Sexpr> condition)
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
    public boolean dfs (final Predicate<Sexpr> condition)
    {
        return preorder(condition);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean preorder (final Predicate<Sexpr> condition)
    {
        return condition.test(this) || stream().anyMatch(x -> x.dfs(condition));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean postorder (final Predicate<Sexpr> condition)
    {
        return stream().anyMatch(x -> x.postorder(condition)) || condition.test(this);
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
    public Optional<Map<Sexpr, Sexpr>> asMap ()
    {
        final Map<Sexpr, Sexpr> map = new TreeMap<>();

        for (Sexpr element : this)
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
        final Parser p = new Parser(Objects.requireNonNull(location));
        return p.parse(Objects.requireNonNull(input));
    }
}
