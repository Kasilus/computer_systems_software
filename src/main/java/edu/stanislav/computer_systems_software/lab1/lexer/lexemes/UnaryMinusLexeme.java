package edu.stanislav.computer_systems_software.lab1.lexer.lexemes;

import edu.stanislav.computer_systems_software.Constants;

public class UnaryMinusLexeme extends Lexeme {
    @Override
    public String toString() {
        if (Constants.FULL_LEXEME_PRINT) {
            return super.toString();
        }
        return "~";
    }
}
