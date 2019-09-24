package edu.stanislav.computer_systems_software.lab1.lexer;

import edu.stanislav.computer_systems_software.lab1.CompilerException;

import java.util.StringJoiner;

public class LexicalException extends CompilerException {

    public LexicalException(String message, int index) {
        super(message, index);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LexicalException.class.getSimpleName() + "[", "]")
                .add("message=" + getMessage())
                .add("index=" + getIndex())
                .toString();
    }
}
