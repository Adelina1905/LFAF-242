package org.example.Lab6.ast;

public class TargetNode implements ASTNode {
    private final String value;
    public TargetNode(String value) { this.value = value; }
    public String getValue() { return value; }
    @Override public String toString() { return value; }
}
