package edu.stanislav.computer_systems_software.lab1;

public class CompilerException extends Exception {

    private int index;

    public CompilerException(String message, int index) {
        super(message);
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}
