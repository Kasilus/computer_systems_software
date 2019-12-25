package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.Constants;
import edu.stanislav.computer_systems_software.lab1.ExpressionAnalyzer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.DivideOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MinusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MultiplyOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.PlusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.HasValue;

import java.util.*;

public class Lab2 {

    public static void main(String[] args) {
        String expression = "-3 * (-15 + 12)";
        buildBalancedExpressionTree(expression);
    }

    public static Node buildBalancedExpressionTree(String expression) {
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
        analyzer.printResults();
        if (!analyzer.getAnalysisSuccessful()) {
            return null;
        }
        List<Lexeme> lexemes = analyzer.getLexemes();
        List<Lexeme> outLexemes = BackwardPolishNotationUtils.calculateBPN(lexemes);
        Node expressionTree = BackwardPolishNotationUtils.buildExpressionTree(outLexemes);
        if (Constants.FULL_PRINT) {
            System.out.println("\nNot balanced expression tree");
            System.out.println(expressionTree);
        }
        Node balancedExpressionTree = TreeBalancer.balance(expressionTree);
        if (Constants.FULL_PRINT) {
            System.out.println("\nBalanced expression tree");
            System.out.println(balancedExpressionTree);
        }
        return balancedExpressionTree;
    }
}
