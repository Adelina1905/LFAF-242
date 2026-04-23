package org.example.Lab6;
import org.example.Lab6.ast.*;

//import org.example.Lab6.Parser.*;

import java.util.ArrayList;
import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int pos;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.pos = 0;
    }

    private Token current() {
        return tokens.get(pos);
    }

//    private Token peek() {
//        return tokens.get(pos);
//    }

    private void consume(TokenType expected) {
        if (current().getType() == expected) {
            pos++;
        } else {
            throw new RuntimeException("Parse error: expected " + expected + ", got " + current().getType() +
                    " ('" + current().getText() + "') at position " + pos);
        }
    }

    private boolean check(TokenType type) {
        return current().getType() == type;
    }

    // parseProgram : command*
    public List<CommandNode> parseProgram() {
        List<CommandNode> commands = new ArrayList<>();
        while (current().getType() != TokenType.EOF) {
            commands.add(parseCommand());
        }
        return commands;
    }

    // parseCommand : COMMAND (arguments depending on the command)
    private CommandNode parseCommand() {
        if (!check(TokenType.COMMAND)) {
            throw new RuntimeException("Parse error: expected COMMAND, got " + current().getType());
        }
        String cmdText = current().getText();
        consume(TokenType.COMMAND);

        switch (cmdText) {
            case "move":
                return parseMove();
            case "attack":
                return parseAttack();
            case "jump":
                return parseJump();
            case "collect":
                return parseCollect();
            case "teleport":
                return parseTeleport();
            default:
                throw new RuntimeException("Unknown command: " + cmdText);
        }
    }

    private MoveNode parseMove() {
        DirectionNode dir = null;
        TargetNode target = null;

        // optional direction
        if (check(TokenType.DIRECTION)) {
            dir = new DirectionNode(current().getText());
            consume(TokenType.DIRECTION);
        }
        // optional "to" + target
        if (check(TokenType.KEYWORD) && current().getText().equals("to")) {
            consume(TokenType.KEYWORD);
            if (check(TokenType.TARGET)) {
                target = new TargetNode(current().getText());
                consume(TokenType.TARGET);
            } else {
                throw new RuntimeException("Expected TARGET after 'to'");
            }
        }
        return new MoveNode(dir, target);
    }

    private AttackNode parseAttack() {
        TargetNode target = null;
        if (check(TokenType.TARGET)) {
            target = new TargetNode(current().getText());
            consume(TokenType.TARGET);
        }
        return new AttackNode(target);
    }

    private JumpNode parseJump() {
        // jump has no arguments
        return new JumpNode();
    }

    private CollectNode parseCollect() {
        ItemNode item = null;
        if (check(TokenType.ITEM)) {
            item = new ItemNode(current().getText());
            consume(TokenType.ITEM);
        }
        return new CollectNode(item);
    }

    private TeleportNode parseTeleport() {
        CoordinateNode coord = parseCoordinate();
        return new TeleportNode(coord);
    }

    private CoordinateNode parseCoordinate() {
        if (!check(TokenType.LPAREN)) {
            throw new RuntimeException("Expected '(' for teleport coordinates");
        }
        consume(TokenType.LPAREN);
        if (!check(TokenType.NUMBER)) {
            throw new RuntimeException("Expected x coordinate");
        }
        int x = Integer.parseInt(current().getText());
        consume(TokenType.NUMBER);
        if (!check(TokenType.COMMA)) {
            throw new RuntimeException("Expected ',' between coordinates");
        }
        consume(TokenType.COMMA);
        if (!check(TokenType.NUMBER)) {
            throw new RuntimeException("Expected y coordinate");
        }
        int y = Integer.parseInt(current().getText());
        consume(TokenType.NUMBER);
        if (!check(TokenType.RPAREN)) {
            throw new RuntimeException("Expected ')' after coordinates");
        }
        consume(TokenType.RPAREN);
        return new CoordinateNode(x, y);
    }
}