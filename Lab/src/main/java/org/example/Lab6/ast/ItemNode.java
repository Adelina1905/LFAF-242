package org.example.Lab6.ast;

public class ItemNode implements ASTNode {
    private final String value;
    public ItemNode(String value) { this.value = value; }
    public String getValue() { return value; }
    @Override public String toString() { return value; }
}