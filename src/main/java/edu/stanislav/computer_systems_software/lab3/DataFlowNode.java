package edu.stanislav.computer_systems_software.lab3;

import edu.stanislav.computer_systems_software.lab2.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class DataFlowNode implements GraphNode {
    private static int counter = 0;

    private DataFlowNode leftChild;
    private DataFlowNode rightChild;
    private String value;
    private int id;


    public DataFlowNode(Node node) {
        if (node.getLeftChild() != null) {
            this.leftChild = new DataFlowNode(node.getLeftChild());
        }
        if (node.getRightChild() != null) {
            this.rightChild = new DataFlowNode(node.getRightChild());
        }
        this.value = node.getCurrentLexeme().toString();
        this.id = ++counter;
    }

    @Override
    public String toString() {
        return this.value + "(" + this.id + ")";
    }

    public String getPrintTree() {
        StringBuilder buffer = new StringBuilder();
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.toString());
        buffer.append('\n');
        if (leftChild != null) {
            leftChild.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
        }
        if (rightChild != null) {
            rightChild.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
        }
    }

    public DataFlowNode getLeftChild() {
        return leftChild;
    }

    public DataFlowNode getRightChild() {
        return rightChild;
    }

    public String getValue() {
        return value;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        List<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));
        if (o instanceof DataFlowNode) {
            DataFlowNode oDataFlowNode = (DataFlowNode) o;
            if (this.value.equals(oDataFlowNode.getValue()) && operators.contains(this.getValue()) && operators.contains(oDataFlowNode.getValue())) {
                return true;
            } else {
                return this.id == oDataFlowNode.getId();
            }
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(leftChild, rightChild, value, id);
    }
}
