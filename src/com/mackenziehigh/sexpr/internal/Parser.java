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
package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SourceLocation;
import high.mackenzie.snowflake.LinesAndColumns;
import high.mackenzie.snowflake.NewlineStyles;
import high.mackenzie.snowflake.ParserOutput;
import java.util.Objects;

/**
 * Symbolic Expression Parser.
 */
public final class Parser
{
    private final String source;

    /**
     * Sole Constructor.
     *
     * @param source is a human -readable string indicating
     * the source of the input. For example, this may be a file-path.
     */
    public Parser (final String source)
    {
        this.source = Objects.requireNonNull(source);
    }

    /**
     * This method converts the textual representation
     * of a symbolic-expression to the equivalent object
     * representation of the symbolic-expression.
     *
     * @param input is symbolic-expression in textual form.
     * @return the symbolic-expression.
     */
    public SList parse (final String input)
    {
        final GeneratedParser parser = new GeneratedParser();
        final ParserOutput output = parser.parse(input);
        final LinesAndColumns locator = new LinesAndColumns(input.toCharArray(), NewlineStyles.fromSystem());

        if (output.success() == false)
        {
            final int line = locator.lineNumbers()[output.lengthOfConsumption()];
            final int column = locator.columnNumbers()[output.lengthOfConsumption()];
            final SourceLocation location = new SourceLocation(source, line, column);
            throw new IllegalArgumentException("Parsing Failed At " + location.message());
        }

        final SourceLocation location = new SourceLocation(source, 1, 1);
        final Visitor visitor = new Visitor(source, input);
        visitor.visit(output.parseTree());
        final SList result = (SList) SList.copyOf(location, visitor.stack.stream().filter(x -> x instanceof Sexpr).map(x -> (Sexpr) x));

        return result;
    }
}
