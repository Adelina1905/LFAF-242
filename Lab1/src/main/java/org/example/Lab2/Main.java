package org.example.Lab2;

import java.util.*;

public class Main {
    private static Map<String, List<String>> rules = new HashMap<>();

    static void main( String[] args) {
        rules.put("S", Arrays.asList("aA", "bB"));
        rules.put("A", Arrays.asList("bS", "cA", "aB"));
        rules.put("B", Arrays.asList("aB", "b"));



        Set<String> nonTerminals = new HashSet<>(Arrays.asList("S", "A", "B"));
        Set<String> terminals = new HashSet<>(Arrays.asList("a", "b", "c"));
        String startSymbol = "S";

        Grammar grammar = new Grammar(
                nonTerminals,
                terminals,
                rules,
                startSymbol
        );

        System.out.println("_________________");
        System.out.println(grammar.getNonTerminals());
        System.out.println(grammar.getTerminals());
        System.out.println(grammar.getProductions());
        System.out.println(grammar.getStartSymbol());
        System.out.println("_________________");
        List<String> generatedWords = grammar.generateStrings();
        FiniteAutomata fa = grammar.toFiniteAutomaton();

        System.out.println("\nControl if is valid words ( like correct words ):");
        for (String word : generatedWords) {
            System.out.println(word + " belongs to the language? " + fa.stringBelongsToLanguage(word));
        }

        // Controls wrong numbers to check if the automaton works correctly
        List<String> incorrectWords = Arrays.asList("xyz", "abc", "ba", "ae", "bc", "fS", "rL", "aee");

        System.out.println("\nControl wrong words");
        for (String word : incorrectWords) {
            System.out.println(word + " are in grammar " + fa.stringBelongsToLanguage(word));
        }

        String grammarType = grammar.classifyGrammar();
        System.out.println("---------------------------------------");
        System.out.println("Grammar classification: " + grammarType);

        System.out.println("_________________________________________");
    }
}
