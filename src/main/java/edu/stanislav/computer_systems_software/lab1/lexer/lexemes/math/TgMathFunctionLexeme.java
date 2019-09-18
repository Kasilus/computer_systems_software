package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math;

import java.util.StringJoiner;

public class TgMathFunctionLexeme extends MathFunctionLexeme {

    @Override
    public String toString() {
        return new StringJoiner(", ", TgMathFunctionLexeme.class.getSimpleName() + "[", "]")
                .toString();
    }
}
