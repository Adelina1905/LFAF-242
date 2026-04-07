package org.example.Lab6;

import org.example.Lab6.Parser.ASTNode;
import org.example.Lab6.Parser.Parser;

import java.util.List;

public class Main {
    static void main( String[] args) {
//        String input = "move forward to dragon teleport (10, 9)";
//
//        Lexer lexer = new Lexer(input);
//
//        List<Token> tokens = lexer.tokenize();
//
//        for (Token token : tokens) {
//            System.out.println(token);
//        }
        String input = "teleport to (10, 20)";

        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();

        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parse();

        System.out.println(ast);
    }
}