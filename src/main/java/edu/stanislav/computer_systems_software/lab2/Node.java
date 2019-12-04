package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.DivideOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MinusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MultiplyOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.PlusOperatorLexeme;

public class Node {
    Lexeme currentLexeme;
    Node leftChild;
    Node rightChild;
    Integer value;

    public Node(Lexeme currentLexeme) {
        this.currentLexeme = currentLexeme;
        if (currentLexeme instanceof PlusOperatorLexeme) {
            this.value = 2;
        } else if (currentLexeme instanceof MinusOperatorLexeme) {
            this.value = 1;
        } else if (currentLexeme instanceof MultiplyOperatorLexeme) {
            this.value = 4;
        } else if (currentLexeme instanceof DivideOperatorLexeme) {
            this.value = 3;
        } else {
            // ?
            this.value = 0;
        }
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    public Node turnRight() {
        Node node = this.leftChild;
        this.leftChild = node.rightChild;
        node.rightChild = this;
        return node;
    }

    public Node turnLeft() {
        Node node = this.rightChild;
        this.rightChild = node.leftChild;
        node.leftChild = this;
        return node;
    }

    public int maxLength() {
        if (this.leftSideLength() > this.rightSideLength()) {
            return this.leftSideLength();
        } else {
            return this.rightSideLength();
        }
    }

    private int leftSideLength() {
        Node node = this;
        int leftChildren = 1;
        while (node.leftChild != null) {
            leftChildren++;
            node = node.leftChild;
        }
        return leftChildren;
    }

    private int rightSideLength() {
        Node node = this;
        int rightChildren = 1;
        while (node.rightChild != null) {
            rightChildren++;
            node = node.rightChild;
        }
        return rightChildren;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this.currentLexeme.toString());
        buffer.append('\n');
        if (leftChild != null) {
            leftChild.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
        }
        if (rightChild != null) {
            rightChild.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
        }
    }
}
