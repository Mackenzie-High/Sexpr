package com.mackenziehigh.sexpr;

/**
 * An instance of this interface indicate the location
 * of a symbolic-expression within a string of text.
 */
public interface SourceLocation
{
    /**
     * This method retrieves the name of the source.
     * This is usually the path to a file.
     *
     * @return the identifier of the source.
     */
    public default String source ()
    {
        return "N/A";
    }

    /**
     * This method retrieves the line-number
     * where the symbolic-expression starts.
     *
     * @return the line-number.
     */
    public default int line ()
    {
        return 0;
    }

    /**
     * This method retrieves the column-number
     * where the symbolic-expression starts.
     *
     * @return the line-number.
     */
    public default int column ()
    {
        return 0;
    }

    /**
     * This method returns a human-readable message
     * based on this location.
     *
     * @return this object in human readable form.
     */
    public default String message ()
    {
        return String.format("Line: %d, Column: %d, Source: %s", line(), column(), source());
    }
}
