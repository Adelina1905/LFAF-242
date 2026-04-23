package org.example.Lab6.ast;

public class CollectNode extends CommandNode {
    private final ItemNode item;

    public CollectNode(ItemNode item) {
        super("collect");
        this.item = item;
    }

    public ItemNode getItem() { return item; }

    @Override
    public String toString() {
        return "CollectCommand{item=" + (item != null ? item : "none") + "}";
    }
}