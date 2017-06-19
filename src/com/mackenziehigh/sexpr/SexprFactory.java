package com.mackenziehigh.sexpr;

import com.mackenziehigh.sexpr.internal.ObjectFactory;
import java.util.Map;
import java.util.Optional;

/**
 * Factory for creating Sexpr objects.
 */
public interface SexprFactory
{
    public static class ParsingFailedException
            extends RuntimeException
    {
        public ParsingFailedException (SourceLocation location)
        {
            super("Parsing Failed At " + location.message());
        }
    }

    /**
     * Default Instance.
     */
    public static final SexprFactory instance = new ObjectFactory();

    public SList parse (String location,
                        String input);

    /**
     * This method creates a new atom from the given value.
     *
     * @param value is the value of the new atom.
     * @return the new atom.
     */
    public SAtom from (boolean value);

    /**
     * This method creates a new atom from the given value.
     *
     * @param value is the value of the new atom.
     * @return the new atom.
     */
    public SAtom from (long value);

    /**
     * This method creates a new atom from the given value.
     *
     * @param value is the value of the new atom.
     * @return the new atom.
     */
    public SAtom from (double value);

    /**
     * This method creates a new atom from the given value.
     *
     * @param value is the value of the new atom.
     * @return the new atom.
     */
    public SAtom from (String value);

    /**
     * This method creates a new atom from the given value.
     *
     * @param value is the value of the new atom.
     * @return the new atom.
     */
    public SAtom from (Class value);

    /**
     * This method creates a new list from the given elements.
     *
     * @param elements will comprise the new list.
     * @return the new list.
     */
    public SList from (Sexpr... elements);

    /**
     * This method creates a new list from the given elements.
     *
     * @param elements will comprise the new list.
     * @return the new list.
     */
    public SList from (Iterable<? extends Sexpr> elements);

    /**
     * This method creates a new two-dimensional list from the given map.
     *
     * <p>
     * The result will be a list of lists.
     * Each inner list will correspond to a single entry in the map.
     * Each inner list will have exactly two elements.
     * The first element is the key from the map entry.
     * The second element is the value from the map entry.
     * </p>
     *
     * @param map contains the entries to add to the list.
     * @return the new list.
     */
    public SList from (Map<? extends Sexpr, ? extends Sexpr> map);

    /**
     * This method converts a specially encoded byte-array to a Sexpr object.
     *
     * @param bytes is the specially encoded byte-array to convert.
     * @return the Sexpr object that is derived from the given array of bytes,
     * if the conversion is successful; otherwise, return empty.
     */
    public Optional<Sexpr> deserialize (byte[] bytes);

    /**
     * This method converts a specially formatted JSON string to a Sexpr object.
     *
     * @param json is the specially formated JSON string to convert.
     * @return the Sexpr object that is derived from the given JSON,
     * if the conversion is successful; otherwise, return empty.
     */
    public Optional<Sexpr> deserialize (String json);
}
