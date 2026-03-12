package org.example.Lab3;
import java.util.*;

public class Main {
    static void main( String[] args) {
        String input = "move left attack goblin jump collect coin move to (10, 90)";

        Lexer lexer = new Lexer(input);

        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}