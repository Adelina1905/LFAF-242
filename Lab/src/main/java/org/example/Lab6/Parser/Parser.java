package org.example.Lab6.Parser;

import org.example.Lab6.Token;
import org.example.Lab6.TokenType;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token advance() {
        return tokens.get(current++);
    }

    private boolean match(TokenType type) {
        if (peek().getType() == type) {
            advance();
            return true;
        }
        return false;
    }

    public ASTNode parse() {
        return parseCommand();
    }
    private ASTNode parseCommand() {
        Token commandToken = advance();

        String command = commandToken.toString();

        String arg1 = null;
        String arg2 = null;

        if (match(TokenType.DIRECTION) ||
                match(TokenType.TARGET) ||
                match(TokenType.ITEM)) {

            arg1 = tokens.get(current - 1).toString();
        }

        else if (match(TokenType.KEYWORD)) {
            // Example: teleport to (10, 20)

            if (match(TokenType.LPAREN)) {
                Token num1 = advance(); // NUMBER
                match(TokenType.COMMA);
                Token num2 = advance(); // NUMBER
                match(TokenType.RPAREN);

                arg1 = num1.toString();
                arg2 = num2.toString();
            }
        }

        return new CommandNode(command, arg1, arg2);
    }
}