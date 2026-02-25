package org.example.Lab1;

import java.util.Map;
import java.util.Set;

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

}
