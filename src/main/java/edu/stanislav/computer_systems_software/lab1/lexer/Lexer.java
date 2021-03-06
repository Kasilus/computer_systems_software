package edu.stanislav.computer_systems_software.lab1.lexer;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.GrammarLexemeFactory;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Lexer {

    private List<Character> arithmeticOperators = new ArrayList<>(Arrays.asList('+', '-', '*', '/'));
    private List<Character> quotes = new ArrayList<>(Arrays.asList('(', ')'));
    private List<String> keyWords = new ArrayList<>(Arrays.asList("sin", "cos", "tg"));
    private String variableRegexp = "[_A-Za-z][_A-Za-z0-9]*";
    private String allowedCharactersRegexp = "[A-Za-z0-9_.]";

    public List<Lexeme> analyzeExpression(String expression) throws LexicalException {
        List<Lexeme> lexemes = new ArrayList<>();
        int pointer = 0;
        String currentString = "";
        while (pointer < expression.length()) {
            char currentCharacter = expression.charAt(pointer);
            // check if allowed
            if (!isCharacterAllowed(currentCharacter)) {
                throw new LexicalException("Unknown character! Regexp = " + allowedCharactersRegexp, pointer);
            }

            if (!isCharacterIsDivider(currentCharacter)) {
                currentString += currentCharacter;
                if (isNextCharacterIsDivider(expression, pointer) || !isNextCharacterExists(expression, pointer)) {
                    // add not divider [keyword, constant, variable]
                    if (keyWords.contains(currentString)) {
                        lexemes.add(GrammarLexemeFactory.createMathFuncLexeme(currentString));
                        lexemes.get(lexemes.size() - 1).setIndex(pointer);
                    } else if (NumberUtils.isCreatable(currentString)) {
                        lexemes.add(GrammarLexemeFactory.createConstantLexeme(currentString));
                        lexemes.get(lexemes.size() - 1).setIndex(pointer);
                    } else {
                        if (currentString.matches(variableRegexp)) {
                            lexemes.add(GrammarLexemeFactory.createVariableLexeme(currentString));
                            lexemes.get(lexemes.size() - 1).setIndex(pointer - currentString.length());
                        } else {
                            throw new LexicalException("Wrong variable name! Regexp = " + variableRegexp, pointer - currentString.length() + 1);
                        }
                    }
                    currentString = "";
                }
            } else {
                // add divider [arithmetic, quote] or skip [space]
                if (arithmeticOperators.contains(currentCharacter)) {
                    lexemes.add(GrammarLexemeFactory.createArithmeticLexeme(currentCharacter));
                    lexemes.get(lexemes.size() - 1).setIndex(pointer);
                } else if (quotes.contains(currentCharacter)) {
                    lexemes.add(GrammarLexemeFactory.createQuoteLexeme(currentCharacter));
                    lexemes.get(lexemes.size() - 1).setIndex(pointer);
                } else if (currentCharacter != ' '){
                    throw new LexicalException("Unknown error", pointer);
                }
            }
            pointer++;
        }
        return lexemes;
    }

    private boolean isCharacterAllowed(char currentCharacter) {
        return isCharacterIsDivider(currentCharacter) ||
                Character.toString(currentCharacter).matches(allowedCharactersRegexp);
    }

    private boolean isCharacterIsDivider(char currentCharacter) {
        return currentCharacter == ' ' ||
                arithmeticOperators.contains(currentCharacter) ||
                quotes.contains(currentCharacter);
    }

    private boolean isNextCharacterIsDivider(String expression, int currentIndex) {
        if (expression.length() > currentIndex + 1) {
            char nextCharacter = expression.charAt(currentIndex + 1);
            return isCharacterIsDivider(nextCharacter);
        }
        return false;
    }

    private boolean isNextCharacterExists(String expression, int currentIndex) {
        return expression.length() > currentIndex + 1;
    }

}
