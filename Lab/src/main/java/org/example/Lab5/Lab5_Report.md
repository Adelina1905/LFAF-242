# Chomsky Normal Form

### Course: Formal Languages & Finite Automata
### Author: Temciuc Adelina

----

## Theory

## Chomsky Normal Form
A grammar where every production is either of the form `A → BC` or `A → c` (where `A`, `B`, `C` are arbitrary variables and `c` an arbitrary symbol).

### Example:
```
S → AS | a
A → SA | b
```
(If language contains `ε`, then we allow `S → ε` where `S` is start symbol, and forbid `S` on RHS.)  
*Goddard 9a: 2*

### Why Chomsky Normal Form?
The key advantage is that in Chomsky Normal Form, every derivation of a string of `n` letters has exactly `2n − 1` steps. Thus: one can determine if a string is in the language by exhaustive search of all derivations.  
*Goddard 9a: 3*

### Conversion
The conversion to Chomsky Normal Form has four main steps:
1. Get rid of all `ε` productions.
2. Get rid of all productions where RHS is one variable.
3. Replace every production that is too long by shorter productions.
4. Move all terminals to productions where RHS is one terminal.

## Objectives:

- Learn about Chomsky Normal Form (CNF)

- Get familiar with the approaches of normalizing a grammar.

- Implement a method for normalizing an input grammar by the rules of CNF.
    1. The implementation needs to be encapsulated in a method with an appropriate signature (also ideally in an appropriate class/type).
    2. The implemented functionality needs executed and tested.
    3. Also, another ***BONUS*** point would be given if the student will make the aforementioned function to accept any grammar, not only the one from the student's variant.


## Implementation Description

### 1. `Grammar` Class
The `Grammar` class represents a context-free grammar with non-terminals, terminals, productions, and a start symbol.

### Fields
- **`nonTerminals`**: Set of non-terminal symbols (e.g., `S`, `A`, `B`).
- **`terminals`**: Set of terminal symbols (e.g., `a`, `b`).
- **`productions`**: A map where the key is a non-terminal (e.g., `S`) and the value is a set of production rules (each rule is a `List<String>` representing the right-hand side, RHS).
- **`startSymbol`**: The starting non-terminal of the grammar.

The Grammar class encapsulates the structure of a context-free grammar, including its non-terminal symbols, terminal symbols, production rules, and the start symbol.

The constructor initializes the grammar with the provided sets of non-terminals, terminals, production rules, and the start symbol. The production rules are stored as a map where each key is a non-terminal symbol, and the corresponding value is a set of possible right-hand sides (RHS) for that non-terminal.

The printGrammar() method provides a readable representation of the grammar. For each production rule, it prints the left-hand side (LHS) followed by the RHS. If the RHS is empty (indicating an ε-production), it displays ε.

### `printGrammar()`
- **How it Works**:
    - If a production’s RHS is empty (`rhs.isEmpty()`), it prints `ε` (epsilon).
    - Otherwise, it joins the symbols with spaces (e.g., `S → a B`).

---

### 2. `GrammarNormalizer` Class
The `GrammarNormalizer` class contains the logic to normalize a CFG into CNF. The main method `normalizeToCNF()` orchestrates the process by calling helper methods in this order:
1. `eliminateEpsilon`
2. `eliminateUnitProductions`
3. `eliminateInaccessibleSymbols`
4. `eliminateNonProductiveSymbols`
5. `convertToCNF`

This class contains the logic for converting a grammar into CNF. The conversion is performed in a step-by-step manner, with each step implemented as a separate method. The main method, normalizeToCNF(), orchestrates the process by calling these helper methods in sequence.

### `normalizeToCNF(Grammar grammar)`
- **Purpose**: Converts the input grammar to CNF by applying all necessary transformations.
- **Process**: Calls the five helper methods sequentially and returns the modified grammar.
- **CNF Requirements**: After this process, every production will either be of the form `A → BC` (two non-terminals) or `A → c` (one terminal), except possibly `S → ε` for the start symbol if the language includes the empty string.

### `eliminateEpsilon(Grammar grammar)`
This method removes all ε-productions (except for the start symbol if needed). It first identifies nullable non-terminals (those that can derive ε) by iteratively checking productions where all symbols in the RHS are nullable. For each production, it generates all possible combinations where nullable symbols are omitted. Finally, it removes ε-productions from all non-terminals except the start symbol
- **Example**: If `S → A B`, `A → ε`, `B → ε`, then `S` becomes nullable, and new productions might include `S → A B`, `S → A`, `S → B`, and `S → ε`.


### `generateNullableCombinations(List<String> symbols, Set<String> nullable)`
- **Purpose**: Helper method to generate all possible combinations of a production’s RHS by omitting nullable symbols.
- **How it Works**:
    - Uses a binary counter (0 to `2^n - 1`, where `n` is the length of `symbols`) to represent all subsets.
    - For each position `j`, if the bit is 0 or the symbol isn’t nullable, it keeps the symbol; otherwise, it omits it.
    - Returns a set of all valid combinations.
- **Example**: For `rhs = [A, B]` where both `A` and `B` are nullable, it generates `[[A, B], [A], [B], []]`.

### `eliminateUnitProductions(Grammar grammar)`
Unit productions (of the form A → B) are eliminated by computing the unit closure for each non-terminal (all non-terminals reachable via unit productions). For each non-terminal in the closure, the method copies its non-unit productions to the original non-terminal, effectively bypassing the unit productions.

- **Example**: If `A → B`, `B → b`, then `A → b` is added, and `A → B` is removed.

### `getUnitClosure(String symbol, Grammar grammar)`
- **Purpose**: Finds all non-terminals reachable from `symbol` via unit productions.
- **How it Works**:
    - Uses a stack-based depth-first search (DFS).
    - Starts with `symbol`, explores each production; if RHS is a single non-terminal, adds it to the closure and continues.
    - Returns the set of all reachable non-terminals.
- **Example**: For `A → B`, `B → C`, returns `{A, B, C}` for `A`.

### `eliminateInaccessibleSymbols(Grammar grammar)`
This method removes non-terminals that cannot be reached from the start symbol. Using a breadth-first search (BFS), it traverses the grammar starting from the start symbol, marking reachable non-terminals. Any non-terminal not marked as reachable is removed from the grammar.

- **Example**: If `S → A`, `A → a`, but `B → b` exists with no path from `S`, then `B` is removed.

### `eliminateNonProductiveSymbols(Grammar grammar)`
Non-productive symbols (those that cannot derive a string of terminals) are identified and removed. The method iteratively builds a set of productive non-terminals by checking if at least one of their productions consists solely of terminals or productive non-terminals.

- **Example**: If `A → B`, but `B` has no productions leading to terminals, `A` and `B` are removed.

### `convertToCNF(Grammar grammar)`
he final step ensures all productions adhere to CNF rules. Terminals in productions with more than one symbol are replaced with new non-terminals, and long productions are broken down into binary productions. For example, A → a B C becomes A → T1 X1, T1 → a, and X1 → B C, where T1 and X1 are newly introduced non-terminals.
- **Example**: `S → a B C` becomes `S → T1 X1`, `T1 → a`, `X1 → B C`.

---

### 3. `Main` Class
The Main class demonstrates the normalization process using a sample grammar. It defines a grammar with non-terminals {S, A, B, C, D}, terminals {a, b}, and a set of production rules. The original grammar is printed, normalized to CNF using GrammarNormalizer, and the resulting CNF grammar is displayed.
- **Output**: Shows the transformation from the original grammar to CNF, with all steps applied.

---

## Summary of the Process
The conversion to Chomsky Normal Form is a systematic process involving the elimination of ε-productions, unit productions, inaccessible symbols, and non-productive symbols, followed by restructuring the remaining productions into the required binary or terminal form. This implementation follows the standard algorithm, ensuring the resulting grammar is both correct and efficient for parsing. The modular design allows for easy extension and adaptation to different grammars, fulfilling the project's objectives.
1. **Epsilon Removal**: Removes `ε` productions and adjusts for nullable symbols.
2. **Unit Production Removal**: Eliminates `A → B` by copying non-unit productions.
3. **Inaccessible Removal**: Removes unreachable non-terminals.
4. **Non-Productive Removal**: Removes non-terminals that cannot produce terminal strings.
5. **CNF Conversion**: Ensures all productions are either `A → BC` or `A → c`.

This implementation follows the standard algorithm for converting a CFG to CNF, ensuring the resulting grammar generates the same language while adhering to CNF constraints.