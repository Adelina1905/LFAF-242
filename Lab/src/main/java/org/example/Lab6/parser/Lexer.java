package org.example.Lab6.parser;

import java.util.*;
import java.util.regex.Pattern;

public class Lexer {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();


    private static final List<PatternEntry> PATTERNS = Arrays.asList(
            new PatternEntry(TokenType.COMMAND,   Pattern.compile("^(move|attack|jump|collect|teleport)$")),
            new PatternEntry(TokenType.DIRECTION, Pattern.compile("^(left|right|forward|back)$")),
            new PatternEntry(TokenType.TARGET,    Pattern.compile("^(goblin|dragon|villager|knight)$")),
            new PatternEntry(TokenType.ITEM,      Pattern.compile("^(coin|potion|key)$")),
            new PatternEntry(TokenType.KEYWORD,   Pattern.compile("^(to|at)$")),
            new PatternEntry(TokenType.NUMBER,    Pattern.compile("^\\d+$")),
            new PatternEntry(TokenType.LPAREN,    Pattern.compile("^\\(")),
            new PatternEntry(TokenType.RPAREN,    Pattern.compile("^\\)")),
            new PatternEntry(TokenType.COMMA,     Pattern.compile("^,$")),
            new PatternEntry(TokenType.IDENTIFIER, Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$"))
    );

    public Lexer(String input) {
        this.input = input;
    }
    public List<Token> tokenize(){
        String processedInput = input.replace("(", " ( ")
                .replace(")", " ) ")
                .replace(",", " , ");

        String[] words = processedInput.trim().split("\\s+");

        for(String word : words){
            Token token = null;
            for (PatternEntry entry : PATTERNS) {
                if (entry.pattern.matcher(word).matches()) {
                    token = new Token(entry.type, word);
                    break;
                }
            }
            if (token == null) {
                token = new Token(TokenType.IDENTIFIER, word);
            }
            tokens.add(token);
        }
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }

    private static class PatternEntry {
        TokenType type;
        Pattern pattern;
        PatternEntry(TokenType type, Pattern pattern) {
            this.type = type;
            this.pattern = pattern;
        }
    }
}
