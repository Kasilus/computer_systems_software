package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.*;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.CosMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.MathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.SinMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.TgMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.HasValue;

import java.util.*;

public class BackwardPolishNotationUtils {

    public static List<Lexeme> calculateBPN(List<Lexeme> lexemes) {
        if (lexemes == null || lexemes.isEmpty()) {
            return Collections.emptyList();
        }
        lexemes.add(new RightQuoteLexeme());
        List<Lexeme> outLexemes = new ArrayList<>();
        Stack<Lexeme> operatorsStack = new Stack<>();
        operatorsStack.push(new LeftQuoteLexeme());
        Map<Class, Integer> operationPriorities = new HashMap<>();
        operationPriorities.put(PlusOperatorLexeme.class, 1);
        operationPriorities.put(MinusOperatorLexeme.class, 1);
        operationPriorities.put(MultiplyOperatorLexeme.class, 2);
        operationPriorities.put(DivideOperatorLexeme.class, 2);
        operationPriorities.put(SinMathFunctionLexeme.class, 3);
        operationPriorities.put(CosMathFunctionLexeme.class, 3);
        operationPriorities.put(TgMathFunctionLexeme.class, 3);
        for (Lexeme lexeme: lexemes) {
            System.out.println("Current lexeme = " + lexeme);
            System.out.println("outLexemes = " + outLexemes);
            System.out.println("operatorsStack = " + operatorsStack);
            System.out.println();
            if (lexeme instanceof HasValue) {
                outLexemes.add(lexeme);
                continue;
            }
            if (lexeme instanceof ArithmeticOperatorLexeme || lexeme instanceof MathFunctionLexeme) {
                Lexeme previousLexeme = null;
                if (!operatorsStack.isEmpty()) {
                    previousLexeme = operatorsStack.pop();
                }
                if ((previousLexeme instanceof ArithmeticOperatorLexeme || previousLexeme instanceof MathFunctionLexeme) && !operationPriorities.get(previousLexeme.getClass()).equals(operationPriorities.get(lexeme.getClass()))) {
                    while (operationPriorities.get(previousLexeme.getClass()) >= operationPriorities.get(lexeme.getClass())){
                        outLexemes.add(previousLexeme);
                        if (operatorsStack.isEmpty()) {
                            break;
                        }
                        previousLexeme = operatorsStack.pop();
                        if (!(previousLexeme instanceof ArithmeticOperatorLexeme) && !(previousLexeme instanceof MathFunctionLexeme)) {
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

        return outLexemes;
    }

    public static Node buildExpressionTree(List<Lexeme> lexemesInBPN) {
        Stack<Node> expressionTreeStack = new Stack<>();
        Node operatorNode;
        for (Lexeme lexeme: lexemesInBPN) {
            if (lexeme instanceof HasValue) {
                expressionTreeStack.push(new Node(lexeme));
                continue;
            }
            operatorNode = new Node(lexeme);
            operatorNode.setRightChild(expressionTreeStack.pop());
            if (!(operatorNode.currentLexeme instanceof MathFunctionLexeme)) {
                operatorNode.setLeftChild(expressionTreeStack.pop());
            }
            expressionTreeStack.push(operatorNode);
        }

        operatorNode = expressionTreeStack.pop();
        return operatorNode;
    }

}
