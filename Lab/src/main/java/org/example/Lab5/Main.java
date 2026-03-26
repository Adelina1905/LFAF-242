package org.example.Lab5;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Set<String> VN = new HashSet<>(Arrays.asList("S", "A", "B", "C", "D"));
        Set<String> VT = new HashSet<>(Arrays.asList("a", "b"));
        String S = "S";

        Map<String, Set<List<String>>> P = new HashMap<>();
        P.put("S", new HashSet<>(Arrays.asList(Arrays.asList("b", "A"), Arrays.asList("A", "C"))));
        P.put("A", new HashSet<>(Arrays.asList(Arrays.asList("A", "b", "C"), Arrays.asList("b", "S"), Arrays.asList("B", "C"))));
        P.put("B", new HashSet<>(Arrays.asList(Arrays.asList("C", "b", "a", "C"), Arrays.asList("a"), Arrays.asList("b", "S", "a"))));
        P.put("C", new HashSet<>(Collections.emptyList()));
        P.put("D", new HashSet<>(Collections.singletonList(Arrays.asList("A", "B"))));

        Grammar grammar = new Grammar(VN, VT, P, S);
        System.out.println("Original Grammar:");
        grammar.printGrammar();

        GrammarNormalizer.normalizeToCNF(grammar);

        System.out.println("\nCNF Grammar:");
        grammar.printGrammar();
    }
}