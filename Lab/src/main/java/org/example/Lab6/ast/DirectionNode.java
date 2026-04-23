package org.example.Lab6.ast;

public class DirectionNode implements ASTNode {
    private final String value;
    public DirectionNode(String value) { this.value = value; }
    public String getValue() { return value; }
    @Override public String toString() { return value; }
}
