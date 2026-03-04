package org.example.Lab2;

import java.util.*;

public class FiniteAutomata {
    private Set<String> states;          // Q
    private Set<String> alphabet;        // Sigma
    private Map<String, Map<String, Set<String>>> transitions; // delta
    private String initialState;         // q0
    private Set<String> finalStates; //F


    public FiniteAutomata(Set<String> states,
                          Set<String> alphabet,
                          Map<String, Map<String, Set<String>>> transitions,
                          String initialState,
                          Set<String> finalStates
    ) {


        this.states = states;
        this.alphabet = alphabet;
        this.transitions = transitions;
        this.initialState = initialState;
        this.finalStates = finalStates;
    }

    public Set<String> getStates() {
        return states;
    }

    public Set<String> getAlphabet() {
        return alphabet;
    }

    public Map<String, Map<String, Set<String>>> getTransitions() {
        return transitions;
    }

    public String getInitialState() {
        return initialState;
    }

    public Set<String> getFinalStates() {
        return finalStates;
    }

    public boolean stringBelongsToLanguage(String inputString) {
        Set<String> currentStates = Set.of(initialState); // Start with the start state

        for (char symbol : inputString.toCharArray()) {
            Set<String> nextStates = new java.util.HashSet<>();

            for (String state : currentStates) {
                if (transitions.containsKey(state) && transitions.get(state).containsKey(String.valueOf(symbol))) {
                    nextStates.addAll(transitions.get(state).get(String.valueOf(symbol)));
                }
            }

            if (nextStates.isEmpty()) {
                return false; // If there is no states to continue than it ends
            }
            currentStates = nextStates;
        }

        // Checking if there is a state that finalize with the final state
        for (String state : currentStates) {
            if (finalStates.contains(state)) {
                return true;
            }
        }

        return false;
    }

    public Grammar toGrammar() {
        // Creating states q0 -> A, q1 -> B и т. д.
        Map<String, String> stateMapping = new HashMap<>();
        char letter = 'A';

        for (String state : states) {
            stateMapping.put(state, String.valueOf(letter));
            letter++;
        }

        Set<String> nonTerminals = new HashSet<>();
        for (String state : states) {
            nonTerminals.add(stateMapping.get(state)); // Добавляем переименованные состояния
        }

        Set<String> terminals = new HashSet<>(alphabet);
        Map<String, List<String>> newProductions = new HashMap<>();

        for (String state : transitions.keySet()) {
            String newState = stateMapping.get(state); // Получаем новое имя состояния
            Map<String, Set<String>> stateTransitions = transitions.get(state);
            List<String> transitionList = new ArrayList<>();

            for (Map.Entry<String, Set<String>> entry : stateTransitions.entrySet()) {
                String symbol = entry.getKey(); // буква ('a', 'b', 'c' и т.д.)
                for (String nextState : entry.getValue()) {
                    transitionList.add(symbol + stateMapping.get(nextState)); // Заменяем на новую букву
                }
            }

            // Если состояние является конечным, добавляем "epsilon"
            if (finalStates.contains(state)) {
                transitionList.add("ε");
            }

            newProductions.put(newState, transitionList);
        }

        // Добавляем epsilon для финальных состояний, даже если у них нет переходов
        for (String finalState : finalStates) {
            String newFinalState = stateMapping.get(finalState);
            newProductions.putIfAbsent(newFinalState, new ArrayList<>(List.of("ε")));
        }

        System.out.println(newProductions);

        return new Grammar(nonTerminals, terminals, newProductions, stateMapping.get(initialState));
    }

    public boolean isDeterministic() {
        for (Map<String, Set<String>> stateTransitions : transitions.values()) {
            for (Set<String> nextStates : stateTransitions.values()) {
                if (nextStates.size() > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public FiniteAutomata convertToDFA() {
        // Очередь для обработки новых состояний DFA
        Queue<Set<String>> queue = new LinkedList<>();
        Map<Set<String>, String> dfaStateNames = new HashMap<>();
        Map<String, Map<String, Set<String>>> dfaTransitions = new HashMap<>();

        // Начальное состояние в DFA (множество состояний, достижимых из q0)
        Set<String> dfaStartState = epsilonClosure(Set.of(initialState));
        queue.add(dfaStartState);
        String dfaStartName = generateStateName(dfaStartState);
        dfaStateNames.put(dfaStartState, dfaStartName);
        dfaTransitions.put(dfaStartName, new HashMap<>());

        Set<String> dfaFinalStates = new HashSet<>();
        if (!Collections.disjoint(dfaStartState, finalStates)) {
            dfaFinalStates.add(dfaStartName);
        }

        while (!queue.isEmpty()) {
            Set<String> currentDFAState = queue.poll();
            String currentDFAStateName = dfaStateNames.get(currentDFAState);

            for (String symbol : alphabet) {
                Set<String> nextState = new HashSet<>();

                for (String nfaState : currentDFAState) {
                    if (transitions.containsKey(nfaState) && transitions.get(nfaState).containsKey(symbol)) {
                        nextState.addAll(transitions.get(nfaState).get(symbol));
                    }
                }

                nextState = epsilonClosure(nextState);
                if (!nextState.isEmpty()) {
                    String nextDFAStateName = dfaStateNames.computeIfAbsent(nextState, s -> {
                        String name = generateStateName(s);
                        queue.add(s);
                        dfaTransitions.put(name, new HashMap<>());
                        if (!Collections.disjoint(s, finalStates)) {
                            dfaFinalStates.add(name);
                        }
                        return name;
                    });

                    dfaTransitions.get(currentDFAStateName).put(symbol, Set.of(nextDFAStateName));
                }
            }
        }

        return new FiniteAutomata(new HashSet<>(dfaStateNames.values()), alphabet, dfaTransitions, dfaStartName, dfaFinalStates);
    }

    private Set<String> epsilonClosure(Set<String> states) {
        Set<String> closure = new HashSet<>(states);
        Stack<String> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            String state = stack.pop();
            if (transitions.containsKey(state) && transitions.get(state).containsKey("ε")) {
                for (String nextState : transitions.get(state).get("ε")) {
                    if (!closure.contains(nextState)) {
                        closure.add(nextState);
                        stack.push(nextState);
                    }
                }
            }
        }

        return closure;
    }
    private String generateStateName(Set<String> states) {
        return "{" + String.join(",", states) + "}";
    }
}
