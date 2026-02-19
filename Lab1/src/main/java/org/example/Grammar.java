package org.example;

import java.util.*;

public class Grammar {
    private Set<String> nonTerminals;    // V_n
    private Set<String> terminals;
    private static Map<String, List<String>> productions; // P (rules)
    private String startSymbol; // S
    private static Random random = new Random();

    public Grammar(
            Set<String> nonTerminals,
            Set<String> terminals,
            Map<String, List<String>> productions,
            String startSymbol
    ) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }
    public List<String> generateStrings() {
        List<String> generatedStrings = new ArrayList<>();

        for (int i= 0; i < 5; i++){
            String generatedWord = generateWord("S");
            System.out.println(generatedWord);
            generatedStrings.add(generatedWord);
        }

        return generatedStrings;
    }

    public FiniteAutomata toFiniteAutomaton() {
        Set<String> states = new HashSet<>(nonTerminals);
        Set<String> alphabet = new HashSet<>(terminals);
        Map<String, Map<String, Set<String>>> transitions = new HashMap<>();
        String initialState = startSymbol;
        Set<String> finalStates = new HashSet<>();

        for (String nonTerminal : productions.keySet()) {
            transitions.put(nonTerminal, new HashMap<>());
            for (String production : productions.get(nonTerminal)) {
                String firstSymbol = production.substring(0, 1);
                String rest = production.length() > 1 ? production.substring(1) : "";

                transitions.get(nonTerminal).computeIfAbsent(firstSymbol, k -> new HashSet<>()).add(rest.isEmpty() ? firstSymbol : rest);
                if (terminals.contains(firstSymbol)) {
                    finalStates.add(firstSymbol);
                }
            }
        }

        return new FiniteAutomata(states, alphabet, transitions, initialState, finalStates);
    }

    private static String generateWord(String symbol) {
        if (!productions.containsKey(symbol)) {
            return symbol; // If it's a terminal symbol, just return it
        }

        // Choosing a random choice out of the variants
        String rule = productions.get(symbol).get(random.nextInt(productions.get(symbol).size()));

        // Recursively work with each non-terminal number
        StringBuilder result = new StringBuilder();
        for (char c : rule.toCharArray()) {
            result.append(generateWord(String.valueOf(c)));
        }
        return result.toString();
    }
}
