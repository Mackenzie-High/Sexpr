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

import java.util.Objects;

/**
 * An instance of this interface indicate the location
 * of a symbolic-expression within a string of text.
 */
public final class SourceLocation
{

    public static SourceLocation DEFAULT = new SourceLocation("null", 0, 0);

    private final String source;

    private final int line;

    private final int column;

    /**
     * Sole Constructor.
     *
     * @param source is the value that will be returnable by source().
     * @param line is the value that will be returnable by line().
     * @param column is the value that will be returnable by column().
     */
    public SourceLocation (final String source,
                           final int line,
                           final int column)
    {
        this.source = Objects.requireNonNull(source);
        this.line = line;
        this.column = column;
    }

    /**
     * This method retrieves the name of the source.
     * This is usually the path to a file.
     *
     * @return the identifier of the source.
     */
    public String source ()
    {
        return source;
    }

    /**
     * This method retrieves the line-number
     * where the symbolic-expression starts.
     *
     * @return the line-number.
     */
    public int line ()
    {
        return line;
    }

    /**
     * This method retrieves the column-number
     * where the symbolic-expression starts.
     *
     * @return the line-number.
     */
    public int column ()
    {
        return column;
    }

    /**
     * This method returns a human-readable message
     * based on this location.
     *
     * @return this object in human readable form.
     */
    public String message ()
    {
        return String.format("Line: %d, Column: %d, Source: %s", line(), column(), source());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return message();
    }
}
