package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.Constants;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.UnaryMinusLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.*;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.CosMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.MathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.SinMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.TgMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.ConstantLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.HasValue;

import java.util.*;

public class BackwardPolishNotationUtils {

    private static Map<Class, Integer> operationPriorities = new HashMap<>();

    /*
    precedence are :
            * > / > + > - > ) > ( > any operand
    */

    static {
        operationPriorities.put(LeftQuoteLexeme.class, 1);
        operationPriorities.put(RightQuoteLexeme.class, 2);
        operationPriorities.put(MinusOperatorLexeme.class, 3);
        operationPriorities.put(PlusOperatorLexeme.class, 4);
        operationPriorities.put(DivideOperatorLexeme.class, 5);
        operationPriorities.put(MultiplyOperatorLexeme.class, 6);
        operationPriorities.put(SinMathFunctionLexeme.class, 7);
        operationPriorities.put(CosMathFunctionLexeme.class, 7);
        operationPriorities.put(TgMathFunctionLexeme.class, 7);
    }

    public static List<Lexeme> calculateBPN(List<Lexeme> inLexemes) {
        if (inLexemes == null || inLexemes.isEmpty()) {
            return Collections.emptyList();
        }
        List<Lexeme> lexemes = replaceUnaryMinuses(inLexemes);
        System.out.println("\nLexemes after unary minuses replace");
        System.out.println(lexemes);
        lexemes = replaceRepeatedMinusesAndDivisions(lexemes);
        System.out.println("\nLexemes after repeated minuses and divisions replace");
        System.out.println(lexemes);
        lexemes.add(new RightQuoteLexeme());
        List<Lexeme> outLexemes = new ArrayList<>();
        Stack<Lexeme> operatorsStack = new Stack<>();
        operatorsStack.push(new LeftQuoteLexeme());
        for (Lexeme lexeme: lexemes) {
            if (Constants.FULL_PRINT || Constants.FULL_BPN_PRINT) {
                System.out.println("Current lexeme = " + lexeme);
                System.out.println("outLexemes = " + outLexemes);
                System.out.println("operatorsStack = " + operatorsStack);
                System.out.println();
            }
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

        System.out.println("\nLexemes after Backward Polish Notation (BPN)");
        System.out.println(outLexemes);

        return outLexemes;
    }

    private static List<Lexeme> replaceUnaryMinuses(List<Lexeme> lexemes) {
        List<Lexeme> lexemesWithReplacedUnaryMinuses = new ArrayList<>();
        int counter = 0;
        if (lexemes.get(0) instanceof MinusOperatorLexeme) {
            addUnaryMinusReplace(lexemesWithReplacedUnaryMinuses, (HasValue) lexemes.get(1));
            counter+=2;
        }
        for (int i = counter; i < lexemes.size() - 1; i++) {
            Lexeme curLexeme = lexemes.get(i);
            if(!(curLexeme instanceof MinusOperatorLexeme)) {
                lexemesWithReplacedUnaryMinuses.add(curLexeme);
                continue;
            }
            Lexeme prevLexeme = lexemes.get(i - 1);
            Lexeme nextLexeme = lexemes.get(i + 1);
            if (nextLexeme instanceof HasValue && (!(prevLexeme instanceof HasValue)) && !(prevLexeme instanceof RightQuoteLexeme)) {
                addUnaryMinusReplace(lexemesWithReplacedUnaryMinuses, (HasValue) nextLexeme);
            }
            i++;
        }
        lexemesWithReplacedUnaryMinuses.add(lexemes.get(lexemes.size() - 1));
        return lexemesWithReplacedUnaryMinuses;
    }

    private static void addUnaryMinusReplace(List<Lexeme> lexemesWithReplacedUnaryMinuses, HasValue lexemeToAdd) {
        lexemesWithReplacedUnaryMinuses.add(new LeftQuoteLexeme());
        lexemesWithReplacedUnaryMinuses.add(new ConstantLexeme("0"));
        lexemesWithReplacedUnaryMinuses.add(new MinusOperatorLexeme());
        lexemesWithReplacedUnaryMinuses.add((Lexeme) lexemeToAdd);
        lexemesWithReplacedUnaryMinuses.add(new RightQuoteLexeme());
    }

    private static List<Lexeme> replaceRepeatedMinusesAndDivisions(List<Lexeme> lexemes) {
        List<Lexeme> changedLexemes = new ArrayList<>();
        for (int i = 0; i < lexemes.size(); i++) {
            if (!(lexemes.get(i) instanceof MinusOperatorLexeme) && !(lexemes.get(i) instanceof DivideOperatorLexeme)) {
                changedLexemes.add(lexemes.get(i));
            } else {
                Class lexemeToChangeClass = lexemes.get(i).getClass();
                if ((i < lexemes.size() - 2) && (lexemes.get(i+1) instanceof HasValue && (lexemes.get(i+2).getClass() == lexemeToChangeClass) && lexemes.get(i+3) instanceof HasValue)) {
                    changedLexemes.add(lexemes.get(i));
                    changedLexemes.add(new LeftQuoteLexeme());
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
                } else {
                    changedLexemes.add(lexemes.get(i));
                }
            }
        }
        return changedLexemes;
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
