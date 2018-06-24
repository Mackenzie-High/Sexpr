package com.mackenziehigh.sexpr.internal;

/**
 *
 */
final class FastLexer
{
    private final char[] text;

    private int pos = 0;

    private final StringBuilder token = new StringBuilder();

    private TokenTypes type;

    public FastLexer (final String input)
    {
        this.text = new char[input.length() + 6];

        for (int i = 0; i < input.length(); i++)
        {
            text[i] = input.charAt(i);
        }
    }

    public StringBuilder token ()
    {
        return token;
    }

    public TokenTypes type ()
    {
        return type;
    }

    public void next ()
    {
        token.setLength(0);
        type = TokenTypes.EOF;

        if (isEOF())
        {
            return;
        }

        consumeWS();

        if (isEOF())
        {
            return;
        }
        else if (isOpenParen())
        {
            consumeOpenParen();
        }
        else if (isCloseParen())
        {
            consumeCloseParen();
        }
        else if (isDoubleQuote())
        {
            consumeDoubleQuoteString();
        }
        else if (isSingleQuote())
        {
            consumeSingleQuoteString();
        }
        else
        {
            consumeAtom();
        }
    }

    private void consumeWS ()
    {
        while (isSpace() || isCommentOpen())
        {
            while (isSpace())
            {
                consumeSpace();
            }

            while (isCommentOpen())
            {
                consumeComment();
            }
        }
    }

    private void consumeSpace ()
    {
        ++pos;
    }

    private void consumeComment ()
    {
        // Pass
    }

    private void consumeOpenParen ()
    {
        token.append('(');
        type = TokenTypes.PAREN_OPEN;
        ++pos;
    }

    private void consumeCloseParen ()
    {
        token.append(')');
        type = TokenTypes.PAREN_CLOSE;
        ++pos;
    }

    private void consumeAtom ()
    {
        type = TokenTypes.ATOM;

        while (isSpecial() == false)
        {
            token.append(text[pos]);
            ++pos;
        }
    }

    private void consumeSingleQuoteString ()
    {
        type = TokenTypes.ATOM;

        while (!isEOF() && !isSingleQuote())
        {
            if (isEscapeSequence())
            {
                consumeEscapeSequence();
            }
            else
            {
                token.append(text[pos]);
                ++pos;
            }
        }
    }

    private boolean consumeDoubleQuoteString ()
    {
        return false;
    }

    private void consumeEscapeSequence ()
    {
        token.append(text[pos]);
        ++pos;
    }

    private boolean isEOF ()
    {
        return pos >= text.length - 6;
    }

    private boolean isSpecial ()
    {
        return isEOF() || isCommentOpen() || isSpace() || isSingleQuote() || isDoubleQuote() || isOpenParen() || isCloseParen();
    }

    private boolean isCommentOpen ()
    {
        return false;
    }

    private boolean isCommentClose ()
    {
        return false;
    }

    private boolean isSpace ()
    {
        return ' ' == text[pos] || '\t' == text[pos] || '\r' == text[pos] || '\n' == text[pos];
    }

    private boolean isOpenParen ()
    {
        return '(' == text[pos];
    }

    private boolean isCloseParen ()
    {
        return ')' == text[pos];
    }

    private boolean isSingleQuote ()
    {
        return '\'' == text[pos];
    }

    private boolean isDoubleQuote ()
    {
        return '\"' == text[pos];
    }

    private boolean isEscapeSequence ()
    {
        return '\\' == text[pos];
    }

    public static enum TokenTypes
    {
        PAREN_OPEN,
        PAREN_CLOSE,
        ATOM,
        EOF,
    }

    public static void main (String[] args)
    {
        final FastLexer lexer = new FastLexer("(10 '3 4' 20 30)");

        lexer.next();
        System.out.println("X = " + lexer.token());

        lexer.next();
        System.out.println("X = " + lexer.token());

        lexer.next();
        System.out.println("X = " + lexer.token());

        lexer.next();
        System.out.println("X = " + lexer.token());

        lexer.next();
        System.out.println("X = " + lexer.token());

        lexer.next();
        System.out.println("X = " + lexer.token());
    }
}
