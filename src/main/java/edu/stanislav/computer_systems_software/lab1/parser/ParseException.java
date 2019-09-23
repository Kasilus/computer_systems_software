package edu.stanislav.computer_systems_software.lab1.parser;

import edu.stanislav.computer_systems_software.lab1.lexer.LexicalException;

import java.util.StringJoiner;

public class ParseException extends Exception {

    private int index;

    public ParseException(String message, int index) {
        super(message);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LexicalException.class.getSimpleName() + "[", "]")
                .add("message=" + getMessage())
                .add("index=" + index)
                .toString();
    }

}
