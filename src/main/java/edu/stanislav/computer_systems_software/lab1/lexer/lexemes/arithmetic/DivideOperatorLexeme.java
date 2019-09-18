package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic;

import java.util.StringJoiner;

public class DivideOperatorLexeme extends ArithmeticOperatorLexeme {

    @Override
    public String toString() {
        return new StringJoiner(", ", DivideOperatorLexeme.class.getSimpleName() + "[", "]")
                .toString();
    }
}
