# Parser & Building an Abstract Syntax Tree for a Game Command Language

### Course: Formal Languages & Finite Automata
### Author: Temciuc Adelina, FAF-242

---

## Overview

Parsing is the process of analyzing a sequence of tokens to determine its grammatical structure according to a given formal grammar. The result is often a parse tree or an Abstract Syntax Tree (AST) – a hierarchical representation that captures the essential syntactic structure of the input while abstracting away superficial details like parentheses or whitespace. ASTs are widely used in compilers, interpreters, and domain‑specific languages because they separate parsing from execution or code generation.

This report presents a parser and AST for a simple game command language. The language supports commands such as `move`, `attack`, `jump`, `collect`, and `teleport`, each with optional arguments (directions, targets, items, or coordinates). The implementation includes a lexer that uses regular expressions to classify tokens, a recursive‑descent parser that builds an AST, and a set of AST node classes that can be easily extended for interpretation or code generation.

---

## Objectives

1. Get familiar with parsing, its principles, and manual implementation techniques.
2. Understand the concept of an Abstract Syntax Tree (AST) and its advantages over raw parse trees.
3. Extend the previous lab work (Lab 3 – lexical analysis) by:
    - Using **regular expressions** to identify token types.
    - Implementing **AST data structures** tailored to the game command language.
    - Building a **simple parser** that extracts syntactic information and constructs an AST from the input text.

---

## Implementation Description

### Lexical Analysis (Tokenizer)

The lexer (`Lexer.java`) reads an input string and breaks it into a list of tokens. Unlike the previous lab, token types are now identified using **regular expressions** for clarity and maintainability.

```java
private static final List<PatternEntry> PATTERNS = Arrays.asList(
    new PatternEntry(TokenType.COMMAND,   Pattern.compile("^(move|attack|jump|collect|teleport)$")),
    new PatternEntry(TokenType.DIRECTION, Pattern.compile("^(left|right|forward|back)$")),
    new PatternEntry(TokenType.TARGET,    Pattern.compile("^(goblin|dragon|villager|knight)$")),
    new PatternEntry(TokenType.ITEM,      Pattern.compile("^(coin|potion|key)$")),
    new PatternEntry(TokenType.KEYWORD,   Pattern.compile("^(to|at)$")),
    new PatternEntry(TokenType.NUMBER,    Pattern.compile("^\\d+$")),
    new PatternEntry(TokenType.LPAREN,    Pattern.compile("^\\(")),
    new PatternEntry(TokenType.RPAREN,    Pattern.compile("^\\)")),
    new PatternEntry(TokenType.COMMA,     Pattern.compile("^,$")),
    new PatternEntry(TokenType.IDENTIFIER, Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$"))
);
```

Before tokenization, the input is preprocessed by adding spaces around parentheses and commas so that these symbols become separate tokens. Each word is then matched against the list of regex patterns in order; the first matching pattern determines the token type. This approach makes it easy to add new keywords or operators by simply inserting a new `PatternEntry`.
### Parsing

The `Parser` class takes the tokens from the lexer and constructs an AST. It implements a recursive descent parser with methods for parsing different grammar rules.

```
program    → command*
command    → move | attack | jump | collect | teleport
move       → "move" [direction] ["to" target]
attack     → "attack" [target]
jump       → "jump"
collect    → "collect" [item]
teleport   → "teleport" coordinate
coordinate → "(" number "," number ")"
```

The parser provides a method for each nonterminal. For example, the `parseMove` method:


```java
private MoveNode parseMove() {
    DirectionNode dir = null;
    TargetNode target = null;

    if (check(TokenType.DIRECTION)) {
        dir = new DirectionNode(current().getText());
        consume(TokenType.DIRECTION);
    }
    if (check(TokenType.KEYWORD) && current().getText().equals("to")) {
        consume(TokenType.KEYWORD);
        if (check(TokenType.TARGET)) {
            target = new TargetNode(current().getText());
            consume(TokenType.TARGET);
        } else {
            throw new RuntimeException("Expected TARGET after 'to'");
        }
    }
    return new MoveNode(dir, target);
}
```

The parser uses lookahead (peeking at the current token) to decide which grammar rule to apply. If a required token is missing, it throws a descriptive runtime exception.
### Abstract Syntax Tree (AST)

The AST is defined as a set of Java classes, all implementing the marker interface ASTNode. The hierarchy is designed to be both type‑safe and easy to traverse.

- `CommandNode` abstract base for all commands; stores the command name.
- `MoveNode`,`AttackNode`,`JumpNode`,`CollectNode`,`TeleportNode` concrete command nodes, each storing the relevant arguments
- `MoveNode`,`AttackNode`,`JumpNode`,`CollectNode`  leaf nodes representing simple values.
  All nodes override `toString()`to provide a human‑readable representation of the AST. For instance:
```java
public class MoveNode extends CommandNode {
    private final DirectionNode direction;
    private final TargetNode target;

    public MoveNode(DirectionNode direction, TargetNode target) {
        super("move");
        this.direction = direction;
        this.target = target;
    }

    @Override
    public String toString() {
        return String.format("MoveCommand{direction=%s, target=%s}",
                direction != null ? direction : "none",
                target != null ? target : "none");
    }
}
```
This structure makes it straightforward to later implement an interpreter (by walking the AST and performing actions) or a code generator.

```java
public final class BinaryExpression implements Expression {
    private final Expression expr1, expr2;
    private final char operation;
    
    // Constructor and getters...

    @Override
    public double eval() {
        switch (operation) {
            case '-': return expr1.eval() - expr2.eval();
            case '*': return expr1.eval() * expr2.eval();
            case '/': return expr1.eval() / expr2.eval();
            case '+':
            default:
                return expr1.eval() + expr2.eval();
        }
    }
    // ...
}
```

The `BinaryExpression` class represents a binary operation (e.g., `2 + 3`). It holds the left and right operands as expressions, and the operation to perform. When evaluated, it evaluates both operands and combines them according to the operation.


## Main Application

The `Main` class ties everything together:

```java
public static void main(String[] args) {
    String input = "move forward to dragon teleport (10, 9)";

    Lexer lexer = new Lexer(input);
    List<Token> tokens = lexer.tokenize();

    Parser parser = new Parser(tokens);
    List<CommandNode> ast = parser.parseProgram();

    for (CommandNode cmd : ast) {
        System.out.println(cmd);
    }
}
```
For the input `move forward to dragon teleport (10, 9)`, the output is:
```
MoveCommand{direction=forward, target=dragon}
TeleportCommand{coordinate=(10,9)}
```

This shows that the parser correctly recognises two separate commands and extracts all relevant syntactic information.
## Core Components
## Core Components

| Component | Responsibility |
|-----------|----------------|
| `TokenType` (enum) | Defines all possible token categories (COMMAND, DIRECTION, NUMBER, LPAREN, etc.). |
| `Lexer` | Converts raw input into a token stream using regular expressions. |
| `Parser` | Consumes tokens and builds an AST using recursive descent. |
| `ASTNode` interface | Marks all AST nodes. |
| `CommandNode` abstract class | Base for all command‑related nodes. |
| Concrete command nodes (`MoveNode`, etc.) | Represent specific commands and their arguments. |
| Expression nodes (`DirectionNode`, `TargetNode`, etc.) | Represent simple value types used as arguments. |
## Conclusion

This lab work successfully extends the previous lexical analyser by:
*   Using regular expressions to classify tokens in a clean, declarative manner.
*   Designing a full AST hierarchy that accurately models the game command language.
*   Implementing a recursive‑descent parser that builds the AST and reports syntax errors.
The resulting system can parse a variety of command sequences and produces a structured representation that can be easily interpreted, translated, or analysed further. The modular design allows adding new commands or modifying the grammar with minimal changes – only new AST classes and corresponding parser methods are needed.

## References:
[1] [Parsing Wiki](https://en.wikipedia.org/wiki/Parsing)

[2] [Abstract Syntax Tree Wiki](https://en.wikipedia.org/wiki/Abstract_syntax_tree)
 
[3] [3] Aho, A. V., Lam, M. S., Sethi, R., & Ullman, J. D. (2006). Compilers: Principles, Techniques, and Tools (2nd ed.). Addison‑Wesley.






