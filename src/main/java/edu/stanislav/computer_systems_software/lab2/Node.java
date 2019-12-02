package edu.stanislav.computer_systems_software.lab2;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

public class Node {
    Lexeme currentLexeme;
    Node leftChild;
    Node rightChild;

    public Node(Lexeme currentLexeme) {
        this.currentLexeme = currentLexeme;
    }

    public void setCurrentLexeme(Lexeme currentLexeme) {
        this.currentLexeme = currentLexeme;
    }

    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
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
