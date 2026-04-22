package org.example.Lab6.parser.ast;

public class JumpNode extends CommandNode {
    public JumpNode() {
        super("jump");
    }

    @Override
    public String toString() {
        return "JumpCommand{}";
    }
}