package edu.stanislav.computer_systems_software.lab1.parser;

import edu.stanislav.computer_systems_software.lab1.CompilerException;

import java.util.StringJoiner;

public class ParseException extends CompilerException {

    public ParseException(String message, int index) {
        super(message, index);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ParseException.class.getSimpleName() + "[", "]")
                .add("message=" + getMessage())
                .add("index=" + getIndex())
                .toString();
    }

}
