package edu.stanislav.computer_systems_software.lab1.lexer;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.GrammarLexemeFactory;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Lexer {

    private List<Character> arithmeticOperators = new ArrayList<>(Arrays.asList('+', '-', '*', '/'));
    private List<Character> quotes = new ArrayList<>(Arrays.asList('(', ')'));
    private List<String> keyWords = new ArrayList<>(Arrays.asList("sin", "cos", "tg"));
    private String variableRegexp = "[_A-Za-z][_A-Za-z0-9]*";
    private String allowedCharactersRegexp = "[A-Za-z0-9_]";

    public List<Lexeme> analyzeExpression(String expression) throws LexicalException {
        List<Lexeme> lexemes = new ArrayList<>();
        int pointer = 0;
        String currentString = "";
        while (pointer < expression.length()) {
            char currentCharacter = expression.charAt(pointer);
            // check whitespace
            if (isCurrentCharacterIsDivider(currentCharacter) || pointer == expression.length() - 1) {
                // check variable, constant, keyWords (more one character)
                if (!currentString.isEmpty()) {
                    // keywords
                    if (keyWords.contains(currentString)) {
                        lexemes.add(GrammarLexemeFactory.createMathFuncLexeme(currentString));
                    } else if (StringUtils.isNumeric(currentString)) {
                        lexemes.add(GrammarLexemeFactory.createConstantLexeme(currentString));
                    } else {
                        // check constant
                        Pattern pattern = Pattern.compile(variableRegexp);
                        if (pattern.matcher(currentString).matches()) {
                            lexemes.add(GrammarLexemeFactory.createVariableLexeme(currentString));
                        } else {
                            throw new LexicalException("Wrong variable name! Regexp = " + variableRegexp, pointer - currentString.length() + 1);
                        }
                    }
                }

                if (arithmeticOperators.contains(currentCharacter)) {
                    lexemes.add(GrammarLexemeFactory.createArithmeticLexeme(currentCharacter));
                } else if (quotes.contains(currentCharacter)) {
                    lexemes.add(GrammarLexemeFactory.createQuoteLexeme(currentCharacter));
                }
                currentString = "";
            } else {
                if (Character.toString(currentCharacter).matches(allowedCharactersRegexp)) {
                    currentString += currentCharacter;
                } else {
                    throw new LexicalException("Wrong character! Regexp = " + allowedCharactersRegexp, pointer);
                }
            }
            pointer++;
        }
        return lexemes;
    }

    private boolean isCurrentCharacterIsDivider(char currentCharacter) {
        return currentCharacter == ' ' ||
                arithmeticOperators.contains(currentCharacter) ||
                quotes.contains(currentCharacter);
    }

}
