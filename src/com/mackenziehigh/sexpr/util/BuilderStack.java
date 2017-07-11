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

import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import java.util.LinkedList;

/**
 * This is a stack designed to make the construction of symbolic-expressions
 * easier when creating them via tree traversals, such as those performed
 * by parser-generators.
 */
public final class BuilderStack
        extends LinkedList<Sexpr>
{
    private final Sexpr MARKER = new SAtom("BEGIN");

    /**
     * Use this method to mark the beginning of a symbolic-list.
     *
     * <p>
     * This method will push a special marker onto the stack.
     * The marker will be used by the end() method in order
     * to determine the elements to place into a new list.
     * </p>
     */
    public void begin ()
    {
        push(MARKER);
    }

    /**
     * Use this method to mark the end of a symbolic-list.
     *
     * <p>
     * This method will pop elements off of this stack,
     * add them to a new symbolic-list, and then push
     * the new symbolic-list onto the stack.
     * </p>
     */
    public void end ()
    {
        final LinkedList<Sexpr> elements = new LinkedList<>();

        while (size() > 0 && peek() != MARKER) // identity equality
        {
            elements.addFirst(pop());
        }

        if (isEmpty())
        {
            throw new IllegalStateException("Unbalanced call to end()");
        }
        else
        {
            pop();
        }
        final SList list = SList.copyOf(elements);
        push(list);
    }
}
