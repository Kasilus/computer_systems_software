package edu.stanislav.computer_systems_software.lab1;

import edu.stanislav.computer_systems_software.lab1.lexer.Lexer;
import edu.stanislav.computer_systems_software.lab1.lexer.LexicalException;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.DivideOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MinusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MultiplyOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.PlusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.SinMathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.ConstantLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.HasValue;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.VariableLexeme;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class LexerTest {

    private Lexer lexer;

    @Before
    public void init() {
        lexer = new Lexer();
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void simpleExpression() throws LexicalException {
        String expression = "A + B*(C/ D)";
        List<Lexeme> lexemes = lexer.analyzeExpression(expression);
        List<Lexeme> expectedLexemes = new ArrayList<>();
        expectedLexemes.add(new VariableLexeme("A"));
        expectedLexemes.add(new PlusOperatorLexeme());
        expectedLexemes.add(new VariableLexeme("B"));
        expectedLexemes.add(new MultiplyOperatorLexeme());
        expectedLexemes.add(new LeftQuoteLexeme());
        expectedLexemes.add(new VariableLexeme("C"));
        expectedLexemes.add(new DivideOperatorLexeme());
        expectedLexemes.add(new VariableLexeme("D"));
        expectedLexemes.add(new RightQuoteLexeme());
        assertEquals(lexemes.size(), expectedLexemes.size());
        assertTrue(compareLexemes(lexemes, expectedLexemes));
    }

    @Test
    public void simpleExpressionWithMath() throws LexicalException {
        String expression = "sin (A - B) + 4";
        List<Lexeme> lexemes = lexer.analyzeExpression(expression);
        List<Lexeme> expectedLexemes = new ArrayList<>();
        expectedLexemes.add(new SinMathFunctionLexeme());
        expectedLexemes.add(new LeftQuoteLexeme());
        expectedLexemes.add(new VariableLexeme("A"));
        expectedLexemes.add(new MinusOperatorLexeme());
        expectedLexemes.add(new VariableLexeme("B"));
        expectedLexemes.add(new RightQuoteLexeme());
        expectedLexemes.add(new PlusOperatorLexeme());
        expectedLexemes.add(new ConstantLexeme("4"));
        assertEquals(lexemes.size(), expectedLexemes.size());
        assertTrue(compareLexemes(lexemes, expectedLexemes));
    }

    @Test
    public void simpleExpressionWithLotOfWhiteSpaces() throws LexicalException {
        String expression = "3 ++        12";
        List<Lexeme> lexemes = lexer.analyzeExpression(expression);
        List<Lexeme> expectedLexemes = new ArrayList<>();
        expectedLexemes.add(new ConstantLexeme("3"));
        expectedLexemes.add(new PlusOperatorLexeme());
        expectedLexemes.add(new PlusOperatorLexeme());
        expectedLexemes.add(new ConstantLexeme("12"));
        assertEquals(lexemes.size(), expectedLexemes.size());
        assertTrue(compareLexemes(lexemes, expectedLexemes));
    }

    @Test
    public void wrongCharacter() throws LexicalException {
        exceptionRule.expect(LexicalException.class);
        exceptionRule.expectMessage("Unknown character");
        String expression = "sin (# + 5) * 300";
        lexer.analyzeExpression(expression);
    }

    @Test
    public void wrongVariableName() throws LexicalException {
        exceptionRule.expect(LexicalException.class);
        exceptionRule.expectMessage("Wrong variable name");
        String expression = "A * 1B - D";
        lexer.analyzeExpression(expression);
    }

    private boolean compareLexemes(List<Lexeme> actual, List<Lexeme> expected) {
        for (int i = 0; i < expected.size(); i++) {
            Lexeme expectedLexeme = expected.get(i);
            Lexeme actualLexeme = actual.get(i);
            if (!expectedLexeme.getClass().equals(actualLexeme.getClass())) {
                return false;
            }
            if (expectedLexeme instanceof HasValue) {
                HasValue expectedLexemeWithValue = (HasValue) expectedLexeme;
                HasValue actualLexmemeWithValue = (HasValue) actualLexeme;
                if (!expectedLexemeWithValue.getValue().equals(actualLexmemeWithValue.getValue())) {
                    return false;
                }
            }
        }
        return true;
    }

}
