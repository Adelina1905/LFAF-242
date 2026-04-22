package org.example.Lab6.parser.ast;

public class TeleportNode extends CommandNode {
    private final CoordinateNode coordinate;

    public TeleportNode(CoordinateNode coordinate) {
        super("teleport");
        this.coordinate = coordinate;
    }

    public CoordinateNode getCoordinate() { return coordinate; }

    @Override
    public String toString() {
        return "TeleportCommand{coordinate=" + coordinate + "}";
    }
}