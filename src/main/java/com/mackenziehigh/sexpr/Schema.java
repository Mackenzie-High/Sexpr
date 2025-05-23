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

import com.mackenziehigh.sexpr.internal.schema.InternalAnnotator;
import com.mackenziehigh.sexpr.internal.schema.InternalSchema;
import com.mackenziehigh.sexpr.internal.schema.InternalSchemaParser;
import java.util.Objects;
import java.util.Optional;
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

        /**
         * This flag becomes true, when <code>build()</code> get called.
         */
        private volatile boolean built = false;

        /**
         * Sole Constructor.
         */
        private Builder ()
        {
            // Pass.
        }

        private void requireNotBuilt ()
        {
            if (built)
            {
                throw new IllegalStateException("build() was already called.");
            }
        }

        /*
         * This method imports the schema rules defined in the given string.
         *
         * @param schema is the textual schema.
         * @return this.
         */
        public Builder include (final String schema)
        {
            requireNotBuilt();
            final StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            final StackTraceElement caller = stackTrace[stackTrace.length - 1];
            final String location = caller.toString();
            InternalSchemaParser.parse(instance, location, schema);
            return this;
        }

        /**
         * This method imports the schema rules defined in the given string.
         *
         * @param source is a human-readable string indicating where the schema is from.
         * @param schema is the textual schema to parse.
         * @return this.
         */
        public Builder include (final String source,
                                final String schema)
        {
            requireNotBuilt();
            InternalSchemaParser.parse(instance, source, schema);
            return this;
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
                                  Predicate<Sexpr<?>> condition)
        {
            requireNotBuilt();
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(condition, "condition");
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
            requireNotBuilt();
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
                               Consumer<Sexpr<?>> action)
        {
            requireNotBuilt();
            Objects.requireNonNull(pass, "pass");
            Objects.requireNonNull(rule, "rule");
            Objects.requireNonNull(action, "action");
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
                              Consumer<Sexpr<?>> action)
        {
            requireNotBuilt();
            Objects.requireNonNull(pass, "pass");
            Objects.requireNonNull(rule, "rule");
            Objects.requireNonNull(action, "action");
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
            requireNotBuilt();
            final InternalAnnotator annotator = new InternalAnnotator(this);
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
            requireNotBuilt();
            built = true;

            instance.validate();

            return new Schema(instance);
        }
    }

    /**
     * Result of a <code>match(Sexpr)</code> invocation.
     */
    public interface Match
    {
        /**
         * Determine whether this object represents a successful match.
         *
         * @return true, if the match attempt succeeded.
         */
        public boolean isSuccess ();

        /**
         * Determine whether this object represents a unsuccessful match.
         *
         * @return true, if the match attempt failed.
         */
        public boolean isFailure ();

        /**
         * Get the symbolic-expression that was the input to <code>match()</code>.
         *
         * @return the input.
         */
        public Sexpr<?> input ();

        /**
         * Get the last node of the input that was successfully matched, if any,
         * which is useful for locating and reporting errors in the input.
         *
         * @return the last successfully matched node, if any.
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
         * @return this.
         */
        public Match execute ();
    }

    private final InternalSchema internal;

    private Schema (final InternalSchema internal)
    {
        this.internal = internal;
    }

    /**
     * This method determines whether the given symbolic-expression obeys this schema.
     *
     * @param input is the symbolic-expression that this schema may match.
     * @return an object that describes the whether the match was successful or not.
     */
    public Match match (final Sexpr<?> input)
    {
        return internal.match(input);
    }

    /**
     * Builder Factory.
     *
     * @return an object that can be used to build a <code>Schema</code> object.
     */
    public static Builder newBuilder ()
    {
        return new Builder();
    }
}
