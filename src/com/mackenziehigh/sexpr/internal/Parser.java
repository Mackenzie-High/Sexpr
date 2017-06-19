package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.SexprFactory;
import com.mackenziehigh.sexpr.SourceLocation;
import high.mackenzie.snowflake.LinesAndColumns;
import high.mackenzie.snowflake.NewlineStyles;
import high.mackenzie.snowflake.ParserOutput;
import java.util.Objects;

/**
 * Symbolic Expression Parser.
 */
final class Parser
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
            final SourceLocation location = new SourceLocation()
            {
                @Override
                public int column ()
                {
                    return locator.columnNumbers()[output.lengthOfConsumption()];
                }

                @Override
                public int line ()
                {
                    return locator.lineNumbers()[output.lengthOfConsumption()];
                }

                @Override
                public String source ()
                {
                    return source;
                }

            };

            throw new SexprFactory.ParsingFailedException(location);
        }

        final Visitor visitor = new Visitor(source, input);
        visitor.visit(output.parseTree());
        final SList result = (SList) visitor.stack.pop();

        return result;
    }
}
