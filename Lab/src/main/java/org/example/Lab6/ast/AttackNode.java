package org.example.Lab6.ast;

public class AttackNode extends CommandNode {
    private final TargetNode target;

    public AttackNode(TargetNode target) {
        super("attack");
        this.target = target;
    }

    public TargetNode getTarget() { return target; }

    @Override
    public String toString() {
        return "AttackCommand{target=" + (target != null ? target : "none") + "}";
    }
}