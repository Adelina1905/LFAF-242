package org.example.Lab5;

import java.util.*;

public class GrammarNormalizer {

    public static Grammar normalizeToCNF(Grammar grammar) {
        eliminateEpsilon(grammar);
        eliminateUnitProductions(grammar);
        eliminateInaccessibleSymbols(grammar);
        eliminateNonProductiveSymbols(grammar);
        convertToCNF(grammar);
        return grammar;
    }

    private static void eliminateEpsilon(Grammar grammar) {
        Set<String> nullable = new HashSet<>();
        for (String lhs : grammar.productions.keySet()) {
            for (List<String> rhs : grammar.productions.get(lhs)) {
                if (rhs.isEmpty()) {
                    nullable.add(lhs);
                }
            }
        }

        boolean changed;
        do {
            changed = false;
            for (String lhs : grammar.productions.keySet()) {
                for (List<String> rhs : grammar.productions.get(lhs)) {
                    if (rhs.stream().allMatch(nullable::contains) && !nullable.contains(lhs)) {
                        nullable.add(lhs);
                        changed = true;
                    }
                }
            }
        } while (changed);

        Map<String, Set<List<String>>> newProductions = new HashMap<>();
        for (String lhs : grammar.productions.keySet()) {
            newProductions.putIfAbsent(lhs, new HashSet<>());
            for (List<String> rhs : grammar.productions.get(lhs)) {
                Set<List<String>> variations = generateNullableCombinations(rhs, nullable);
                newProductions.get(lhs).addAll(variations);
            }
        }

        for (String lhs : new HashSet<>(newProductions.keySet())) {
            newProductions.get(lhs).removeIf(rhs -> rhs.isEmpty() && !lhs.equals(grammar.startSymbol));
        }

        grammar.productions = newProductions;
    }

    private static Set<List<String>> generateNullableCombinations(List<String> symbols, Set<String> nullable) {
        Set<List<String>> results = new HashSet<>();
        int n = symbols.size();
        for (int i = 0; i < (1 << n); i++) {
            List<String> combination = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if ((i & (1 << j)) == 0 || !nullable.contains(symbols.get(j))) {
                    combination.add(symbols.get(j));
                }
            }
            results.add(combination);
        }
        return results;
    }

    private static void eliminateUnitProductions(Grammar grammar) {
        Map<String, Set<List<String>>> newProductions = new HashMap<>();
        for (String lhs : grammar.nonTerminals) {
            newProductions.put(lhs, new HashSet<>());
            Set<String> reachable = getUnitClosure(lhs, grammar);
            for (String nt : reachable) {
                for (List<String> rhs : grammar.productions.getOrDefault(nt, new HashSet<>())) {
                    if (!(rhs.size() == 1 && grammar.nonTerminals.contains(rhs.get(0)))) {
                        newProductions.get(lhs).add(rhs);
                    }
                }
            }
        }
        grammar.productions = newProductions;
    }

    private static Set<String> getUnitClosure(String symbol, Grammar grammar) {
        Set<String> closure = new HashSet<>();
        Stack<String> stack = new Stack<>();
        closure.add(symbol);
        stack.push(symbol);
        while (!stack.isEmpty()) {
            String current = stack.pop();
            for (List<String> rhs : grammar.productions.getOrDefault(current, new HashSet<>())) {
                if (rhs.size() == 1 && grammar.nonTerminals.contains(rhs.get(0))) {
                    String target = rhs.get(0);
                    if (!closure.contains(target)) {
                        closure.add(target);
                        stack.push(target);
                    }
                }
            }
        }
        return closure;
    }

    private static void eliminateInaccessibleSymbols(Grammar grammar) {
        Set<String> accessible = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        accessible.add(grammar.startSymbol);
        queue.add(grammar.startSymbol);

        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (List<String> rhs : grammar.productions.getOrDefault(current, new HashSet<>())) {
                for (String symbol : rhs) {
                    if (grammar.nonTerminals.contains(symbol) && !accessible.contains(symbol)) {
                        accessible.add(symbol);
                        queue.add(symbol);
                    }
                }
            }
        }

        grammar.productions.keySet().retainAll(accessible);
        grammar.nonTerminals.retainAll(accessible);
    }

    private static void eliminateNonProductiveSymbols(Grammar grammar) {
        Set<String> productive = new HashSet<>();
        boolean changed;

        do {
            changed = false;
            for (String lhs : grammar.productions.keySet()) {
                for (List<String> rhs : grammar.productions.get(lhs)) {
                    if (rhs.stream().allMatch(s -> grammar.terminals.contains(s) || productive.contains(s))) {
                        if (productive.add(lhs)) {
                            changed = true;
                        }
                    }
                }
            }
        } while (changed);

        grammar.productions.keySet().retainAll(productive);
        grammar.nonTerminals.retainAll(productive);
    }

    private static void convertToCNF(Grammar grammar) {
        Map<String, String> terminalMap = new HashMap<>();
        int newVarCounter = 1;

        for (String lhs : new HashSet<>(grammar.productions.keySet())) {
            Set<List<String>> newRules = new HashSet<>();
            for (List<String> rhs : grammar.productions.get(lhs)) {
                List<String> newRHS = new ArrayList<>();
                for (String symbol : rhs) {
                    if (grammar.terminals.contains(symbol) && rhs.size() > 1) {
                        if (!terminalMap.containsKey(symbol)) {
                            String newVar = "T" + (newVarCounter++);
                            grammar.nonTerminals.add(newVar);
                            grammar.productions.put(newVar, new HashSet<>(Collections.singletonList(Collections.singletonList(symbol))));
                            terminalMap.put(symbol, newVar);
                        }
                        newRHS.add(terminalMap.get(symbol));
                    } else {
                        newRHS.add(symbol);
                    }
                }
                newRules.add(newRHS);
            }
            grammar.productions.put(lhs, newRules);
        }

        Map<String, Set<List<String>>> newProductions = new HashMap<>();
        int varCounter = 1;
        for (String lhs : grammar.productions.keySet()) {
            newProductions.putIfAbsent(lhs, new HashSet<>());
            for (List<String> rhs : grammar.productions.get(lhs)) {
                if (rhs.size() <= 2) {
                    newProductions.get(lhs).add(rhs);
                } else {
                    String prev = rhs.get(0);
                    for (int i = 1; i < rhs.size() - 1; i++) {
                        String newVar = "X" + (varCounter++);
                        grammar.nonTerminals.add(newVar);
                        newProductions.putIfAbsent(newVar, new HashSet<>());
                        newProductions.get(newVar).add(Arrays.asList(prev, rhs.get(i)));
                        prev = newVar;
                    }
                    newProductions.get(lhs).add(Arrays.asList(prev, rhs.get(rhs.size() - 1)));
                }
            }
        }
        grammar.productions = newProductions;
    }
}