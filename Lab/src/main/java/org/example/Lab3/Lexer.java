package org.example.Lab3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Lexer {
    private final String input;
    private final List<Token> tokens = new ArrayList<>();


    private static final Set<String> COMMANDS =
            Set.of("move", "attack", "jump", "collect", "teleport");
    private static final Set<String> DIRECTIONS =
            Set.of("left", "right", "forward", "back");
    private static final Set<String> TARGETS =
            Set.of("goblin", "dragon", "villager", "knight");
    private static final Set<String> ITEMS =
            Set.of("coin", "potion", "key");
    private static final Set<String> KEYWORDS =
            Set.of("to", "at");
    public Lexer(String input) {
        this.input = input;
    }
    public List<Token> tokenize(){
        String processedInput = input.replace("(", " ( ")
                .replace(")", " ) ")
                .replace(",", " , ");

        String[] words = processedInput.split("\\s+");

        for(String word : words){
            if(COMMANDS.contains(word)){
                tokens.add(new Token(TokenType.COMMAND, word));
            }
            else if(DIRECTIONS.contains(word)){
                tokens.add(new Token(TokenType.DIRECTION, word));
            }
            else if(TARGETS.contains(word)){
                tokens.add(new Token(TokenType.TARGET, word));
            }
            else if(ITEMS.contains(word)){
                tokens.add(new Token(TokenType.ITEM, word));
            }
            else if(KEYWORDS.contains(word)){
                tokens.add(new Token(TokenType.KEYWORD, word));
            }
            else if (word.matches("\\d+")) {
                tokens.add(new Token(TokenType.NUMBER, word));
            }
            else if(word.equals("(")){
                tokens.add(new Token(TokenType.LPAREN, word));
            }
            else if(word.equals(")")){
                tokens.add(new Token(TokenType.RPAREN, word));
            }
            else if(word.equals(",")){
                tokens.add(new Token(TokenType.COMMA, word));
            }
            else {
                tokens.add(new Token(TokenType.IDENTIFIER, word));
            }
        }
        tokens.add(new Token(TokenType.EOF, ""));

        return tokens;
    }
}
