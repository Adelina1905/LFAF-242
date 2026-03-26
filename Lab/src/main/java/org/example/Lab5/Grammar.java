package org.example.Lab5;

import java.util.*;

public class Grammar {
    public Set<String> nonTerminals;
    public Set<String> terminals;
    public Map<String, Set<List<String>>> productions;
    public String startSymbol;

    public Grammar(Set<String> nonTerminals, Set<String> terminals,
                   Map<String, Set<List<String>>> productions, String startSymbol) {
        this.nonTerminals = nonTerminals;
        this.terminals = terminals;
        this.productions = productions;
        this.startSymbol = startSymbol;
    }

    public void printGrammar() {
        System.out.println("Productions:");
        for (String lhs : productions.keySet()) {
            for (List<String> rhs : productions.get(lhs)) {
                System.out.println(lhs + " → " + (rhs.isEmpty() ? "ε" : String.join(" ", rhs)));
            }
        }
    }
}