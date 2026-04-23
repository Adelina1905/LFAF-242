package org.example.Lab6.ast;

public abstract class CommandNode implements ASTNode {
    protected final String commandName;

    public CommandNode(String commandName) {
        this.commandName = commandName;
    }

    public String getCommandName() { return commandName; }
}