package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 * Implementation of SList.
 */
class BaseList
        extends AbstractList<Sexpr>
        implements SList
{
    private final ArrayList<Sexpr> elements;

    public BaseList (final Iterable<Sexpr> elements)
    {
        this.elements = new ArrayList<>();
        elements.forEach(x -> this.elements.add(x));
        this.elements.trimToSize();
    }

    @Override
    public Sexpr get (final int i)
    {
        return elements.get(i);
    }

    @Override
    public int size ()
    {
        return elements.size();
    }

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
}
