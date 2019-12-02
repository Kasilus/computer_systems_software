package edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math;

import edu.stanislav.computer_systems_software.Constants;

public class SinMathFunctionLexeme extends MathFunctionLexeme {

    @Override
    public String toString() {
        if (Constants.FULL_LEXEME_PRINT) {
            return super.toString();
        }
        return "sin";
    }
}
