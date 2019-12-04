package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.UnaryMinusLexeme;
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

    private static Map<Class, Integer> operationPriorities = new HashMap<>();

    /*
    precedence are :
            * > ^ > / > % > + > - > ) > ( > any operand
    */

    static {
        operationPriorities.put(LeftQuoteLexeme.class, 1);
        operationPriorities.put(RightQuoteLexeme.class, 2);
        operationPriorities.put(MinusOperatorLexeme.class, 3);
        operationPriorities.put(PlusOperatorLexeme.class, 4);
        operationPriorities.put(MultiplyOperatorLexeme.class, 6);
        operationPriorities.put(DivideOperatorLexeme.class, 5);
//        operationPriorities.put(SinMathFunctionLexeme.class, 3);
//        operationPriorities.put(CosMathFunctionLexeme.class, 3);
//        operationPriorities.put(TgMathFunctionLexeme.class, 3);
//        operationPriorities.put(UnaryMinusLexeme.class, 3);
    }

    public static List<Lexeme> calculateBPN(List<Lexeme> inLexemes) {
        if (inLexemes == null || inLexemes.isEmpty()) {
            return Collections.emptyList();
        }
        List<Lexeme> lexemes = replaceUnaryMinuses(inLexemes);
        lexemes.add(new RightQuoteLexeme());
        List<Lexeme> outLexemes = new ArrayList<>();
        Stack<Lexeme> operatorsStack = new Stack<>();
        operatorsStack.push(new LeftQuoteLexeme());
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
                    previousLexeme = operatorsStack.peek();
                }
                if (previousLexeme != null &&
                        operationPriorities.get(previousLexeme.getClass()) > operationPriorities.get(lexeme.getClass())) {
                    while (!(previousLexeme instanceof LeftQuoteLexeme)) {
                        previousLexeme = operatorsStack.pop();
                        outLexemes.add(previousLexeme);
                        if (operatorsStack.isEmpty()) {
                            break;
                        }
                        previousLexeme = operatorsStack.peek();
                    }
                }
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

    private static List<Lexeme> replaceUnaryMinuses(List<Lexeme> lexemes) {
        List<Lexeme> lexemesWithReplacedUnaryMinuses = new ArrayList<>(lexemes);
        if (lexemes.get(0) instanceof MinusOperatorLexeme) {
            lexemesWithReplacedUnaryMinuses.set(0, new UnaryMinusLexeme());
        }
        // change unary minuses (except first [already] and last [not needed])
        for (int i = 1; i < lexemes.size() - 1; i++) {
            Lexeme curLexeme = lexemes.get(i);
            if(!(curLexeme instanceof MinusOperatorLexeme)) {
                continue;
            }
            Lexeme prevLexeme = lexemes.get(i - 1);
            Lexeme nextLexeme = lexemes.get(i + 1);
            if (nextLexeme instanceof HasValue && (!(prevLexeme instanceof HasValue)) && !(prevLexeme instanceof RightQuoteLexeme)) {
                lexemesWithReplacedUnaryMinuses.set(i, new UnaryMinusLexeme());
            }
        }
        return lexemesWithReplacedUnaryMinuses;
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
            if (!(operatorNode.currentLexeme instanceof MathFunctionLexeme) && !(operatorNode.currentLexeme instanceof UnaryMinusLexeme)) {
                operatorNode.setLeftChild(expressionTreeStack.pop());
            }
            expressionTreeStack.push(operatorNode);
        }

        operatorNode = expressionTreeStack.pop();
        return operatorNode;
    }

}
