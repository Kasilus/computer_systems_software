package edu.stanislav.computer_systems_software.lab3;

import edu.stanislav.computer_systems_software.lab2.Lab2;
import edu.stanislav.computer_systems_software.lab2.Node;

import java.util.*;

public class Lab3 {

    public static void main(String[] args) {
        int processors = 4;
        Map<String, Integer> operationDurability = new HashMap<>();
        operationDurability.put("+", 1);
        operationDurability.put("-", 2);
        operationDurability.put("*", 3);
        operationDurability.put("/", 4);

        //        String expression = "a + b + c / d + r * s * t * 10 + m / n";
        String expression = "2 * 3 + 15 * 8 - a + b";
        Node balancedExpressionTree = Lab2.buildBalancedExpressionTree(expression);
        System.out.println("\nLab3");
        System.out.println("Balanced expression tree");
        System.out.println(balancedExpressionTree);

        // turn Node from lab2 to lab3 DataFlowNode
        DataFlowNode dataFlowTree = new DataFlowNode(balancedExpressionTree);
        System.out.println("Data Flow tree");
        System.out.println(dataFlowTree.getPrintTree());

        ParallelModel dataFlowParallelModel = new DataFlowParallelModel(processors, operationDurability);
        dataFlowParallelModel.run(dataFlowTree);

    }



}
