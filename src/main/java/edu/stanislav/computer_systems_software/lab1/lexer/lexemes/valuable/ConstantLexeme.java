package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.StringJoiner;

public class ConstantLexeme extends Lexeme implements HasValue {

    private final String value;

    public ConstantLexeme(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ConstantLexeme.class.getSimpleName() + "[", "]")
                .add("value='" + value + "'")
                .toString();
    }
}
