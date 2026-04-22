package org.example.Lab6;
import org.example.Lab6.parser.*;
import org.example.Lab6.parser.ast.CommandNode;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        String input = " move left to goblin attack knight collect coin teleport (5, 3)";
//        move left to goblin attack knight collect coin teleport (5, 3)
//        move forward to dragon teleport (10, 9)
        System.out.println("Input: " + input);
        System.out.println("\n--- Tokenization (using regex) ---");
        Lexer lexer = new Lexer(input);
        List<Token> tokens = lexer.tokenize();
        for (Token t : tokens) {
            System.out.println(t);
        }

        System.out.println("\n--- Parsing & AST ---");
        Parser parser = new Parser(tokens);
        List<CommandNode> ast = parser.parseProgram();

        for (CommandNode cmd : ast) {
            System.out.println(cmd);
        }
    }
}