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

/**
 * An instance of this class can serialize symbolic-expressions
 * to/from a textual representation using using JSON.
 */
public final class SexprSerializerJSON
{
    /**
     * This method converts a symbolic-expression to standardized JSON.
     *
     * <p>
     * Each symbolic-list will be converted to a JSON Object.
     * The JSON Object will contain two entries.
     * Entry "location" will be a JSON Object describing the location().
     * Entry "elements" will be a JSON List containing child nodes.
     * </p>
     *
     * <p>
     * Each symbolic-atom will be converted to a JSON Object.
     * The JSON Object will contain two entries.
     * The "content" entry will contain the content() string of the atom.
     * The "location" entry will contain the location() of the atom.
     * </p>
     *
     * <p>
     * Each Source Location (X) will be converted to a JSON Object.
     * The JSON Object will contain three entries.
     * Entry "source" will be a string corresponding to the source() of X.
     * Entry "line" will be an integer corresponding to the line() of X.
     * Entry "column" will be an integer corresponding to the column() of X.
     * </p>
     *
     * @param expression is the symbolic-expression to convert.
     * @return the JSON representation of the expression.
     */
    public static String convertToJson (final Sexpr expression)
    {
        return null;
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
    public static Sexpr convertFromJson (final String text)
    {
        return null;
    }

}
