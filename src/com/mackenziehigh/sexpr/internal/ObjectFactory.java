package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SexprFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Implementation of SexprFactory.
 */
public final class ObjectFactory
        implements SexprFactory
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Sexpr> deserialize (final byte[] bytes)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Sexpr> deserialize (final String json)
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAtom from (final boolean value)
    {
        final Optional<Boolean> result = Optional.of(value);

        return new BaseAtom(Boolean.toString(value))
        {
            @Override
            public Optional<Boolean> asBoolean ()
            {
                return result;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAtom from (final long value)
    {
        final Optional<Long> result = Optional.of(value);

        return new BaseAtom(Long.toString(value))
        {
            @Override
            public Optional<Long> asInteger ()
            {
                return result;
            }

        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAtom from (final double value)
    {
        final Optional<Double> result = Optional.of(value);

        return new BaseAtom(Double.toString(value))
        {
            @Override
            public Optional<Double> asFloat ()
            {
                return result;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAtom from (final String value)
    {
        return new BaseAtom(Objects.requireNonNull(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SAtom from (final Class value)
    {
        final Optional<Class> result = Optional.of(value);

        return new BaseAtom(value.getName())
        {
            @Override
            public Optional<Class> asClass ()
            {
                return result;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SList from (final Sexpr... elements)
    {
        return from(Arrays.asList(elements));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SList from (final Iterable<? extends Sexpr> elements)
    {
        final List<Sexpr> list = new ArrayList<>();
        elements.forEach(x -> list.add(Objects.requireNonNull(x)));
        return new BaseList(list);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SList from (final Map<? extends Sexpr, ? extends Sexpr> map)
    {
        final List<SList> outer = new LinkedList<>();
        map.forEach((x, y) -> outer.add(from(x, y)));
        return from(outer);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SList parse (final String location,
                        final String input)
    {
        final Parser p = new Parser(Objects.requireNonNull(location));
        return p.parse(Objects.requireNonNull(input));
    }
}
