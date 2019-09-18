package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes;

import java.util.StringJoiner;

public class LeftQuoteLexeme extends QuoteLexeme {

    @Override
    public String toString() {
        return new StringJoiner(", ", LeftQuoteLexeme.class.getSimpleName() + "[", "]")
                .toString();
    }
}
