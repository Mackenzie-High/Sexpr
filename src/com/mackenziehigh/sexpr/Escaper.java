package com.mackenziehigh.sexpr;

import java.util.Arrays;

/**
 * Escaper.
 *
 * <p>
 * Herein, an escape sequence is any one of the following substrings:
 * <ul>
 * <li>\b</li>
 * <li>\t</li>
 * <li>\n</li>
 * <li>\f</li>
 * <li>\r</li>
 * <li>\'</li>
 * <li>\"</li>
 * <li>\\</li>
 * <li>\ uWXYZ, where WXYZ is a four-digit hexadecimal Unicode character code.</li>
 * </ul>
 * </p>
 */
public final class Escaper
{
    /**
     * This is the singleton instance of this class.
     */
    public static final Escaper instance = new Escaper();

    /**
     * Lookup Table.
     */
    private static final String[] ASCII = new String[128];

    static
    {
        ASCII['\b'] = "\\b";
        ASCII['\t'] = "\\t";
        ASCII['\n'] = "\\n";
        ASCII['\f'] = "\\f";
        ASCII['\r'] = "\\r";
        ASCII[34] = "\\\"";
        ASCII[39] = "\\'";
        ASCII[92] = "\\\\";

        /**
         * Normal Characters.
         */
        ASCII[33] = "!";
        //
        ASCII[35] = "#";
        ASCII[36] = "$";
        ASCII[37] = "%";
        ASCII[38] = "&";
        //
        ASCII[40] = "(";
        ASCII[41] = ")";
        ASCII[42] = "*";
        ASCII[43] = "+";
        ASCII[44] = ",";
        ASCII[45] = "-";
        ASCII[46] = ".";
        ASCII[47] = "/";
        ASCII[48] = "0";
        ASCII[49] = "1";
        ASCII[50] = "2";
        ASCII[51] = "3";
        ASCII[52] = "4";
        ASCII[53] = "5";
        ASCII[54] = "6";
        ASCII[55] = "7";
        ASCII[56] = "8";
        ASCII[57] = "9";
        ASCII[58] = ":";
        ASCII[59] = ";";
        ASCII[60] = "<";
        ASCII[61] = "=";
        ASCII[62] = ">";
        ASCII[63] = "?";
        ASCII[64] = "@";
        ASCII[65] = "A";
        ASCII[66] = "B";
        ASCII[67] = "C";
        ASCII[68] = "D";
        ASCII[69] = "E";
        ASCII[70] = "F";
        ASCII[71] = "G";
        ASCII[72] = "H";
        ASCII[73] = "I";
        ASCII[74] = "J";
        ASCII[75] = "K";
        ASCII[76] = "L";
        ASCII[77] = "M";
        ASCII[78] = "N";
        ASCII[79] = "O";
        ASCII[80] = "P";
        ASCII[81] = "Q";
        ASCII[82] = "R";
        ASCII[83] = "S";
        ASCII[84] = "T";
        ASCII[85] = "U";
        ASCII[86] = "V";
        ASCII[87] = "W";
        ASCII[88] = "X";
        ASCII[89] = "Y";
        ASCII[90] = "Z";
        ASCII[91] = "[";
        //
        ASCII[93] = "]";
        ASCII[94] = "^";
        ASCII[95] = "_";
        ASCII[96] = "`";
        ASCII[97] = "a";
        ASCII[98] = "b";
        ASCII[99] = "c";
        ASCII[100] = "d";
        ASCII[101] = "e";
        ASCII[102] = "f";
        ASCII[103] = "g";
        ASCII[104] = "h";
        ASCII[105] = "i";
        ASCII[106] = "j";
        ASCII[107] = "k";
        ASCII[108] = "l";
        ASCII[109] = "m";
        ASCII[110] = "n";
        ASCII[111] = "o";
        ASCII[112] = "p";
        ASCII[113] = "q";
        ASCII[114] = "r";
        ASCII[115] = "s";
        ASCII[116] = "t";
        ASCII[117] = "u";
        ASCII[118] = "v";
        ASCII[119] = "w";
        ASCII[120] = "x";
        ASCII[121] = "y";
        ASCII[122] = "z";
        ASCII[123] = "{";
        ASCII[124] = "|";
        ASCII[125] = "}";
        ASCII[126] = "~";
    }

    /**
     * Sole Constructor.
     */
    private Escaper ()
    {
        // Pass
    }

    /**
     * This method creates a string from a char-array,
     * with each special-character replaced with
     * a relevant escape sequence.
     *
     * @param input will be converted to a string.
     * @return the new string.
     */
    public String escape (final char[] input)
    {
        final StringBuilder str = new StringBuilder();

        for (int i = 0; i < input.length; i++)
        {
            final char chr = input[i];

            final boolean isAscii = chr < ASCII.length && ASCII[chr] != null;

            str.append(isAscii ? ASCII[chr] : String.format("\\u%04X", (int) chr));
        }

        return str.toString();
    }

    /**
     * This method replaces escape sequences in a string
     * with the equivalent special-characters and then
     * returns the equivalent char-array.
     *
     * <p>
     * In short, X.equals(expand(escape(X))).
     * </p>
     *
     * @param input is an escaped string.
     * @return the non-escaped char-array.
     */
    public char[] expand (final String input)
    {
        final char[] chars = input.toCharArray();

        final char[] temp = new char[chars.length];

        int i = 0; // position in the input
        int k = 0; // position in the output

        while (i < chars.length)
        {
            final int remaining = chars.length - i;

            final boolean slash = remaining >= 1 && chars[i] == '\\';

            if (slash && remaining == 0)
            {
                throw new IllegalArgumentException("Escape Slash at End of String");
            }

            /**
             * Detect the two-character escape-sequences.
             */
            final boolean s1 = slash && remaining >= 2 && chars[i + 1] == 'b';
            final boolean s2 = slash && remaining >= 2 && chars[i + 1] == 't';
            final boolean s3 = slash && remaining >= 2 && chars[i + 1] == 'n';
            final boolean s4 = slash && remaining >= 2 && chars[i + 1] == 'f';
            final boolean s5 = slash && remaining >= 2 && chars[i + 1] == 'r';
            final boolean s6 = slash && remaining >= 2 && chars[i + 1] == '\\';
            final boolean s7 = slash && remaining >= 2 && chars[i + 1] == '\'';
            final boolean s8 = slash && remaining >= 2 && chars[i + 1] == '\"';

            /**
             * Detect the hexadecimal Unicode escape-sequences.
             */
            final boolean d0 = slash && remaining >= 6 && chars[i + 1] == 'u';
            final boolean d1 = slash && remaining >= 6 && isHex(chars[i + 2]);
            final boolean d2 = slash && remaining >= 6 && isHex(chars[i + 3]);
            final boolean d3 = slash && remaining >= 6 && isHex(chars[i + 4]);
            final boolean d4 = slash && remaining >= 6 && isHex(chars[i + 5]);

            if (slash && !d0 && !s1 && !s2 && !s3 && !s4 && !s5 && !s6 && !s7 && !s8)
            {
                throw new IllegalArgumentException("No Such Escape Sequence");
            }

            if (slash && d0 && (!d1 || !d2 || !d3 || !d4))
            {
                throw new IllegalArgumentException("Invalid Unicode Escape Sequence");
            }

            /**
             * If the character is not part of an escape sequence,
             * then take the character itself.
             */
            char r = chars[i];

            /**
             * Replace escape-sequences with their expansions.
             */
            r = s1 ? '\b' : r;
            r = s2 ? '\t' : r;
            r = s3 ? '\n' : r;
            r = s4 ? '\f' : r;
            r = s5 ? '\r' : r;
            r = s6 ? '\\' : r;
            r = s7 ? '\'' : r;
            r = s8 ? '\"' : r;
            r = d0 && d1 && d2 && d3 && d4 ? (char) Integer.parseInt("" + chars[i + 2] + chars[i + 3] + chars[i + 4] + chars[i + 5], 16) : r;

            /**
             * Append the character onto the output.
             */
            temp[k++] = r;

            /**
             * Advance forward.
             * 6 = Unicode Escape Sequence
             * 2 = Two Character Escape Sequence
             * 1 = No Escape Sequence
             */
            i += slash ? (d0 ? 6 : 2) : 1;
        }

        final char[] result = Arrays.copyOf(temp, k);

        return result;
    }

    private boolean isHex (final char value)
    {
        switch (value)
        {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                return true;
            default:
                return false;
        }
    }

    public static void main (String[] args)
    {
        final Escaper m = new Escaper();

        for (char c = 1; c < Character.MAX_VALUE; c++)
        {
            String e = "" + c;
            char[] a = m.expand(m.escape(e.toCharArray()));
            if (e.charAt(0) != a[0] || a.length != 1)
            {
                System.out.println("XXXXXXXXXXXXXXXXXXXXXXXX = " + (int) c);
            }
            else
            {
                System.out.println("X = " + (int) c);
            }
        }
    }

}
