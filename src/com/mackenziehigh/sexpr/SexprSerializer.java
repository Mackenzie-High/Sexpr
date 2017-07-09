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
 * An instance of this interface can serialize symbolic-expressions.
 */
public interface SexprSerializer<T>
{
    /**
     * Use this method to encode a symbolic-expression.
     *
     * @param expression is the symbolic-expression to encode.
     * @return the encoded representation of the symbolic-expression.
     */
    public T encode (Sexpr expression);

    /**
     * Use this method to decode a symbolic-expression.
     *
     * @param input is the encoded representation of the symbolic-expression to decode.
     * @return the symbolic-expression corresponding to the give input.
     */
    public Sexpr decode (T input);
}
