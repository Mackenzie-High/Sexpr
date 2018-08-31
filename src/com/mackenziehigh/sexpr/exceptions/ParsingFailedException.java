package com.mackenziehigh.sexpr.exceptions;

import com.mackenziehigh.sexpr.SourceLocation;
import java.util.Objects;

/**
 * TODO: Should this be a checked exception.
 */
public class ParsingFailedException
        extends RuntimeException
{
    private final SourceLocation location;

    public ParsingFailedException (final SourceLocation location)
    {
        this.location = Objects.requireNonNull(location);
    }

    public SourceLocation location ()
    {
        return location;
    }

    @Override
    public String toString ()
    {
        return location.toString();
    }
}
