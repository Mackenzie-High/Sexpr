package com.mackenziehigh.sexpr;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Symbolic Expression - List.
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
public interface SList
        extends Sexpr<SList>,
                List<Sexpr>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public default boolean isSList ()
    {
        return true;
    }

    /**
     * This method retrieves the first element of this list.
     *
     * @return the first element, or null, if the list is empty.
     */
    public default Sexpr first ()
    {
        return isEmpty() ? null : get(0);
    }

    /**
     * This method retrieves the last element of this list.
     *
     * @return the last element, or null, if the list is empty.
     */
    public default Sexpr last ()
    {
        return isEmpty() ? null : get(size() - 1);
    }

    /**
     * This method retrieves the sub-list containing
     * all of the elements of this list,
     * except for the first element.
     *
     * @return the tail sub-list.
     */
    public default SList tail ()
    {
        return isEmpty() ? this : (SList) subList(1, size());
    }

    /**
     * This method retrieves this value, as a map.
     *
     * <p>
     * This list must be a list of two-element sub-lists.
     * </p>
     *
     * @return the value, if possible.
     */
    public default Optional<Map<Sexpr, Sexpr>> asMap ()
    {
        final Map<Sexpr, Sexpr> map = new LinkedHashMap<>(size());

        for (Sexpr element : this)
        {
            if (element.isSList() == false)
            {
                return Optional.empty();
            }
            else if (((SList) element).size() != 2)
            {
                return Optional.empty();
            }
            else
            {
                final SList pair = (SList) element;
                map.put(pair.first(), pair.last());
            }
        }

        return Optional.of(Collections.unmodifiableMap(map));
    }

    /**
     * This method converts this object to its textual representation.
     *
     * @return the textual representation of this SList.
     */
    @Override
    public String toString ();
}
