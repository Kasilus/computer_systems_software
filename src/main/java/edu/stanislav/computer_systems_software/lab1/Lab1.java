package edu.stanislav.computer_systems_software.lab1;

import edu.stanislav.computer_systems_software.lab1.lexer.Lexer;
import edu.stanislav.computer_systems_software.lab1.lexer.LexicalException;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.parser.ParseException;
import edu.stanislav.computer_systems_software.lab1.parser.Parser;
import edu.stanislav.computer_systems_software.lab1.parser.TreeNode;

import java.util.List;

public class Lab1 {

    // arithmetic

    public static void main(String[] args) {

        // TODO: read from CL or/and GUI
//        String expression = "190+(_12A+B)+C/D+G+(K/L+M+N) * sin(A-B)";
        String expression = "A + B*(C/ D";
        System.out.println("INPUT EXPRESSION\n" + expression);

        // lexer
        Lexer lexer = new Lexer();
        List<Lexeme> lexemes = null;
        try {
            lexemes = lexer.analyzeExpression(expression);
        } catch (LexicalException e) {
            String indexString = generateIndexString(e.getIndex());
            System.out.println(indexString);
            System.out.println(e);
        }

        if (lexemes != null) {
            System.out.println("\nLEXEMES");
            lexemes.forEach(System.out::println);
        }

        // parser
        Parser parser = new Parser();
        TreeNode rootNode = null;
        try {
            rootNode = parser.parse(lexemes);
        } catch (ParseException e) {
            String indexString = generateIndexString(e.getIndex());
            System.out.println(indexString);
            System.out.println(e);
        }

        // print tree
        if (rootNode != null) {
            System.out.println(rootNode.getTree());
        }
    }

    private static String generateIndexString(int exceptionIndex) {
        StringBuilder indexString = new StringBuilder();
        int i = 0;
        while (i < exceptionIndex) {
            indexString.append(" ");
            i++;
        }
        return indexString + "^";
    }

}
