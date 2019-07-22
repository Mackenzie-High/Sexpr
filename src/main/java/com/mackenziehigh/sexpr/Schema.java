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

import com.mackenziehigh.sexpr.internal.schema.Annotator;
import com.mackenziehigh.sexpr.internal.schema.InternalSchema;
import com.mackenziehigh.sexpr.internal.schema.SchemaParser;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * An instance of this class can be used to determine
 * whether a symbolic-expression matches a pattern.
 */
public final class Schema
{
    /**
     * Use an instance of this class to create a <code>Schema</code> object.
     */
    public static final class Builder
    {

        /**
         * This is the instance that is being constructed.
         */
        private final InternalSchema instance = new InternalSchema();

        private volatile Object built;

        /**
         * Sole Constructor.
         */
        private Builder ()
        {
            // Pass.
        }

        /**
         * This method parses a schema string.
         *
         * @param source is a human-readable indicating where the schema is from.
         * @param schema is the textual schema.
         * @return this.
         */
        public Builder fromString (final String source,
                                   final String schema)
        {
            SchemaParser.parse(instance, source, schema);
            return this;
        }

        public Builder fromFile (final File file)
                throws IOException
        {
            final String source = file.toString();
            final StringBuilder content = new StringBuilder();
            Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)
                    .forEach(line -> content.append(line).append('\n'));
            return fromString(source, content.toString());
        }

        public Builder fromResource (final String path)
                throws IOException
        {
            final StringBuilder schema = new StringBuilder();

            try (InputStream in = Schema.class.getResourceAsStream(path);
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
        public Builder condition (String name,
                                  Predicate<Sexpr> condition)
        {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(condition, "condition");
            Objects.requireNonNull(built, "build() was already called.");
            instance.defineCondition(name, condition);
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
        public Builder pass (String name)
        {
            Objects.requireNonNull(name, "name");
            instance.definePass(name);
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
        public Builder before (String pass,
                               String rule,
                               Consumer<Sexpr> action)
        {
            Objects.requireNonNull(pass, "pass");
            Objects.requireNonNull(rule, "rule");
            Objects.requireNonNull(action, "action");
            Objects.requireNonNull(built, "build() was already called.");
            instance.defineBeforeAction(pass, rule, action);
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
        public Builder after (String pass,
                              String rule,
                              Consumer<Sexpr> action)
        {
            Objects.requireNonNull(pass, "pass");
            Objects.requireNonNull(rule, "rule");
            Objects.requireNonNull(action, "action");
            Objects.requireNonNull(built, "build() was already called.");
            instance.defineAfterAction(pass, rule, action);
            return this;
        }

        /**
         * Given an object containing properly annotated methods,
         * define the conditions and actions defined therein.
         *
         * @param object contains condition and action definitions.
         * @return this.
         */
        public Builder defineViaAnnotations (Object object)
        {
            final Annotator annotator = new Annotator(this);
            annotator.defineViaReflection(object);
            return this;
        }

        /**
         * Use this method to obtain the new schema object.
         *
         * @return the new matcher.
         */
        public Schema build ()
        {
            built = this; // TODO: Make boolean, change exception type.

            instance.validate();

            return new Schema(instance);
        }
    }

    /**
     * Result of a <code>match(Sexpr)</code> invocation.
     */
    public interface MatchResult
    {
        public boolean isSuccess ();

        public boolean isFailure ();

        public Sexpr<?> root ();

        /**
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
         * @return
         */
        public Optional<Sexpr<?>> lastSuccess ();

        /**
         * Execute the defined passes and related actions.
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
         * @return
         */
        public MatchResult execute ();
    }

    private final InternalSchema internal;

    private Schema (final InternalSchema internal)
    {
        this.internal = internal;
    }

    /**
     * This method determines whether the given symbolic-expression obeys this schema.
     *
     *
     * @param tree is the symbolic-expression that this schema may match.
     * @return true, iff the match was successful.
     */
    public MatchResult match (final Sexpr<?> tree)
    {
        return internal.match(tree);
    }

    public static Builder newBuilder ()
    {
        return new Builder();
    }
}
