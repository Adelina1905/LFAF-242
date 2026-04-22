package org.example.Lab6.parser.ast;

public class MoveNode extends CommandNode {
    private final DirectionNode direction;
    private final TargetNode target;

    public MoveNode(DirectionNode direction, TargetNode target) {
        super("move");
        this.direction = direction;
        this.target = target;
    }

    public DirectionNode getDirection() { return direction; }
    public TargetNode getTarget() { return target; }

    @Override
    public String toString() {
        return String.format("MoveCommand{direction=%s, target=%s}",
                direction != null ? direction : "none",
                target != null ? target : "none");
    }
}