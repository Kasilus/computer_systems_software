package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic;

import edu.stanislav.computer_systems_software.Constants;

public class DivideOperatorLexeme extends ArithmeticOperatorLexeme {

    @Override
    public String toString() {
        if (Constants.FULL_LEXEME_PRINT) {
            return super.toString();
        }
        return "/";
    }
}
