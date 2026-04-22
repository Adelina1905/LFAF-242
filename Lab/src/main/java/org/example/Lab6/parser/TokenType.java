package org.example.Lab6.parser;

public enum TokenType {
    COMMAND,
    DIRECTION,
    TARGET,
    ITEM,

    COMMA,
    LPAREN, //(
    RPAREN, //)
    KEYWORD,
    NUMBER,
    IDENTIFIER,

    EOF
}
