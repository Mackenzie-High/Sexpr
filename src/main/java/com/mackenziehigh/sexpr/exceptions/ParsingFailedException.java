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
package com.mackenziehigh.sexpr.exceptions;

import com.mackenziehigh.sexpr.SourceLocation;
import java.util.Objects;

/**
 * This type of exception indicates the detection of a syntax-error.
 */
public class ParsingFailedException
        extends RuntimeException
{
    private final SourceLocation location;

    public ParsingFailedException (final SourceLocation location)
    {
        super(String.format("Parsing Failed At Line: %d, Column: %d, Source: %s",
                            location.line(), location.column(), location.source()));
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
