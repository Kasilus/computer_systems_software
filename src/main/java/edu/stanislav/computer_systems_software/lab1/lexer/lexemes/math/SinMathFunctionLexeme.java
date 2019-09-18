package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math;

import java.util.StringJoiner;

public class SinMathFunctionLexeme extends MathFunctionLexeme {

    @Override
    public String toString() {
        return new StringJoiner(", ", SinMathFunctionLexeme.class.getSimpleName() + "[", "]")
                .toString();
    }
}
