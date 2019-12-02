package edu.stanislav.computer_systems_software.lab1;

public class Lab1 {
    public static void main(String[] args) {
        ExpressionAnalyzer expressionAnalyzer = new ExpressionAnalyzer();
        String expression = "(2 + (2 - 2 * (3 + 15 / (12 + A))))";
        expressionAnalyzer.analyzeExpression(expression);
        expressionAnalyzer.printResults();
    }
}
