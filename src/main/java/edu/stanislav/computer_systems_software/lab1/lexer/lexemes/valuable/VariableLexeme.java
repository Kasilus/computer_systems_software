package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable;

import edu.stanislav.computer_systems_software.Constants;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.StringJoiner;

public class VariableLexeme extends Lexeme implements HasValue {

    private final String value;

    public VariableLexeme(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        if(Constants.FULL_LEXEME_PRINT) {
            return new StringJoiner(", ", VariableLexeme.class.getSimpleName() + "[", "]")
                    .add("value='" + value + "'")
                    .toString();
        } else {
            return getValue();
        }
    }
}
