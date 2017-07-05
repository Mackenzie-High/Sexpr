package com.mackenziehigh.sexpr.internal.schema;

import com.mackenziehigh.sexpr.SourceLocation;
import high.mackenzie.snowflake.LinesAndColumns;
import high.mackenzie.snowflake.NewlineStyles;
import high.mackenzie.snowflake.ParserOutput;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Schema Parser.
 */
public final class SchemaParser
{
    /**
     * Use this method to convert the textual representation
     * of a schema to the object representation thereof.
     *
     * @param source is a human-readable string describing where the schema is from.
     * @param input is the textual representation of the schema.
     * @return the object representation of the input schema.
     * @throws IllegalArgumentException if parsing fails.
     */
    public Schema parse (final String source,
                         final String input)
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

        final Visitor visitor = new Visitor();
        visitor.visit(output.parseTree());
        final Schema schema = visitor.g.build();

        return schema;
    }

    /**
     * Use this method to read a file and convert it to a schema object.
     *
     * @param file is a text file containing a schema.
     * @return the new schema object.
     * @throws IOException if the file cannot be read.
     * @throws IllegalArgumentException if parsing fails.
     */
    public Schema parse (final File file)
            throws IOException
    {
        final StringBuilder text = new StringBuilder();
        Files.readAllLines(file.toPath()).forEach(line -> text.append(line).append("\n"));
        final String source = file.toString();
        final String input = text.toString();
        return parse(source, input);
    }
}
