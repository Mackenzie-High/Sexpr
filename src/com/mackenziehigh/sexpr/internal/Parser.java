package com.mackenziehigh.sexpr.internal;

import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.exceptions.ParsingFailedException;
import java.io.IOException;
import java.io.StringReader;

public final class Parser
{
    public static SList parse (final String source,
                               final String text)
            throws ParsingFailedException
    {
        final Lexer lexer = new Lexer(new StringReader(text));

        try
        {
            lexer.stack.begin();

            while (Lexer.YYEOF != lexer.yylex())
            {
                // Pass
            }

            lexer.stack.end();

            if (lexer.stack.size() > 1)
            {
                // TODO: Fix error location reporting with SLists
                final Sexpr<?> top = lexer.stack.peek();
                throw new ParsingFailedException(top.location());
            }

            final SList root = (SList) lexer.stack.pop();

            return root;
        }
        catch (IOException ex)
        {
            /**
             * This should never actually happen.
             */
            throw new RuntimeException(ex);
        }
    }

    public static void main (String[] args)
            throws ParsingFailedException
    {
        final Sexpr s = Parser.parse("", "(");

        System.out.println("Sexpr = " + s);
    }
}
