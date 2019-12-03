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

    private Iterator<Lexeme> lexemeIterator = null;
    private Lexeme currentLexeme = null;
    private int lastLexemeIndex = 0;

    public TreeNode parse(List<Lexeme> lexemes) throws ParseException {
        lastLexemeIndex = lexemes.get(lexemes.size() - 1).getIndex();
        this.lexemeIterator = lexemes.iterator();
        nextLexeme();
        TreeNode root = checkExpression();
        if (this.currentLexeme != null) {
            throw new ParseException("End of expression", this.currentLexeme.getIndex());
        }
        return root;
    }

    private TreeNode checkExpression() throws ParseException {
        TreeNode expression = new TreeNode();
        expression.setTreeNodeType(TreeNode.TreeNodeType.EXPRESSION);
        expression.setChildren(new ArrayList<>(Collections.singletonList(checkAddOp())));
        return expression;
    }

    private TreeNode checkAddOp() throws ParseException {
        TreeNode expression = checkMulOp();
        if (this.currentLexeme instanceof PlusOperatorLexeme || this.currentLexeme instanceof MinusOperatorLexeme) {
            List<TreeNode> children = new ArrayList<>();
            children.add(expression);
            expression = new TreeNode();
            expression.setTreeNodeType(TreeNode.TreeNodeType.ADDOP);
            expression.setLexeme(currentLexeme);
            nextLexeme();
            children.add(checkAddOp());
            expression.setChildren(children);
        }
        return expression;
    }

    private TreeNode checkMulOp() throws ParseException {
        TreeNode mulOp = checkTerm();
        if (this.currentLexeme instanceof MultiplyOperatorLexeme || this.currentLexeme instanceof DivideOperatorLexeme) {
            List<TreeNode> children = new ArrayList<>();
            children.add(mulOp);
            mulOp = new TreeNode();
            mulOp.setTreeNodeType(TreeNode.TreeNodeType.MULOP);
            mulOp.setLexeme(this.currentLexeme);
            nextLexeme();
            children.add(checkMulOp());
            mulOp.setChildren(children);
        }
        return mulOp;
    }

    private TreeNode checkTerm() throws ParseException {
        TreeNode term = new TreeNode();
        term.setTreeNodeType(TreeNode.TreeNodeType.TERM);
        if (this.currentLexeme instanceof VariableLexeme) {
            term.setChildren(new ArrayList<>(Collections.singletonList(checkIdentifier())));
        } else if (this.currentLexeme instanceof ConstantLexeme) {
            term.setChildren(new ArrayList<>(Collections.singletonList(checkConstant())));
        } else if (this.currentLexeme instanceof MathFunctionLexeme) {
            term.setChildren(new ArrayList<>(Collections.singletonList(checkMath())));
        } else if(this.currentLexeme instanceof MinusOperatorLexeme) {
            List<TreeNode> children = new ArrayList<>();
            TreeNode unary = new TreeNode();
            unary.setTreeNodeType(TreeNode.TreeNodeType.UNARY);
            unary.setChildren(new ArrayList<>());
            unary.setLexeme(this.currentLexeme);
            children.add(unary);
            nextLexeme();
            TreeNode varOrConst = checkIdentifier();
            if (varOrConst == null) {
                varOrConst = checkConstant();
            }
            children.add(varOrConst);
            term.setChildren(children);
        } else if (this.currentLexeme instanceof LeftQuoteLexeme) {
            term.setChildren(new ArrayList<>(Collections.singletonList(checkExpressionInQuotes())));
        } else if (this.currentLexeme == null){
            throw new ParseException("Bad end for expression. Should be term after this lexeme", lastLexemeIndex);
        } else {
            throw new ParseException("Wrong lexeme! There should be variable, constant, math func or left quote for new expression start", currentLexeme.getIndex());
        }
        return term;
    }

    private TreeNode checkIdentifier() {
        TreeNode identifier = null;
        if (currentLexeme instanceof VariableLexeme) {
            identifier = new TreeNode();
            identifier.setTreeNodeType(TreeNode.TreeNodeType.IDENTIFIER);
            identifier.setChildren(new ArrayList<>());
            identifier.setLexeme(currentLexeme);
            nextLexeme();
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
            nextLexeme();
        }
        return constant;
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
            nextLexeme();
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
            mathExpr.setChildren(new ArrayList<>(Collections.singletonList(checkExpressionInQuotes())));
        }
        return mathExpr;
    }

    private TreeNode checkExpressionInQuotes() throws ParseException {
        TreeNode expressionInQuotes = new TreeNode();
        expressionInQuotes.setTreeNodeType(TreeNode.TreeNodeType.EXPRESSION_IN_QUOTES);
        List<TreeNode> children = new ArrayList<>();
        TreeNode leftQuote = new TreeNode();
        leftQuote.setTreeNodeType(TreeNode.TreeNodeType.LEFT_QUOTE);
        leftQuote.setChildren(new ArrayList<>());
        leftQuote.setLexeme(currentLexeme);
        this.currentLexeme = lexemeIterator.next();
        TreeNode expression = checkExpression();
        TreeNode rightQuote = null;
        if (this.currentLexeme instanceof RightQuoteLexeme) {
            rightQuote = new TreeNode();
            // check right
            rightQuote.setTreeNodeType(TreeNode.TreeNodeType.RIGHT_QUOTE);
            rightQuote.setChildren(new ArrayList<>());
            rightQuote.setLexeme(this.currentLexeme);
            nextLexeme();
        } else {
            if (this.currentLexeme != null) {
                throw new ParseException("Should be closing parentheses", this.currentLexeme.getIndex());
            } else {
                throw new ParseException("Should be closing parentheses", lastLexemeIndex);
            }
        }
        children.add(leftQuote);
        children.add(expression);
        children.add(rightQuote);
        expressionInQuotes.setChildren(children);
        return expressionInQuotes;
    }

    private void nextLexeme() {
        if (lexemeIterator.hasNext()) {
            this.currentLexeme = lexemeIterator.next();
        } else {
            this.currentLexeme = null;
        }
    }
}
