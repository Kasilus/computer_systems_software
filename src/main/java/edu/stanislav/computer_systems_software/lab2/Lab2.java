package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.ExpressionAnalyzer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.*;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.HasValue;

import java.util.*;

public class Lab2 {
    public static void main(String[] args) {

        String expression = "(A+B)-C/D+G*(K/L+M+N)";
        ExpressionAnalyzer analyzer = new ExpressionAnalyzer();
        analyzer.analyzeExpression(expression);
//        analyzer.printLexemes();
        List<Lexeme> lexemes = analyzer.getLexemes();
        lexemes.add(new RightQuoteLexeme());
        if (lexemes == null || lexemes.isEmpty()) {
            return;
        }
        // change sequences on that, which could be paralleled

        // backward polish notation
        List<Lexeme> outLexemes = new ArrayList<>();
        Stack<Lexeme> operatorsStack = new Stack<>();
        operatorsStack.push(new LeftQuoteLexeme());
        Map<Class, Integer> operationPriorities = new HashMap<>();
        operationPriorities.put(PlusOperatorLexeme.class, 1);
        operationPriorities.put(MinusOperatorLexeme.class, 1);
        operationPriorities.put(MultiplyOperatorLexeme.class, 2);
        operationPriorities.put(DivideOperatorLexeme.class, 2);
        for (Lexeme lexeme: lexemes) {
            System.out.println("Current lexeme = " + lexeme);
            System.out.println("outLexemes = " + outLexemes);
            System.out.println("operatorsStack = " + operatorsStack);
            System.out.println();
            if (lexeme instanceof HasValue) {
                outLexemes.add(lexeme);
                continue;
            }
            if (lexeme instanceof ArithmeticOperatorLexeme) {
                Lexeme previousLexeme = null;
                if (!operatorsStack.isEmpty()) {
                    previousLexeme = operatorsStack.pop();
                }
                if (previousLexeme instanceof ArithmeticOperatorLexeme && operationPriorities.get(previousLexeme.getClass()) != operationPriorities.get(lexeme.getClass())) {
                    while (operationPriorities.get(previousLexeme.getClass()) >= operationPriorities.get(lexeme.getClass())){
                        outLexemes.add(previousLexeme);
                        if (operatorsStack.isEmpty()) {
                            break;
                        }
                        previousLexeme = operatorsStack.pop();
                        if (!(previousLexeme instanceof ArithmeticOperatorLexeme)) {
                            break;
                        }
                    }
                }
                operatorsStack.push(previousLexeme);
                operatorsStack.push(lexeme);
                continue;
            }
            if (lexeme instanceof LeftQuoteLexeme) {
                operatorsStack.push(lexeme);
                continue;
            }
            if (lexeme instanceof RightQuoteLexeme) {
                Lexeme currentLexeme = operatorsStack.pop();
                while (!(currentLexeme instanceof LeftQuoteLexeme) && !operatorsStack.isEmpty()) {
                    outLexemes.add(currentLexeme);
                    currentLexeme = operatorsStack.pop();
                }
            }
        }

        System.out.println("outLexemes = " + outLexemes);
        System.out.println("operatorsStack = " + operatorsStack);

        // backward polish notation with sin, cos, unary '-'

        // build expression tree
        Stack<Node> expressionTreeStack = new Stack<>();
        Node operatorNode;
        for (Lexeme lexeme: outLexemes) {
            if (lexeme instanceof HasValue) {
                expressionTreeStack.push(new Node(lexeme));
                continue;
            }
            operatorNode = new Node(lexeme);
            operatorNode.setRightChild(expressionTreeStack.pop());
            operatorNode.setLeftChild(expressionTreeStack.pop());
            expressionTreeStack.push(operatorNode);
        }

        operatorNode = expressionTreeStack.pop();
        System.out.println(operatorNode);

        // output
    }
}
