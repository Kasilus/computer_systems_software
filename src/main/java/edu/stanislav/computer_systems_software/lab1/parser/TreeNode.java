package edu.stanislav.computer_systems_software.lab1.parser;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;

import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

public class TreeNode {

    enum TreeNodeType {EXPRESSION, TERM, IDENTIFIER, ADDOP, MULOP, MATH, MATHFUNC, MATHEXPR, CONSTANT,
        LEFT_QUOTE, RIGHT_QUOTE, EXPRESSION_IN_QUOTES, UNARY
    }

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

    public TreeNodeType getTreeNodeType() {
        return treeNodeType;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public Lexeme getLexeme() {
        return lexeme;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TreeNode.class.getSimpleName() + "[", "]")
                .add("treeNodeType=" + treeNodeType)
                .add("lexeme=" + lexeme)
                .toString();
    }

    public String getTree() {
        StringBuilder buffer = new StringBuilder(50);
        print(buffer, "", "");
        return buffer.toString();
    }

    private void print(StringBuilder buffer, String prefix, String childrenPrefix) {
        buffer.append(prefix);
        buffer.append(this);
        buffer.append('\n');
        for (Iterator<TreeNode> it = this.getChildren().iterator(); it.hasNext();) {
            TreeNode next = it.next();
            if (it.hasNext()) {
                next.print(buffer, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                next.print(buffer, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }
}
