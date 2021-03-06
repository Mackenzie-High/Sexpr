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
import com.mackenziehigh.sexpr.exceptions.ParsingFailedException;
import java.io.IOException;
import java.io.StringReader;

/**
 * Converts a <code>String</code> to a <code>SList</code>.
 */
public final class Parser
{
    public static SList parse (final String source,
                               final String text)
            throws ParsingFailedException
    {
        final Lexer lexer = new Lexer(new StringReader(text));
        lexer.stack.setSource(source);

        try
        {
            /**
             * Keep invoking the lexer, until it reaches the End-Of-Input.
             * The lexer has embedded actions that will execute upon token matches.
             * The actions manipulate an internal stack of tokens.
             */
            lexer.stack.parenOpen(0, 0);
            while (Lexer.YYEOF != lexer.yylex())
            {
                // Pass
            }
            lexer.stack.parenClose();

            /**
             * If the stack is not empty, then the input is missing a closing parenthesis.
             */
            final Sexpr<?> top = lexer.stack.top();
            if (lexer.stack.size() > 1)
            {
                throw new ParsingFailedException(top.location());
            }

            /**
             * Parsing was successful.
             */
            final SList root = (SList) top;
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
    {
        final SList list = SList.parse("1 (2");
        System.out.println("X = " + list);
    }
}
