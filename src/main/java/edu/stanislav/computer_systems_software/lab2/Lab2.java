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
        String expression6 = "A/B/C/D/E/F";
        String expression = "A-B-C-X+D-E-F-Y";
        String expression5 = "A-B-(5+12)";
        String expression7 = "A-B-C-D-E-F";
        String expression1 = "1-(2+3+4+5+6+7+8+9)";
        String expression4 = "A/(B*C*D*E*F*Z)+1-(2+12+6+3000000)+3";
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
        analyzer.printResults();
        List<Lexeme> lexemes = analyzer.getLexemes();
        // change minuses on pluses and / on *
        Iterator<Lexeme> lexemeIterator = lexemes.iterator();
        List<Lexeme> changedLexemes = new ArrayList<>();
        for (int i = 0; i < lexemes.size(); i++) {
            if (lexemes.get(i) instanceof HasValue) {
                changedLexemes.add(lexemes.get(i));
            }
            // check for minus sequence
            if (lexemes.get(i) instanceof MinusOperatorLexeme || lexemes.get(i) instanceof DivideOperatorLexeme) {
                Class lexemeToChangeClass = lexemes.get(i).getClass();
                // check that next is a valuable
                if (i < lexemes.size() - 2) {
                    if (lexemes.get(i+1) instanceof HasValue && (lexemes.get(i+2).getClass() == lexemeToChangeClass) && lexemes.get(i+3) instanceof HasValue) {
                        // add minus or division
                        changedLexemes.add(lexemes.get(i));
                        // add quote
                        changedLexemes.add(new LeftQuoteLexeme());
                        // add first valuable
                        changedLexemes.add(lexemes.get(i + 1));
                        i+=2;
                        while (i <= lexemes.size() - 2 && lexemes.get(i).getClass() == lexemeToChangeClass) {
                            if (lexemeToChangeClass == MinusOperatorLexeme.class) {
                                changedLexemes.add(new PlusOperatorLexeme());
                            } else if(lexemeToChangeClass == DivideOperatorLexeme.class) {
                                changedLexemes.add(new MultiplyOperatorLexeme());
                            }
                            changedLexemes.add(lexemes.get(i+1));
                            i+=2;
                        }
                        changedLexemes.add(new RightQuoteLexeme());
                    }
                } else {
                    changedLexemes.add(lexemes.get(i));
                }
            }

            if (i < lexemes.size() && (lexemes.get(i) instanceof PlusOperatorLexeme || lexemes.get(i) instanceof MultiplyOperatorLexeme)) {
                changedLexemes.add(lexemes.get(i));
            }
        }
        System.out.println(changedLexemes);
        List<Lexeme> outLexemes = BackwardPolishNotationUtils.calculateBPN(lexemes);
        Node expressionTree = BackwardPolishNotationUtils.buildExpressionTree(outLexemes);
        System.out.println(expressionTree);
        Node balancedExpressionTree = balance(expressionTree);
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
