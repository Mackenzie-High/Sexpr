package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.Objects;

/**
 * Partially Customizable Implementation of SAtom.
 */
class BaseAtom
        implements SAtom
{
    private final String text;

    private final int hash;

    public BaseAtom (final String text)
    {
        this.text = Objects.requireNonNull(text);
        this.hash = text.hashCode();
    }

    @Override
    public final String content ()
    {
        return text;
    }

    @Override
    public final boolean equals (final Object other)
    {
        if (hash != other.hashCode())
        {
            return false;
        }
        else if (other instanceof SAtom == false)
        {
            return false;
        }
        else
        {
            return compareTo((Sexpr) other) == 0;
        }
    }

    @Override
    public final int hashCode ()
    {
        return hash;
    }

    @Override
    public final String toString ()
    {
        final String symbol = content();
        final String string = "@'" + escaped() + "'";
        final String result = escaped().equals(content()) ? symbol : string;
        return result;
    }

}
