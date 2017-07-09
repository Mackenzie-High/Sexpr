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

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.mackenziehigh.sexpr.internal.BinaryFormat;
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
 * An instance of this class can serialize symbolic-expressions
 * to/from a binary representation using Google Protocol Buffers.
 */
public final class SexprSerializerGPB
        implements SexprSerializer<byte[]>
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
    @Override
    public byte[] encode (final Sexpr expression)
    {
        final BinaryFormat.tree_t tree = linearize(expression);
        final byte[] data = tree.toByteArray();
        final byte[] hash = computeMD5(data);

        final BinaryFormat.sexpr_t.Builder builder = BinaryFormat.sexpr_t.newBuilder();
        builder.setChecksum(ByteString.copyFrom(hash));
        builder.setTree(ByteString.copyFrom(data));
        final BinaryFormat.sexpr_t message = builder.build();
        return message.toByteArray();
    }

    private byte[] computeMD5 (final byte[] data)
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

    private BinaryFormat.tree_t linearize (final Sexpr tree)
    {
        final Consumer<Sexpr> NOP = x ->
        {
            // Pass
        };

        final List<BinaryFormat.node_t> nodes = new ArrayList<>(tree.treeSize());

        final Consumer<Sexpr> encode = x ->
        {
            nodes.add(encodeNode(x));
        };

        tree.transverse(NOP, encode);

        final BinaryFormat.tree_t.Builder builder = BinaryFormat.tree_t.newBuilder();
        nodes.forEach(x -> builder.addNodes(x));
        return builder.build();
    }

    private BinaryFormat.node_t encodeNode (final Sexpr node)
    {
        return node.isAtom() ? encodeAtom(node.toAtom()) : encodeList(node.toList());
    }

    private BinaryFormat.node_t encodeAtom (final SAtom node)
    {
        final BinaryFormat.node_t.Builder builder = BinaryFormat.node_t.newBuilder();
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

    private BinaryFormat.node_t encodeList (final SList node)
    {
        final BinaryFormat.node_t.Builder builder = BinaryFormat.node_t.newBuilder();
        builder.setLocation(encodeLocation(node.location()));
        builder.setElementCount(node.size());
        return builder.build();
    }

    private BinaryFormat.location_t encodeLocation (final SourceLocation node)
    {
        final BinaryFormat.location_t.Builder builder = BinaryFormat.location_t.newBuilder();
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
     */
    @Override
    public Sexpr decode (final byte[] bytes)
    {
        try
        {
            final BinaryFormat.sexpr_t outer = BinaryFormat.sexpr_t.parseFrom(bytes);

            final byte[] expectedMD5 = outer.getChecksum().toByteArray();
            final byte[] actualMD5 = computeMD5(outer.getTree().toByteArray());
            final boolean checksumOK = Arrays.equals(expectedMD5, actualMD5);
            if (checksumOK == false)
            {
                // TODO: Stringify hashes
                throw new RuntimeException(String.format("Corrupt Data: Expected MD5 = , Actual MD5 = "));
            }

            final BinaryFormat.tree_t tree = BinaryFormat.tree_t.parseFrom(outer.getTree());
            final Stack<Sexpr> stack = new Stack<>();
            tree.getNodesList().forEach(node -> decodeNode(stack, node));
            // TODO: verify stack is size 1
            final Sexpr root = stack.pop();
            return root;
        }
        catch (InvalidProtocolBufferException ex)
        {
            throw new RuntimeException(ex);
        }
    }

    private void decodeNode (final Stack<Sexpr> stack,
                             final BinaryFormat.node_t node)
    {
        if (node.getOptionalElementCountCase() == BinaryFormat.node_t.OptionalElementCountCase.OPTIONALELEMENTCOUNT_NOT_SET)
        {
            decodeAtom(stack, node);
        }
        else
        {
            decodeList(stack, node);
        }
    }

    private void decodeAtom (final Stack<Sexpr> stack,
                             final BinaryFormat.node_t node)
    {
        final SourceLocation location = decodeLocation(node);
        final SAtom result;

        if (node.getOptionalValueAsBooleanCase() == BinaryFormat.node_t.OptionalValueAsBooleanCase.VALUEASBOOLEAN)
        {
            result = new SAtom(location, node.getValueAsBoolean());
        }
        else if (node.getOptionalValueAsLongCase() == BinaryFormat.node_t.OptionalValueAsLongCase.VALUEASLONG)
        {
            result = new SAtom(location, node.getValueAsLong());
        }
        else if (node.getOptionalValueAsDoubleCase() == BinaryFormat.node_t.OptionalValueAsDoubleCase.VALUEASDOUBLE)
        {
            result = new SAtom(location, node.getValueAsDouble());
        }
        else if (node.getOptionalValueAsStringCase() == BinaryFormat.node_t.OptionalValueAsStringCase.VALUEASSTRING)
        {
            result = new SAtom(location, node.getValueAsString());
        }
        else
        {
            throw new RuntimeException("No Value in node_t");
        }

        stack.push(result);
    }

    private void decodeList (final Stack<Sexpr> stack,
                             final BinaryFormat.node_t node)
    {
        final SourceLocation location = decodeLocation(node);
        final int elementCount = node.getElementCount();
        final LinkedList<Sexpr> children = new LinkedList<>();
        IntStream.range(0, elementCount).forEach(i -> children.addFirst(stack.pop()));
        final SList result = SList.copyOf(location, children);
        stack.push(result);
    }

    private SourceLocation decodeLocation (final BinaryFormat.node_t node)
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
}
