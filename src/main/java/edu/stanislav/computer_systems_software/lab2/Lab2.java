package edu.stanislav.computer_systems_software.lab2;

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
        String expression6 = "A/B/C/D/E/F";
        String expression0 = "A-B-C-X+D-E-F-Y";
        String expression5 = "A-B-(5+12)";
        String expression7 = "A-B-C-D-E-F";
        String expression1 = "1-(2+3+4+5+6+7+8+9)";
        String expression4 = "A/(B*C*D*E*F*Z)+1-(2+12+6+3000000)+3";
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
        analyzer.printResults();
        if (!analyzer.getAnalysisSuccessful()) {
            return;
        }
        List<Lexeme> lexemes = analyzer.getLexemes();
        List<Lexeme> outLexemes = BackwardPolishNotationUtils.calculateBPN(lexemes);
        Node expressionTree = BackwardPolishNotationUtils.buildExpressionTree(outLexemes);
        System.out.println("\nNot balanced expression tree");
        System.out.println(expressionTree);
        Node balancedExpressionTree = balance(expressionTree);
        System.out.println("\nBalanced expression tree");
        System.out.println(balancedExpressionTree);
    }

    public static Node balance(Node root) {
        int result = 0;
        while (true) {
            if (result == 2) {
                // WA when branches are equivalent
                break;
            }
            if (root.leftChild.maxLength() - root.rightChild.maxLength() > 1) {
                if (root.value.equals(root.leftChild.value)) {
                    root = root.turnRight();
                    result++;
                } else {
                    break;
                }
            } else if (root.rightChild.maxLength() - root.leftChild.maxLength() > 1) {
                if (root.value.equals(root.rightChild.value)) {
                    root = root.turnLeft();
                    result++;
                } else {
                    break;
                }
            } else {
                break;
            }
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
