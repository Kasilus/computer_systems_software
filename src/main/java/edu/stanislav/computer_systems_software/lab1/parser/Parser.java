package edu.stanislav.computer_systems_software.lab1.parser;

import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.Lexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.DivideOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MinusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.MultiplyOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.arithmetic.PlusOperatorLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.math.MathFunctionLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.LeftQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.quotes.RightQuoteLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.ConstantLexeme;
import edu.stanislav.computer_systems_software.lab1.lexer.lexemes.valuable.VariableLexeme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Parser {

    private List<Lexeme> lexemes = null;
    private Iterator<Lexeme> lexemeIterator = null;
    private Lexeme currentLexeme = null;
    private int pointer = 0;

    public TreeNode parse(List<Lexeme> lexemes) throws ParseException {
        this.lexemes = lexemes;
        this.lexemeIterator = lexemes.iterator();
        this.currentLexeme = lexemeIterator.next();
        TreeNode root = checkExpression();
        return root;
    }

    private TreeNode checkExpression() throws ParseException {
        TreeNode expression = new TreeNode();
        expression.setTreeNodeType(TreeNode.TreeNodeType.EXPRESSION);
        List<TreeNode> children = new ArrayList<>();
        TreeNode term1 = checkTerm();
        children.add(term1);
        TreeNode addOp = checkAddOp();
        if (addOp != null) {
            children.add(addOp);
            TreeNode term2 = checkTerm();
            children.add(term2);
        }
        expression.setChildren(children);
        return expression;
    }

    private TreeNode checkTerm() throws ParseException {
        TreeNode term = new TreeNode();
        term.setTreeNodeType(TreeNode.TreeNodeType.TERM);
        List<TreeNode> children = new ArrayList<>();
        TreeNode primary1 = checkPrimary();
        children.add(primary1);
        TreeNode mulOp = checkMulOp();
        if (mulOp != null) {
            children.add(mulOp);
            TreeNode primary2 = checkPrimary();
            children.add(primary2);
        }
        term.setChildren(children);
        return term;
    }

    private TreeNode checkPrimary() throws ParseException {
        TreeNode primary = new TreeNode();
        primary.setTreeNodeType(TreeNode.TreeNodeType.PRIMARY);
        if (this.currentLexeme instanceof VariableLexeme) {
            primary.setChildren(new ArrayList<>(Collections.singletonList(checkIdentifier())));
        } else if (this.currentLexeme instanceof ConstantLexeme) {
            primary.setChildren(new ArrayList<>(Collections.singletonList(checkConstant())));
        } else if (this.currentLexeme instanceof MathFunctionLexeme) {
            primary.setChildren(new ArrayList<>(Collections.singletonList(checkMath())));
        } else if (this.currentLexeme instanceof LeftQuoteLexeme) {
            List<TreeNode> children = new ArrayList<>();
            TreeNode leftQuote = new TreeNode();
            // check left upper (maybe, is needed there)
            leftQuote.setTreeNodeType(TreeNode.TreeNodeType.LEFT_QUOTE);
            leftQuote.setChildren(new ArrayList<>());
            leftQuote.setLexeme(currentLexeme);
            this.currentLexeme = lexemeIterator.next();
            TreeNode expression = checkExpression();
            TreeNode rightQuote = new TreeNode();
            // check right
            rightQuote.setTreeNodeType(TreeNode.TreeNodeType.RIGHT_QUOTE);
            rightQuote.setChildren(new ArrayList<>());
            rightQuote.setLexeme(currentLexeme);
            this.currentLexeme = lexemeIterator.next();
            children.add(leftQuote);
            children.add(expression);
            children.add(rightQuote);
            primary.setChildren(children);
        } else {
            throw new ParseException("Wrong lexeme! There should be variable, constant, math func or left quote for new expression start", currentLexeme.getIndex());
        }
        return primary;
    }

    private TreeNode checkAddOp() {
        TreeNode addOp = null;
        if (this.currentLexeme instanceof PlusOperatorLexeme || this.currentLexeme instanceof MinusOperatorLexeme) {
            addOp = new TreeNode();
            addOp.setTreeNodeType(TreeNode.TreeNodeType.ADDOP);
            addOp.setChildren(new ArrayList<>());
            addOp.setLexeme(this.currentLexeme);
            this.currentLexeme = lexemeIterator.next();
        }
        return addOp;
    }

    private TreeNode checkMulOp() {
        TreeNode mulOp = null;
        if (this.currentLexeme instanceof MultiplyOperatorLexeme || this.currentLexeme instanceof DivideOperatorLexeme) {
            mulOp = new TreeNode();
            mulOp.setTreeNodeType(TreeNode.TreeNodeType.MULOP);
            mulOp.setChildren(new ArrayList<>());
            mulOp.setLexeme(this.currentLexeme);
            this.currentLexeme = lexemeIterator.next();
        }
        return mulOp;
    }

    private TreeNode checkMath() throws ParseException {
        TreeNode math = new TreeNode();
        math.setTreeNodeType(TreeNode.TreeNodeType.MATH);
        List<TreeNode> children = new ArrayList<>();
        TreeNode mathFunc = checkMathFunc();
        children.add(mathFunc);
        TreeNode mathExpr = checkMathExpr();
        children.add(mathExpr);
        math.setChildren(children);
        return math;
    }

    private TreeNode checkMathFunc() {
        TreeNode mathFunc = null;
        if (this.currentLexeme instanceof MathFunctionLexeme) {
            mathFunc = new TreeNode();
            mathFunc.setTreeNodeType(TreeNode.TreeNodeType.MATHFUNC);
            mathFunc.setChildren(new ArrayList<>());
            mathFunc.setLexeme(currentLexeme);
            this.currentLexeme = lexemeIterator.next();
        }
        return mathFunc;
    }

    private TreeNode checkMathExpr() throws ParseException {
        TreeNode mathExpr = new TreeNode();
        mathExpr.setTreeNodeType(TreeNode.TreeNodeType.MATHEXPR);
        TreeNode identifier = checkIdentifier();
        if (identifier != null) {
            mathExpr.setChildren(new ArrayList<>(Collections.singletonList(identifier)));
            return mathExpr;
        }
        TreeNode constant = checkConstant();
        if (constant != null) {
            mathExpr.setChildren(new ArrayList<>(Collections.singletonList(constant)));
            return mathExpr;
        }
        if (this.currentLexeme instanceof LeftQuoteLexeme) {
            List<TreeNode> children = new ArrayList<>();
            TreeNode leftQuote = new TreeNode();
            // check left upper (maybe, is needed there)
            leftQuote.setTreeNodeType(TreeNode.TreeNodeType.LEFT_QUOTE);
            leftQuote.setChildren(new ArrayList<>());
            leftQuote.setLexeme(this.currentLexeme);
            this.currentLexeme = lexemeIterator.next();
            TreeNode expression = checkExpression();
            TreeNode rightQuote = null;
            if (this.currentLexeme instanceof RightQuoteLexeme) {
                rightQuote = new TreeNode();
                // check right
                rightQuote.setTreeNodeType(TreeNode.TreeNodeType.RIGHT_QUOTE);
                rightQuote.setChildren(new ArrayList<>());
                rightQuote.setLexeme(this.currentLexeme);
                this.currentLexeme = lexemeIterator.next();
            } else {
                throw new ParseException("Should be closing parentheses", this.currentLexeme.getIndex());
            }
            children.add(leftQuote);
            children.add(expression);
            children.add(rightQuote);
            mathExpr.setChildren(children);
        }
        return mathExpr;
    }

    private TreeNode checkIdentifier() {
        TreeNode identifier = null;
        if (currentLexeme instanceof VariableLexeme) {
            identifier = new TreeNode();
            identifier.setTreeNodeType(TreeNode.TreeNodeType.IDENTIFIER);
            identifier.setChildren(new ArrayList<>());
            identifier.setLexeme(currentLexeme);
            this.currentLexeme = lexemeIterator.next();
        }
        return identifier;
    }

    private TreeNode checkConstant() {
        TreeNode constant = null;
        if (currentLexeme instanceof ConstantLexeme) {
            constant = new TreeNode();
            constant.setTreeNodeType(TreeNode.TreeNodeType.CONSTANT);
            constant.setChildren(new ArrayList<>());
            constant.setLexeme(currentLexeme);
            this.currentLexeme = lexemeIterator.next();
        }
        return constant;
    }
}
