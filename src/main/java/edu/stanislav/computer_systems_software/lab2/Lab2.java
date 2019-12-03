package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.ExpressionAnalyzer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.*;

public class Lab2 {
    public static void main(String[] args) {

        String expression = "(A + 8 * sin(B * 15)) / (C + tg(D))";
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
//        analyzer.printLexemes();
        List<Lexeme> lexemes = analyzer.getLexemes();
        // change sequences on that, which could be paralleled

        // backward polish notation
        List<Lexeme> outLexemes = BackwardPolishNotationUtils.calculateBPN(lexemes);

        // backward polish notation with sin, cos, unary '-'

        // build expression tree
        Node expressionTree = BackwardPolishNotationUtils.buildExpressionTree(outLexemes);
        System.out.println(expressionTree);
    }
}
