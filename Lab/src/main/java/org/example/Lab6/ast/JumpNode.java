package org.example.Lab6.ast;

public class JumpNode extends CommandNode {
    public JumpNode() {
        super("jump");
    }

    @Override
    public String toString() {
        return "JumpCommand{}";
    }
}