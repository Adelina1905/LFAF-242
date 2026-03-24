package org.example.Lab3;
import java.util.*;

public class Main {
    static void main( String[] args) {
        String input = "move forward to dragon teleport (10, 9)";

        Lexer lexer = new Lexer(input);

        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}