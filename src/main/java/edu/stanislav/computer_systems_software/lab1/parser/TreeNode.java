package edu.stanislav.computer_systems_software.lab1.parser;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.List;

public class TreeNode {

    enum TreeNodeType {EXPRESSION, TERM, PRIMARY, IDENTIFIER, ADDOP, MULOP, MATH, MATHFUNC, MATHEXPR, CONSTANT, LEFT_QUOTE, RIGHT_QUOTE}

    private TreeNodeType treeNodeType;
    private List<TreeNode> children;
    // for ALL after PRIMARY
    private Lexeme lexeme;

    public void setTreeNodeType(TreeNodeType treeNodeType) {
        this.treeNodeType = treeNodeType;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public void setLexeme(Lexeme lexeme) {
        this.lexeme = lexeme;
    }
}
