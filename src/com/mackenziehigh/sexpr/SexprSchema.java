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

import com.mackenziehigh.sexpr.internal.schema.Schema;
import com.mackenziehigh.sexpr.internal.schema.SchemaParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An instance of this class can be used to determine
 * whether a symbolic-expression matches a schema (pattern).
 */
public final class SexprSchema
{
    /**
     * This function can be used as a default failure-handler that will
     * print the approximate location of match failures to standard-output.
     */
    public static final Consumer<Optional<Sexpr>> PRINT_FAILURE = x ->
    {
        if (x.isPresent())
        {
            System.out.println("Match Failed At " + x.get().location().message());
        }
        else
        {
            System.out.println("Match Totally Failed");
        }
    };

    private final Schema schema;

    /**
     * Sole Constructor.
     *
     * @param schema is the schema that symbolic-expressions must match.
     */
    private SexprSchema (final Schema schema)
    {
        this.schema = Objects.requireNonNull(schema, "schema");
    }

    /**
     * This method configures the matcher to use a given schema.
     *
     * @param source is a human-readable indicating where the schema is from.
     * @param schema is the schema for the matcher to use.
     * @return this.
     */
    public static Builder fromString (final String source,
                                      final String schema)
    {
        final SchemaParser parser = new SchemaParser();
        final Schema pattern = parser.parse(source, schema);
        return new Builder(pattern);
    }

    /**
     * This method configures the matcher to use a schema from a given file.
     *
     * @param file contains the schema for the matcher to use.
     * @return this.
     * @throws java.io.IOException if the file cannot be read.
     */
    public static Builder fromFile (final File file)
            throws IOException
    {
        final String source = file.toString();
        final StringBuilder content = new StringBuilder();
        Files.readAllLines(file.toPath(), Charset.forName("UTF-8"))
                .forEach(line -> content.append(line).append('\n'));
        return fromString(source, content.toString());
    }

    /**
     * This method configures the matcher to use a schema from a given resource.
     *
     * @param path identifies the resource that contains the schema.
     * @return this.
     * @throws java.io.IOException if the resource cannot be read.
     */
    public static Builder fromResource (final String path)
            throws IOException
    {
        final StringBuilder schema = new StringBuilder();

        try (InputStream in = SexprSchema.class.getResourceAsStream(path);
             BufferedInputStream bin = new BufferedInputStream(in);
             Scanner scanner = new Scanner(bin))
        {
            while (scanner.hasNextLine())
            {
                schema.append(scanner.nextLine()).append('\n');
            }
        }
        catch (IOException | RuntimeException ex)
        {
            throw ex;
        }

        return fromString(path, schema.toString());
    }

    /**
     * This method determines whether the schema
     * matches a given symbolic-expression.
     *
     * <p>
     * If the match is successful, then the match-tree
     * will be traversed once for each translation-pass
     * that was defined previously. Upon encountering
     * the successful match of a rule (R) during a
     * translation-pass, the before-actions of (R)
     * will be executed, then the subordinate matches
     * will be visited and their actions will be executed,
     * and then the after-actions of (R) will be executed.
     * </p>
     *
     * <p>
     * If the match is unsuccessful, then the failure-handler function
     * will be invoked passing-in the highest numbered node that
     * was successfully matched. If no node was successfully matched,
     * then an empty optional will be passed-in to the function.
     * Conceptually, the highest numbered node that is successfully
     * matched will be close to the site of failure; therefore,
     * the location of the node is useful for generating human
     * readable error-messages indication the approximate
     * location of the failure-to-match. Nodes are numbered
     * in accordance with a post-order transversal of the tree.
     * </p>
     *
     * @param tree is the symbolic-expression that this schema may match.
     * @return true, iff the match was successful.
     */
    public boolean match (final Sexpr tree)
    {
        Objects.requireNonNull(tree, "tree");
        final boolean match = schema.match(tree);
        return match;
    }

    /**
     * Use an instance of this class to create a Matcher object.
     */
    public static final class Builder
    {
        /**
         * This is the instance that is being constructed.
         */
        private SexprSchema instance;

        /**
         * Sole Constructor.
         *
         * @param schema is the schema that the matcher will use.
         */
        private Builder (final Schema schema)
        {
            Objects.requireNonNull(schema, "schema");
            this.instance = new SexprSchema(schema);
        }

        /**
         * Use this method to define a predicate that can be used within a schema.
         *
         * <p>
         * Inside of the schema, the predicate must be referenced via a 'require' rule.
         * </p>
         *
         * @param name is the name that will be used to identify the predicate.
         * @param condition is the predicate itself.
         * @return this.
         * @throws IllegalArgumentException if the name already identifies a predicate.
         */
        public Builder condition (final String name,
                                  final Predicate<Sexpr> condition)
        {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(condition, "condition");
            Objects.requireNonNull(instance, "build() was already called.");
            instance.schema.defineCondition(name, condition);
            return this;
        }

        /**
         * Use this method to declare another translation pass.
         *
         * <p>
         * Call this method multiple times in order to declare multiple passes.
         * The translation passes will occur in the order of those invocations.
         * </p>
         *
         * @param name is the name of the new translation pass.
         * @return this.
         */
        public Builder pass (final String name)
        {
            Objects.requireNonNull(name, "name");
            instance.schema.definePass(name);
            return this;
        }

        /**
         * Use this method to define an action that will be executed before
         * matches of a named rule during a specific pass.
         *
         * @param pass is the name of the pass that this action applies to.
         * @param rule is the name of the rule that this action applies to.
         * @param action is the action itself.
         * @return this.
         * @throws IllegalArgumentException if the name already identifies a action.
         */
        public Builder before (final String pass,
                               final String rule,
                               final Consumer<Sexpr> action)
        {
            Objects.requireNonNull(pass, "pass");
            Objects.requireNonNull(rule, "rule");
            Objects.requireNonNull(action, "action");
            Objects.requireNonNull(instance, "build() was already called.");
            instance.schema.defineBeforeAction(pass, rule, action);
            return this;
        }

        /**
         * Use this method to define an action that will be executed after
         * matches of a named rule during a specific pass.
         *
         * @param pass is the name of the pass that this action applies to.
         * @param rule is the name of the rule that this action applies to.
         * @param action is the action itself.
         * @return this.
         * @throws IllegalArgumentException if the name already identifies a action.
         */
        public Builder after (final String pass,
                              final String rule,
                              final Consumer<Sexpr> action)
        {
            Objects.requireNonNull(pass, "pass");
            Objects.requireNonNull(rule, "rule");
            Objects.requireNonNull(action, "action");
            Objects.requireNonNull(instance, "build() was already called.");
            instance.schema.defineAfterAction(pass, rule, action);
            return this;
        }

        /**
         * This method specifies the action to perform
         * when a match attempt is unsuccessful.
         *
         *
         * @param handler will handle unsuccessful matches.
         * @return this.
         */
        public Builder setFailureHandler (final Consumer<Optional<Sexpr>> handler)
        {
            instance.schema.setFailureHandler(handler);
            return this;
        }

        /**
         * Use this method to obtain the new schema object.
         *
         * @return the new matcher.
         */
        public SexprSchema build ()
        {
            final SexprSchema result = instance;
            instance = null;
            return result;
        }
    }
}
