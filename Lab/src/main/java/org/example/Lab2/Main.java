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

        Set<String> states = new HashSet<>(Arrays.asList("q0", "q1", "q2", "q3"));
        Set<String> alphabet = new HashSet<>(Arrays.asList("a", "b"));
        Set<String> finalStates = new HashSet<>(Collections.singletonList("q3"));
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>();

        transitions.put("q0", Map.of("a", Set.of("q1"), "b", Set.of("q2")));
        transitions.put("q1", Map.of("a", Set.of("q1", "q3"), "b", Set.of("q2")));
        transitions.put("q2", Map.of("b", Set.of("q3")));

        FiniteAutomata fa1 = new FiniteAutomata(states, alphabet, transitions, "q0", finalStates);
        Grammar grammar1 = fa1.toGrammar();

        System.out.println("_________________");
        System.out.println(grammar1.getNonTerminals());
        System.out.println(grammar1.getTerminals());
        System.out.println(grammar1.getProductions());
        System.out.println(grammar1.getStartSymbol());
        System.out.println("_________________");


        System.out.println(grammar.getNonTerminals());
        System.out.println(grammar.getTerminals());
        System.out.println(grammar.getProductions());
        System.out.println(grammar.getStartSymbol());

        System.out.println(fa1.isDeterministic());
        FiniteAutomata dfa = fa1.convertToDFA();

        System.out.println("DFA States: " + dfa.getStates());
        System.out.println("DFA Transitions: " + dfa.getTransitions());
        System.out.println("DFA beginning state: " + dfa.getInitialState());
        System.out.println("DFA ending state: " + dfa.getFinalStates());

    }
}
