package edu.stanislav.computer_systems_software.lab1;

import edu.stanislav.computer_systems_software.Constants;
import edu.stanislav.computer_systems_software.lab1.lexer.Lexer;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.parser.Parser;
import edu.stanislav.computer_systems_software.lab1.parser.TreeNode;

import java.util.List;

public class ExpressionAnalyzer {

    private List<Lexeme> lexemes;
    private TreeNode rootNode;
    private Boolean isAnalysisSuccessful = null;

    public void analyzeExpression(String expression) {
        System.out.println("Input expression\n" + expression);
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        try {
            lexemes = lexer.analyzeExpression(expression);
            rootNode = parser.parse(lexemes);
            isAnalysisSuccessful = Boolean.TRUE;
        } catch (CompilerException e) {
            String indexString = generateIndexString(e.getIndex());
            System.out.println(indexString);
            System.out.println(e);
            isAnalysisSuccessful = Boolean.FALSE;
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
        if (isAnalysisSuccessful) {
            printLexemes();
            printParseTree();
        } else {
            System.out.println("");
        }

    }

    public void printLexemes() {
        if (lexemes != null) {
            System.out.println("\nLexemes");
            System.out.println(lexemes);
        }
    }

    public void printParseTree() {
        if (rootNode != null) {
            if (Constants.FULL_PRINT) {
                System.out.println("\nParse tree");
                System.out.println(rootNode.getTree());
            }
        }
    }

    public List<Lexeme> getLexemes() {
        return lexemes;
    }

    public TreeNode getTree() {
        return rootNode;
    }

    public Boolean getAnalysisSuccessful() {
        return isAnalysisSuccessful;
    }
}
