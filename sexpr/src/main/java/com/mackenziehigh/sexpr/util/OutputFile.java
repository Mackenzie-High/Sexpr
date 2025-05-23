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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Objects;

/**
 * An instance of this class represents a output text file.
 * This class is useful when using schemas to perform code generation.
 * The file will be encoded using (UTF-8).
 */
public final class OutputFile
{
    private final File file;

    private final StringPrinter content;

    /**
     * Sole Constructor.
     *
     * @param file is the value for file().
     * @param capacity is the expected capacity of the output string.
     * @param step is the number of spaces per indentation level.
     */
    public OutputFile (final File file,
                       final int capacity,
                       final int step)
    {
        Objects.requireNonNull(file, "file");
        this.file = file;
        this.content = new StringPrinter(capacity, step);
    }

    /**
     * This method retrieves the path to where the output file will be written.
     *
     * @return the path to the output file.
     */
    public File file ()
    {
        return file;
    }

    /**
     * This method retrieve the object used to generate the formatted file content.
     *
     * @return the content of the output file.
     */
    public StringPrinter content ()
    {
        return content;
    }

    /**
     * Use this method to write the output file to disk.
     *
     * @throws IOException if the file cannot be written.
     */
    public void write ()
            throws IOException
    {
        final byte[] text = content.toString().getBytes(Charset.forName("UTF-8"));
        Files.write(file.toPath(), text);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public String toString ()
    {
        return file.toString();
    }
}
