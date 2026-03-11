package org.example.Lab2;

import java.util.*;

public class Grammar {
    private Set<String> nonTerminals;    // V_n
    private Set<String> terminals;  //V_T
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

    public Set<String> getNonTerminals() {
        return nonTerminals;
    }

    public Set<String> getTerminals() {
        return terminals;
    }

    public static Map<String, List<String>> getProductions() {
        return productions;
    }

    public String getStartSymbol() {
        return startSymbol;
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


    public String classifyGrammar() {
        boolean isType3 = true;
        boolean isType2 = true;
        boolean isType1 = true;

        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            String left = entry.getKey();
            List<String> rights = entry.getValue();

            for (String rightSide : rights) {
                // Type 3 (Regular) Grammar check
                // Right-linear: A → aB or A → a
                // Left-linear: A → Ba or A → a
                boolean isValidType3 = false;
                if (rightSide.length() <= 1) {
                    isValidType3 = terminals.contains(rightSide);
                } else if (rightSide.length() == 2) {
                    String firstChar = rightSide.substring(0, 1);
                    String secondChar = rightSide.substring(1);
                    isValidType3 = (terminals.contains(firstChar) && nonTerminals.contains(secondChar)) ||
                            (nonTerminals.contains(firstChar) && terminals.contains(secondChar));
                }
                if (!isValidType3) {
                    isType3 = false;
                }

                // Type 2 (Context-Free) Grammar check
                // A → α, where A is a non-terminal and α is a string of terminals and non-terminals
                if (left.length() != 1 || !nonTerminals.contains(left)) {
                    isType2 = false;
                }

                // Type 1 (Context-Sensitive) Grammar check
                // αAβ → αγβ, where |γ| >= |A|
                if (rightSide.length() < left.length()) {
                    isType1 = false;
                }
            }
        }
        if (isType3) {
            return "Type 3: Regular Grammar";
        } else if (isType2) {
            return "Type 2: Context-Free Grammar";
        } else if (isType1) {
            return "Type 1: Context-Sensitive Grammar";
        } else {
            return "Type 0: Unrestricted Grammar";
        }

    }
    }
