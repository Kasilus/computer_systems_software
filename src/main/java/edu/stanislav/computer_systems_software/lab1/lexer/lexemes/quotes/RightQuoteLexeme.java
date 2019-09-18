package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes;

import java.util.StringJoiner;

public class RightQuoteLexeme extends QuoteLexeme {

    @Override
    public String toString() {
        return new StringJoiner(", ", RightQuoteLexeme.class.getSimpleName() + "[", "]")
                .toString();
    }
}
