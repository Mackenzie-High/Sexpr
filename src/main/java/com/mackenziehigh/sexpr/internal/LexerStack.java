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

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SourceLocation;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Used by the Lexer in order to transform <code>String</code>s into <code>SList</code>s.
 */
final class LexerStack
{
    private final Sexpr MARKER = SAtom.fromString("BEGIN");

    private final Deque<Sexpr<?>> stack = new ArrayDeque<>();

    private final Deque<SourceLocation> locations = new ArrayDeque<>();

    private volatile String source;

    public void setSource (final String source)
    {
        this.source = source;
    }

    public void parenOpen (final int line,
                           final int column)
    {
        stack.push(MARKER);
        locations.push(new SourceLocation(source, line, column));
    }

    public void parenClose ()
    {
        final LinkedList<Sexpr> elements = new LinkedList<>();

        while (stack.size() > 0 && stack.peek() != MARKER) // identity equality
        {
            elements.addFirst(stack.pop());
        }

        if (stack.isEmpty())
        {
            throw new IllegalStateException("Unbalanced call to end()");
        }
        else
        {
            stack.pop();
        }
        final SourceLocation location = locations.pop();
        final SList list = SList.copyOf(location, elements);
        stack.push(list);
    }

    public Sexpr<?> top ()
    {
        final Sexpr<?> result = stack.isEmpty() ? null : stack.peek();
        return result;
    }

    public int size ()
    {
        return stack.size();
    }

    public void pushAtomForm1 (final String yytext,
                               final int yyline,
                               final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(2, length - 1);
        final SourceLocation location = new SourceLocation(source, yyline, yycolum);
        final SAtom atom = SAtom.fromString(location, text);
        stack.push(atom);
    }

    public void pushAtomForm2 (final String yytext,
                               final int yyline,
                               final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(2, length - 1);
        final SourceLocation location = new SourceLocation(source, yyline, yycolum);
        final SAtom atom = SAtom.fromString(location, text);
        stack.push(atom);
    }

    public void pushAtomForm3 (final String yytext,
                               final int yyline,
                               final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(1, length - 1);
        final String expanded = new String(Escaper.instance.expand(text));
        final SourceLocation location = new SourceLocation(source, yyline, yycolum);
        final SAtom atom = SAtom.fromString(location, expanded);
        stack.push(atom);
    }

    public void pushAtomForm4 (final String yytext,
                               final int yyline,
                               final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(1, length - 1);
        final String expanded = new String(Escaper.instance.expand(text));
        final SourceLocation location = new SourceLocation(source, yyline, yycolum);
        final SAtom atom = SAtom.fromString(location, expanded);
        stack.push(atom);
    }

    public void pushAtomForm5 (final String yytext,
                               final int yyline,
                               final int yycolum)
    {
        final SourceLocation location = new SourceLocation(source, yyline, yycolum);
        final SAtom atom = SAtom.fromString(location, yytext);
        stack.push(atom);
    }
}
