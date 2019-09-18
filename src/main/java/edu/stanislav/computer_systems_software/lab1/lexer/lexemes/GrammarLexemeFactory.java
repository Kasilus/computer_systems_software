package edu.stanislav.computer_systems_software.lab1.lexer.lexemes;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.*;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.CosMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.MathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.SinMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.TgMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.QuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.ConstantLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.VariableLexeme;

public class GrammarLexemeFactory {

    public static ArithmeticOperatorLexeme createArithmeticLexeme(Character currentCharacter) {
        ArithmeticOperatorLexeme newLexeme = null;
        switch (currentCharacter) {
            case '+': newLexeme = new PlusOperatorLexeme(); break;
            case '-': newLexeme = new MinusOperatorLexeme(); break;
            case '*': newLexeme = new MultiplyOperatorLexeme(); break;
            case '/': newLexeme = new DivideOperatorLexeme(); break;
        }
        return newLexeme;
    }

    public static QuoteLexeme createQuoteLexeme(Character currentCharacter) {
        QuoteLexeme newLexeme = null;
        switch (currentCharacter) {
            case '(': newLexeme = new LeftQuoteLexeme(); break;
            case ')': newLexeme = new RightQuoteLexeme(); break;
        }
        return newLexeme;
    }

    public static MathFunctionLexeme createMathFuncLexeme(String currentString) {
        MathFunctionLexeme newLexeme = null;
        switch (currentString) {
            case "sin": newLexeme = new SinMathFunctionLexeme(); break;
            case "cos": newLexeme = new CosMathFunctionLexeme(); break;
            case "tg": newLexeme = new TgMathFunctionLexeme(); break;
        }
        return newLexeme;
    }

    public static ConstantLexeme createConstantLexeme(String currentString) {
        return new ConstantLexeme(currentString);
    }

    public static VariableLexeme createVariableLexeme(String currentString) {
        return new VariableLexeme(currentString);
    }
}
