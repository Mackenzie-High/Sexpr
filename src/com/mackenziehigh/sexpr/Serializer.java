package com.mackenziehigh.sexpr;

import com.mackenziehigh.sexpr.internal.Message.message_t;

/**
 * This class provides static methods for converting
 * a symbolic-expression to and from other formats.
 */
public final class Serializer
{
    public static byte[] convertToBytes (final Sexpr tree)
    {
        message_t.Builder msg = message_t.newBuilder();

        return null;
    }

    public static Sexpr convertFromBytes (final byte[] bytes)
    {
        return null;
    }

    public static byte[] convertToJson (final Sexpr tree)
    {
        return null;
    }

    public static Sexpr convertFromJson (final byte[] bytes)
    {
        return null;
    }
}
