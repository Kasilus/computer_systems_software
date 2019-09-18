package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math;

import java.util.StringJoiner;

public class CosMathFunctionLexeme extends MathFunctionLexeme {

    @Override
    public String toString() {
        return new StringJoiner(", ", CosMathFunctionLexeme.class.getSimpleName() + "[", "]")
                .toString();
    }
}
