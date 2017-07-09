package com.mackenziehigh.sexpr.internal;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SList;
import com.mackenziehigh.sexpr.Sexpr;
import com.mackenziehigh.sexpr.SourceLocation;
import com.mackenziehigh.sexpr.internal.BinaryFormat.location_t;
import com.mackenziehigh.sexpr.internal.BinaryFormat.node_t;
import com.mackenziehigh.sexpr.internal.BinaryFormat.sexpr_t;
import com.mackenziehigh.sexpr.internal.BinaryFormat.tree_t;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * This class provides static methods for converting
 * a symbolic-expression to and from other formats.
 */
public final class Serializer
{
    /**
     * This method converts a symbolic-expression to binary data,
     * using a special encoding scheme.
     *
     * <p>
     * Use the convertFromBytes(*) method to decode the binary data.
     * </p>
     *
     * @param expression is the expression to convert.
     * @return the encoded expression.
     */
    public static byte[] convertToBytes (final Sexpr expression)
    {
        final tree_t tree = linearize(expression);
        final byte[] data = tree.toByteArray();
        final byte[] hash = computeMD5(data);

        final sexpr_t.Builder builder = sexpr_t.newBuilder();
        builder.setChecksum(ByteString.copyFrom(hash));
        builder.setTree(ByteString.copyFrom(data));
        final sexpr_t message = builder.build();
        return message.toByteArray();
    }

    private static byte[] computeMD5 (final byte[] data)
    {
        try
        {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] digest = md.digest(data);
            return digest;
        }
        catch (NoSuchAlgorithmException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private static tree_t linearize (final Sexpr tree)
    {
        final Consumer<Sexpr> NOP = x ->
        {
            // Pass
        };

        final List<node_t> nodes = new ArrayList<>(tree.treeSize());

        final Consumer<Sexpr> encode = x ->
        {
            nodes.add(encodeNode(x));
        };

        tree.transverse(NOP, encode);

        final tree_t.Builder builder = tree_t.newBuilder();
        nodes.forEach(x -> builder.addNodes(x));
        return builder.build();
    }

    private static node_t encodeNode (final Sexpr node)
    {
        return node.isAtom() ? encodeAtom(node.toAtom()) : encodeList(node.toList());
    }

    private static node_t encodeAtom (final SAtom node)
    {
        final node_t.Builder builder = node_t.newBuilder();
        builder.setLocation(encodeLocation(node.location()));

        if (node.asLong().isPresent())
        {
            builder.setValueAsLong(node.asLong().get());
        }
        else if (node.asDouble().isPresent())
        {
            builder.setValueAsDouble(node.asDouble().get());
        }
        else if (node.asBoolean().isPresent())
        {
            builder.setValueAsBoolean(node.asBoolean().get());
        }
        else
        {
            builder.setValueAsString(node.content());
        }

        return builder.build();
    }

    private static node_t encodeList (final SList node)
    {
        final node_t.Builder builder = node_t.newBuilder();
        builder.setLocation(encodeLocation(node.location()));
        builder.setElementCount(node.size());
        return builder.build();
    }

    private static location_t encodeLocation (final SourceLocation node)
    {
        final location_t.Builder builder = location_t.newBuilder();
        builder.setSource(node.source());
        builder.setLine(node.line());
        builder.setColumn(node.column());
        return builder.build();
    }

    /**
     * This method converts binary data to a symbolic-expression.
     *
     * <p>
     * The binary data must be formatted according to
     * the contract of the convertToBytes(*) method.
     * </p>
     *
     * @param bytes is the binary data to convert.
     * @return the symbolic-expression derived from the binary data.
     * @throws InvalidProtocolBufferException
     */
    public static Sexpr convertFromBytes (final byte[] bytes)
            throws InvalidProtocolBufferException
    {
        final sexpr_t outer = sexpr_t.parseFrom(bytes);

        final byte[] expectedMD5 = outer.getChecksum().toByteArray();
        final byte[] actualMD5 = computeMD5(outer.getTree().toByteArray());
        final boolean checksumOK = Arrays.equals(expectedMD5, actualMD5);
        if (checksumOK == false)
        {
            // TODO: Stringify hashes
            throw new RuntimeException(String.format("Corrupt Data: Expected MD5 = , Actual MD5 = "));
        }

        final tree_t tree = tree_t.parseFrom(outer.getTree());
        final Stack<Sexpr> stack = new Stack<>();
        tree.getNodesList().forEach(node -> decodeNode(stack, node));
        // TODO: verify stack is size 1
        final Sexpr root = stack.pop();
        return root;
    }

    private static void decodeNode (final Stack<Sexpr> stack,
                                    final node_t node)
    {
        if (node.getOptionalElementCountCase() == node_t.OptionalElementCountCase.OPTIONALELEMENTCOUNT_NOT_SET)
        {
            decodeAtom(stack, node);
        }
        else
        {
            decodeList(stack, node);
        }
    }

    private static void decodeAtom (final Stack<Sexpr> stack,
                                    final node_t node)
    {
        final SourceLocation location = decodeLocation(node);
        final SAtom result;

        if (node.getOptionalValueAsBooleanCase() == node_t.OptionalValueAsBooleanCase.VALUEASBOOLEAN)
        {
            result = new SAtom(location, node.getValueAsBoolean());
        }
        else if (node.getOptionalValueAsLongCase() == node_t.OptionalValueAsLongCase.VALUEASLONG)
        {
            result = new SAtom(location, node.getValueAsLong());
        }
        else if (node.getOptionalValueAsDoubleCase() == node_t.OptionalValueAsDoubleCase.VALUEASDOUBLE)
        {
            result = new SAtom(location, node.getValueAsDouble());
        }
        else if (node.getOptionalValueAsStringCase() == node_t.OptionalValueAsStringCase.VALUEASSTRING)
        {
            result = new SAtom(location, node.getValueAsString());
        }
        else
        {
            throw new RuntimeException("No Value in node_t");
        }

        stack.push(result);
    }

    private static void decodeList (final Stack<Sexpr> stack,
                                    final node_t node)
    {
        final SourceLocation location = decodeLocation(node);
        final int elementCount = node.getElementCount();
        final LinkedList<Sexpr> children = new LinkedList<>();
        IntStream.range(0, elementCount).forEach(i -> children.addFirst(stack.pop()));
        final SList result = SList.copyOf(location, children);
        stack.push(result);
    }

    private static SourceLocation decodeLocation (final node_t node)
    {
        if (node.hasLocation())
        {
            final String source = node.getLocation().getSource();
            final int line = node.getLocation().getLine();
            final int column = node.getLocation().getColumn();
            return new SourceLocation(source, line, column);
        }
        else
        {
            return SourceLocation.DEFAULT;
        }
    }

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

    public static void main (String[] args)
            throws InvalidProtocolBufferException
    {
        final SList tree = SList.parse("Mars", "(@'1\\b 7' 2.89) 3 (4 5 'true')");

        final byte[] bytes = Serializer.convertToBytes(tree);
        final Sexpr decoded = Serializer.convertFromBytes(bytes);

        System.out.println(decoded);
    }
}
