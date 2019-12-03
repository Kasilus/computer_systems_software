package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.ExpressionAnalyzer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Lab2Test {

    private ExpressionAnalyzer expressionAnalyzer;

    @Before
    public void init() {
        expressionAnalyzer = new ExpressionAnalyzer();
    }

    @Test
    public void testBPN_arithmetic_simple() {
        String input = "(2+4)*(4+6)";
        String BPNString = getBPNString(input);
        assertEquals("24+46+*", BPNString);
    }

    @Test
    public void testBPN_arithmetic_hard() {
        String input = "(A+B)-C/D+G*(K/L+M+N)";
        String BPNString = getBPNString(input);
        assertEquals("AB+CD/-GKL/MN++*+", BPNString);
    }

    private String getBPNString(String input) {
        expressionAnalyzer.analyzeExpression(input);
        List<Lexeme> lexemes = expressionAnalyzer.getLexemes();
        List<Lexeme> lexemesInBPN = BackwardPolishNotationUtils.calculateBPN(lexemes);
        StringBuilder BPNString = new StringBuilder();
        for (Lexeme lexeme: lexemesInBPN) {
            BPNString.append(lexeme.toString());
        }
        return BPNString.toString();
    }


}
