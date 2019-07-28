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
package com.mackenziehigh.sexpr.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * Instances of this class simplify the creation of indented text.
 */
public final class StringPrinter
{
    private final int tabsize;

    private final StringBuilder text;

    private int indent = 0;

    /**
     * Sole Constructor.
     *
     * @param capacity is the expected capacity of the output string.
     * @param tabsize is the number of spaces per indentation level.
     */
    public StringPrinter (final int capacity,
                          final int tabsize)
    {
        if (capacity <= 0)
        {
            throw new IllegalArgumentException("capacity <= 0");
        }

        if (tabsize < 0)
        {
            throw new IllegalArgumentException("tabsize <= 0");
        }

        this.text = new StringBuilder(capacity);
        this.tabsize = tabsize;
    }

    /**
     * Use this method in order to make subsequent output more indented.
     *
     * @return this.
     */
    public StringPrinter increment ()
    {
        ++indent;
        return this;
    }

    /**
     * Use this method in order to make subsequent output less indented.
     *
     * @return this.
     */
    public StringPrinter decrement ()
    {
        --indent;
        return this;
    }

    /**
     * Use this method in order to make subsequent output more or less indented.
     *
     * @param value is the new indentation level.
     * @return this.
     */
    public StringPrinter setIndent (final int value)
    {
        if (value >= 0)
        {
            throw new IllegalArgumentException("value < 0");
        }

        this.indent = value;
        return this;
    }

    /**
     * Use this method in order to retrieve the current indentation level.
     *
     * @return the current level of indentation.
     */
    public int getIndent ()
    {
        return indent;
    }

    /**
     * Use this method to print a value to the output string.
     *
     * @param value is the value to print.
     * @return this.
     */
    public StringPrinter print (final Object value)
    {
        text.append(repeat(" ", indent * tabsize)).append(value);
        return this;
    }

    /**
     * Use this method to print a newline to the output string.
     *
     * @return this.
     */
    public StringPrinter println ()
    {
        text.append("\n");
        return this;
    }

    /**
     * Use this method to print a value and a newline to the output string.
     *
     * @param value is the value to print.
     * @return this.
     */
    public StringPrinter println (final Object value)
    {
        text.append(repeat(" ", indent * tabsize)).append(value).append("\n");
        return this;
    }

    /**
     * Use this method to print a formatted value to the output string.
     *
     * @param format is the format specifier string.
     * @param args are the values to substitute into the format string.
     * @return this.
     */
    public StringPrinter printf (final String format,
                                 final Object... args)
    {
        text.append(repeat(" ", indent * tabsize)).append(String.format(format, args));
        return this;
    }

    /**
     * Use this method to print a series of values to the output string, one per line.
     *
     * @param values is the value to print.
     * @return this.
     */
    public StringPrinter printlns (final Iterable<?> values)
    {
        for (Object line : values)
        {
            println(line);
        }
        return this;
    }

    /**
     * This method prints the string representation of an iterable.
     *
     * @param iterable is the iterable itself.
     * @param prefix is a string to prepend onto the result.
     * @param separator is the substring used to separate elements in the result.
     * @param suffix is a string to append onto the result.
     * @return this.
     */
    public StringPrinter printList (final Iterable<?> iterable,
                                    final String prefix,
                                    final String separator,
                                    final String suffix)
    {
        Objects.requireNonNull(iterable, "iterable");
        Objects.requireNonNull(prefix, "prefix");
        Objects.requireNonNull(separator, "separator");
        Objects.requireNonNull(suffix, "suffix");

        final List<Object> elements = new LinkedList<>();
        iterable.forEach(x -> elements.add(x));

        final StringBuilder result = new StringBuilder();

        result.append(prefix);
        {
            int count = 0;

            for (Object arg : elements)
            {
                ++count;

                result.append(arg);

                if (count < elements.size())
                {
                    result.append(separator);
                }
            }
        }
        result.append(suffix);

        print(result.toString());

        return this;
    }

    /**
     * Use this method to retrieve the output string.
     *
     * @return the generated output.
     */
    public String output ()
    {
        return text.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString ()
    {
        return output();
    }

    private String repeat (final String value,
                           final int count)
    {
        final StringBuilder result = new StringBuilder();
        IntStream.range(0, count).forEach(i -> result.append(value));
        return result.toString();
    }
}
