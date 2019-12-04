package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.ExpressionAnalyzer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.*;

public class Lab2 {
    public static void main(String[] args) {

        String expression = "1+2-3+4+5+6+7";
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
        analyzer.printResults();
        List<Lexeme> lexemes = analyzer.getLexemes();
        // change sequence (A/S/D/F/G/H or A-S-D-F-G-H)
        // A/S/D/F/G/H => (A/S)/(D*F)/(G*H)
        // A-S-D-F-G-H => (A-S)-(D+F)-(G+H)
        List<Lexeme> outLexemes = BackwardPolishNotationUtils.calculateBPN(lexemes);
        Node expressionTree = BackwardPolishNotationUtils.buildExpressionTree(outLexemes);
        System.out.println(expressionTree);
        Node balancedExpressionTree = balance(expressionTree);
        System.out.println(balancedExpressionTree);
    }

    public static Node balance(Node root) {
        while (true) {
            if (root.leftChild.maxLength() - root.rightChild.maxLength() > 1) {
                if (root.currentLexeme.getClass() == root.leftChild.currentLexeme.getClass()) {
                    root = root.turnRight();
                } else {
                    break;
                }
            } else if (root.rightChild.maxLength() - root.leftChild.maxLength() > 1) {
                if (root.currentLexeme.getClass() == root.rightChild.currentLexeme.getClass()) {
                    root = root.turnLeft();
                } else {
                    break;
                }
            }
            else break;
        }

        if (root.leftChild.leftChild != null) {
            root.leftChild = balance(root.leftChild);
        }
        if (root.rightChild.rightChild != null || root.rightChild.leftChild != null) {
            root.rightChild = balance(root.rightChild);
        }

        return root;
    }
}
