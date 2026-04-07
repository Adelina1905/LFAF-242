package org.example.Lab6.Parser;

public class CommandNode extends ASTNode {
    public String command;
    public String argument1;
    public String argument2;

    public CommandNode(String command, String arg1, String arg2) {
        this.command = command;
        this.argument1 = arg1;
        this.argument2 = arg2;
    }

    @Override
    public String toString() {
        return "Command: " + command +
                ", Arg1: " + argument1 +
                ", Arg2: " + argument2;
    }
}