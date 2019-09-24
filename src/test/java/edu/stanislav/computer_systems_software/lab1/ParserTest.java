package edu.stanislav.computer_systems_software.lab1;

import edu.stanislav.computer_systems_software.lab1.lexer.Lexer;
import edu.stanislav.computer_systems_software.lab1.lexer.LexicalException;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.parser.ParseException;
import edu.stanislav.computer_systems_software.lab1.parser.Parser;

import edu.stanislav.computer_systems_software.lab1.parser.TreeNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;

public class ParserTest {

    private Lexer lexer;
    private Parser parser;

    @Before
    public void init() {
        lexer = new Lexer();
        parser = new Parser();
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void wrongLexeme() throws LexicalException, ParseException {
        exceptionRule.expect(ParseException.class);
        exceptionRule.expectMessage("Wrong lexeme! There should be variable, constant, math func or left quote for new expression start");
        List<Lexeme> lexemes = lexer.analyzeExpression("A + 1 -* 60");
        parser.parse(lexemes);
    }

    @Test
    public void closingParenthesesMissed() throws LexicalException, ParseException {
        exceptionRule.expect(ParseException.class);
        exceptionRule.expectMessage("Should be closing parentheses");
        List<Lexeme> lexemes = lexer.analyzeExpression("A + (12 - 3");
        parser.parse(lexemes);
    }

    @Test
    public void badEnd_1() throws LexicalException, ParseException {
        exceptionRule.expect(ParseException.class);
        exceptionRule.expectMessage("Bad end for expression. Should be term after this lexeme");
        List<Lexeme> lexemes = lexer.analyzeExpression("A + (12 - 3) *");
        parser.parse(lexemes);
    }

    @Test
    public void badEnd_2() throws LexicalException, ParseException {
        exceptionRule.expect(ParseException.class);
        exceptionRule.expectMessage("End of expression");
        List<Lexeme> lexemes = lexer.analyzeExpression("70 - 300 A + B");
        parser.parse(lexemes);
    }

    @Test
    public void manyQuotes() throws LexicalException, ParseException {
        List<Lexeme> lexemes = lexer.analyzeExpression("2 + (2 - 2 * (3 + 15 / (12 + A)))");
        TreeNode root = parser.parse(lexemes);
        Assert.assertNotNull(root);
    }

}
