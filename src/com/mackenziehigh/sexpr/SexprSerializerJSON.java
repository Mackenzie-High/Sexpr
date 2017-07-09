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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * An instance of this class can serialize symbolic-expressions
 * to/from a textual representation using using JSON.
 */
public final class SexprSerializerJSON
        implements SexprSerializer<String>
{
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(Sexpr.class, new SexprAdapter().nullSafe())
            .registerTypeAdapter(SAtom.class, new SexprAdapter().nullSafe())
            .registerTypeAdapter(SList.class, new SexprAdapter().nullSafe())
            .create();

    /**
     * This method converts a symbolic-expression to standardized JSON.
     *
     * @param expression is the symbolic-expression to convert.
     * @return the JSON representation of the expression.
     */
    @Override
    public String encode (final Sexpr expression)
    {
        final String json = gson.toJson(expression);
        return json;
    }

    /**
     * This method converts a string of JSON text to a symbolic-expression.
     *
     * <p>
     * The JSON text must be formatted according to
     * the contract of the convertToJSON(*) method.
     * </p>
     *
     * @param text is the JSON text to parse.
     * @return the symbolic-expression derived from the text.
     */
    @Override
    public Sexpr decode (final String text)
    {
        final Sexpr result = gson.fromJson(text, Sexpr.class);
        return result;
    }

    private final class SexprAdapter
            extends TypeAdapter<Sexpr>
    {
        @Override
        public Sexpr read (final JsonReader reader)
                throws IOException
        {
            switch (reader.peek())
            {
                case BEGIN_ARRAY:
                    reader.beginArray();
                    final List<Sexpr> elements = new LinkedList<>();
                    while (reader.peek() != JsonToken.END_ARRAY)
                    {
                        elements.add(read(reader));
                    }
                    reader.endArray();
                    return SList.copyOf(elements);
                case BOOLEAN:
                    return new SAtom(reader.nextBoolean());
                case NUMBER:
                    return new SAtom(reader.nextString());
                case STRING:
                    return new SAtom(reader.nextString());
                default:
                    throw new UnsupportedOperationException();
            }
        }

        @Override
        public void write (final JsonWriter writer,
                           final Sexpr value)
                throws IOException
        {
            if (value.isList())
            {
                writer.beginArray();
                for (int i = 0; i < value.toList().size(); i++)
                {
                    write(writer, value.toList().get(i));
                }
                writer.endArray();
            }
            else if (value.toAtom().asLong().isPresent())
            {
                writer.value(value.toAtom().asLong().get());
            }
            else if (value.toAtom().asDouble().isPresent())
            {
                writer.value(value.toAtom().asDouble().get());
            }
            else if (value.toAtom().asBoolean().isPresent())
            {
                writer.value(value.toAtom().asBoolean().get());
            }
            else
            {
                writer.value(value.toAtom().content());
            }
        }

    }
}
