package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.ExpressionAnalyzer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.*;

public class Lab2 {
    public static void main(String[] args) {

        String expression = "-3 + A * (-D)";
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
        analyzer.printResults();
        List<Lexeme> lexemes = analyzer.getLexemes();
        List<Lexeme> outLexemes = BackwardPolishNotationUtils.calculateBPN(lexemes);
        Node expressionTree = BackwardPolishNotationUtils.buildExpressionTree(outLexemes);
        System.out.println(expressionTree);
    }
}
