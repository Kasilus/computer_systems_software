package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes;

import edu.stanislav.computer_systems_software.Constants;

public class RightQuoteLexeme extends QuoteLexeme {
    @Override
    public String toString() {
        if (Constants.FULL_LEXEME_PRINT) {
            return super.toString();
        }
        return ")";
    }
}
