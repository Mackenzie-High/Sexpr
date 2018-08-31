package com.mackenziehigh.sexpr.internal;


import com.mackenziehigh.sexpr.SAtom;
import com.mackenziehigh.sexpr.SourceLocation;
import com.mackenziehigh.sexpr.internal.Escaper;
import com.mackenziehigh.sexpr.util.BuilderStack;

/**
 *
 */
final class LexerHelpers
{
    public static void pushAtomForm1 (final BuilderStack stack,
                                      final String yytext,
                                      final int yyline,
                                      final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(2, length - 1);
        final SourceLocation location = new SourceLocation("TODO", yyline, yycolum);
        final SAtom atom = new SAtom(location, text);
        stack.push(atom);
    }

    public static void pushAtomForm2 (final BuilderStack stack,
                                      final String yytext,
                                      final int yyline,
                                      final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(2, length - 1);
        final SourceLocation location = new SourceLocation("TODO", yyline, yycolum);
        final SAtom atom = new SAtom(location, text);
        stack.push(atom);
    }

    public static void pushAtomForm3 (final BuilderStack stack,
                                      final String yytext,
                                      final int yyline,
                                      final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(1, length - 1);
        final String expanded = new String(Escaper.instance.expand(text));
        final SourceLocation location = new SourceLocation("TODO", yyline, yycolum);
        final SAtom atom = new SAtom(location, expanded);
        stack.push(atom);
    }

    public static void pushAtomForm4 (final BuilderStack stack,
                                      final String yytext,
                                      final int yyline,
                                      final int yycolum)
    {
        final int length = yytext.length();
        final String text = yytext.substring(1, length - 1);
        final String expanded = new String(Escaper.instance.expand(text));
        final SourceLocation location = new SourceLocation("TODO", yyline, yycolum);
        final SAtom atom = new SAtom(location, expanded);
        stack.push(atom);
    }

    public static void pushAtomForm5 (final BuilderStack stack,
                                      final String yytext,
                                      final int yyline,
                                      final int yycolum)
    {
        final SourceLocation location = new SourceLocation("TODO", yyline, yycolum);
        final SAtom atom = new SAtom(location, yytext);
        stack.push(atom);
    }
}
