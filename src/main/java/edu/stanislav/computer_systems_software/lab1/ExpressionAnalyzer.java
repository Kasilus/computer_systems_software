package edu.stanislav.computer_systems_software.lab1;

import edu.stanislav.computer_systems_software.lab1.lexer.Lexer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.parser.Parser;
import edu.stanislav.computer_systems_software.lab1.parser.TreeNode;

import java.util.List;

public class ExpressionAnalyzer {

    List<Lexeme> lexemes;
    TreeNode rootNode;

    public void analyzeExpression(String expression) {
        System.out.println("INPUT EXPRESSION\n" + expression);

        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        try {
            lexemes = lexer.analyzeExpression(expression);
            rootNode = parser.parse(lexemes);
        } catch (CompilerException e) {
            String indexString = generateIndexString(e.getIndex());
            System.out.println(indexString);
            System.out.println(e);
        }
    }

    private String generateIndexString(int exceptionIndex) {
        StringBuilder indexString = new StringBuilder();
        int i = 0;
        while (i < exceptionIndex) {
            indexString.append(" ");
            i++;
        }
        return indexString + "^";
    }

    public void printResults() {
        printLexemes();
        printParseTree();
    }

    public void printLexemes() {
        if (lexemes != null) {
            System.out.println("\nLEXEMES");
            lexemes.forEach(System.out::println);
        }
    }

    public void printParseTree() {
        if (rootNode != null) {
            System.out.println("\nPARSE TREE");
            System.out.println(rootNode.getTree());
        }
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public TreeNode getTree() {
        return rootNode;
    }
}
