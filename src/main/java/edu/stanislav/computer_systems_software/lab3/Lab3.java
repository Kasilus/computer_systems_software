package edu.stanislav.computer_systems_software.lab3;

import edu.stanislav.computer_systems_software.lab2.Lab2;
import edu.stanislav.computer_systems_software.lab2.Node;

public class Lab3 {
    public static void main(String[] args) {

        String expression = "a + b + c / d + r * s * t * 10 + m / n";
        Node balancedExpressionTree = Lab2.buildBalancedExpressionTree(expression);
        System.out.println("\nLab3");
        System.out.println("Balanced expression tree");
        System.out.println(balancedExpressionTree);
    }
}
