package com.mackenziehigh.sexpr;

import com.mackenziehigh.sexpr.internal.schema.MatchNode;
import com.mackenziehigh.sexpr.internal.schema.Schema;
import com.mackenziehigh.sexpr.internal.schema.SchemaParser;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * An instance of this class can be used to determine
 * whether a symbolic-expression matches a schema (pattern).
 *
 * TODO predefined actions
 */
public final class Matcher
{
    private final Map<String, Predicate<Sexpr>> requirements = new HashMap<>();

    private final Map<String, Consumer<Sexpr>> actions = new HashMap<>();

    private final Schema schema;

    /**
     * Sole Constructor.
     *
     * @param schema is the schema that symbolic-expressions must match.
     */
    private Matcher (final Schema schema)
    {
        this.schema = Objects.requireNonNull(schema, "schema");
    }

    /**
     * This method configures the matcher to use a given schema.
     *
     * @param schema is the schema for the matcher to use.
     * @return this.
     */
    public static Builder fromString (final String schema)
    {
        final SchemaParser parser = new SchemaParser();
        final Schema pattern = parser.parse("<dynamic>", schema);
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
        final SchemaParser parser = new SchemaParser();
        final Schema pattern = parser.parse(source, content.toString());
        return new Builder(pattern);
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
        throw new IOException();
    }

    /**
     * This method determines whether the schema
     * matches a given symbolic-expression.
     *
     * <p>
     * If the match is successful, then the onSuccess function(s)
     * will be invoked passing-in the root of the match-tree
     * that describes the individual successful rule matches.
     * </p>
     *
     * <p>
     * If the match is unsuccessful, then the onFailure function(s)
     * will be invoked passing-in the highest numbered node that
     * was successfully matched. If no node was successfully matched,
     * then an empty optional will be passed-in to the function(s).
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

        final Function<String, Predicate<Sexpr>> resolveCondition = name -> requirements.get(name);
        final Function<String, Consumer<Sexpr>> resolveAction = name -> actions.get(name);
        final Consumer<MatchNode> onSuccess = x -> System.out.println("Success!");
        final Consumer<Optional<Sexpr>> onFailure = x -> System.out.println("Failed At: " + (x.isPresent() ? x.get().location().message() : "TOTAL FAILURE"));

        final boolean match = schema.match(tree, resolveCondition, resolveAction, onSuccess, onFailure);
        return match;
    }

    /**
     * Use an instance of this class to create a Matcher object.
     */
    public static final class Builder
    {
        private Matcher instance;

        /**
         * Sole Constructor.
         *
         * @param schema is the schema that the matcher will use.
         */
        private Builder (final Schema schema)
        {
            Objects.requireNonNull(schema, "schema");
            this.instance = new Matcher(schema);
            addPredefinedActions();
        }

        private void addPredefinedActions ()
        {
            action("$PRINTLN", x -> System.out.println(x.toString()));
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

            if (instance.requirements.containsKey(name))
            {
                throw new IllegalArgumentException("Duplicate Predicate: " + name);
            }

            instance.requirements.put(name, condition);
            return this;
        }

        /**
         * Use this method to define an action that can be used within a schema.
         *
         * <p>
         * Inside of the schema, the predicate must be referenced via
         * either a 'before' or 'after' directive.
         * </p>
         *
         * @param name is the name that will be used to identify the predicate.
         * @param action is the action itself.
         * @return this.
         * @throws IllegalArgumentException if the name already identifies a action.
         */
        public Builder action (final String name,
                               final Consumer<Sexpr> action)
        {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(action, "action");
            Objects.requireNonNull(instance, "build() was already called.");

            if (instance.actions.containsValue(name))
            {
                throw new IllegalArgumentException("Duplicate Action: " + name);
            }

            instance.actions.put(name, action);
            return this;
        }

        /**
         * Use this method to obtain the new Matcher object.
         *
         * @return the new matcher.
         */
        public Matcher build ()
        {
            final Matcher result = instance;
            instance = null;
            return result;
        }
    }

    public static void main (String[] args)
            throws IOException
    {
        final Matcher m = Matcher.fromFile(new File("/home/mackenzie/Schema.s")).build();
        final Sexpr tree = SList.parse("", "(div (p (div)) (p 123))");

        m.match(tree);
    }
}
