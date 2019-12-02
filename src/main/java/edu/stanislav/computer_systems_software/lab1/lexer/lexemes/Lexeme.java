package edu.stanislav.computer_systems_software.lab1.lexer.lexemes;

import java.util.StringJoiner;

public abstract class Lexeme {

    int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "[", "]")
                .toString();
    }
}
