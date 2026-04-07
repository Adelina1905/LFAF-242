# Lexer & Scanner

### Course: Formal Languages & Finite Automata
### Author: Temciuc Adelina

----

## Theory

A **Lexer**, also known as a lexical analyzer, is a fundamental component of a compiler or interpreter that processes input text and converts it into a sequence of tokens. A token is a structured representation of meaningful elements in a programming language, such as keywords, identifiers, literals, operators, and punctuation. The lexer plays a crucial role in breaking down the raw input into a form that is easier for subsequent stages, like parsing, to process.

- A lexer operates using a set of predefined rules, often specified using regular expressions or finite automata, to recognize and classify different tokens. It scans the input sequentially, grouping characters into meaningful units.
- The concept of lexical analysis can be related to finite automata, as a lexer can be implemented using **Deterministic Finite Automata (DFA)** or **Nondeterministic Finite Automata (NFA)** to efficiently recognize token patterns.

A **Deterministic Lexer** follows a strict rule where, given a current state and an input character, there is only one possible next state. This approach ensures predictability and efficiency. On the other hand, a **Nondeterministic Lexer** may have multiple possible transitions for a given input, requiring additional steps to resolve ambiguity, such as backtracking or lookahead techniques.

Lexers can be categorized based on their complexity and functionality:
- **Simple Lexers** work with a basic set of rules and operate in a single pass, making them efficient for straightforward tokenization tasks.
- **Complex Lexers** may involve multiple passes, require symbol tables, and handle context-sensitive lexical analysis, such as resolving reserved keywords from identifiers.

Despite its role as a separate phase in many compiler architectures, lexical analysis is closely tied to syntax analysis, as the output of the lexer serves as the input for the parser. Optimizing the lexer for efficiency ensures faster and more reliable language processing.


## Objectives:

- Understand what lexical analysis [1] is.

- Get familiar with the inner workings of a lexer/scanner/tokenizer.

- Implement a sample lexer and show how it works.

Note: Just because too many students were showing me the same idea of lexer for a calculator, I've decided to specify requirements for such case. Try to make it at least a little more complex. Like, being able to pass integers and floats, also to be able to perform trigonometric operations (cos and sin). But it does not mean that you need to do the calculator, you can pick anything interesting you want

## Implementation Description

The **Lexer** class is responsible for tokenizing an input string into meaningful components, which can then be used for parsing. The lexer scans the input, identifies tokens based on predefined rules, and classifies them into categories such as **keywords**, **identifiers**, **numbers**, **strings**, **operators**.

# Core Functionalities

## Lexer Class

The **Lexer** class is the central component of this lexical analysis implementation. Its role is to process an input command string and convert it into a sequence of tokens. These tokens represent meaningful elements of a simple game command language, such as commands, directions, targets, items, numbers, and punctuation symbols.

Unlike traditional character-by-character lexers, this implementation processes the input primarily by splitting it into words and symbols and then classifying each element according to predefined language rules.

---

## Key Components of the Lexer

The lexer defines several predefined sets that represent the vocabulary of the command language. These sets allow the lexer to quickly determine the role of each word encountered in the input.

### Commands

The `COMMANDS` set defines all valid actions that a player can perform in the game. These include commands such as `move`, `attack`, `jump`, `teleport` and `collect`. Whenever one of these words appears in the input, the lexer generates a `COMMAND` token.

### Directions

The `DIRECTIONS` set contains words describing movement directions. Examples include `left`, `right`, `forward`, and `back`. These words are recognized as `DIRECTION` tokens and are typically used together with movement commands.

### Targets

The `TARGETS` set defines entities in the game world that the player can interact with. Examples include `goblin`, `dragon`, `villager`, and `knight`. When one of these words appears, the lexer creates a `TARGET` token.

### Items

The `ITEMS` set contains collectible objects such as `coin`, `potion`, and `key`. These words are converted into `ITEM` tokens.

### Keywords

The `KEYWORDS` set contains structural words that connect different parts of a command. In this implementation the keywords include `to` and `at`, and they generate `KEYWORD` tokens.

---

## Fields

The lexer maintains several internal fields used during the tokenization process.

The `input` field stores the original command string that needs to be analyzed. This string represents the raw input provided by the user.

The `tokens` field is a list that stores all tokens produced during lexical analysis. As the lexer processes the input, it continuously adds new tokens to this list.

The `pos` field represents the current position in the input string. Although the current implementation primarily relies on word splitting rather than character scanning, the position variable can be used to track the lexer’s progress if needed.

---

## Constructor

The constructor initializes the lexer with the provided input string.

```java
public Lexer(String input) {
    this.input = input;
}
```

After the lexer object is created, the `tokenize()` method can be called to begin the lexical analysis process.

---

## Main Method: tokenize()

The `tokenize()` method performs the core lexical analysis. It reads the input string, separates it into smaller elements, and converts those elements into tokens.

The first step of this process is preprocessing the input. Parentheses and commas are surrounded with spaces so they can be treated as independent tokens.

```java
String processedInput = input.replace("(", " ( ")
        .replace(")", " ) ")
        .replace(",", " , ");
```

This transformation ensures that symbols such as `(`, `)` and `,` will not remain attached to surrounding words.

After preprocessing, the input string is split into individual words using whitespace as a delimiter.

```java
String[] words = processedInput.split("\\s+");
```

Each word in this array is then analyzed and classified according to the language rules.

---

## Token Classification

For every word encountered in the input, the lexer determines its token type through a series of conditional checks.

If the word belongs to the predefined `COMMANDS` set, a `COMMAND` token is created.

```java
tokens.add(new Token(TokenType.COMMAND, word));
```

If the word belongs to the `DIRECTIONS` set, a `DIRECTION` token is generated.

Words found in the `TARGETS` set produce a `TARGET` token, while words in the `ITEMS` set produce an `ITEM` token.

If a word belongs to the `KEYWORDS` set, it is converted into a `KEYWORD` token.

Numbers are detected using a regular expression that checks whether the word consists only of digits.

```java
word.matches("\\d+")
```

If the expression matches, the lexer generates a `NUMBER` token.

The lexer also explicitly recognizes punctuation symbols used in commands. Parentheses generate `LPAREN` and `RPAREN` tokens, while commas generate `COMMA` tokens.

If a word does not match any predefined category, the lexer classifies it as an `IDENTIFIER`. This allows the language to support custom names or values that are not part of the predefined vocabulary.

---

## End of Input Handling

After all words have been processed, the lexer adds a special token representing the end of the input stream.

```java
tokens.add(new Token(TokenType.EOF, ""));
```

This token helps later stages of processing, such as parsing, determine when the input has been completely analyzed.

---

## Token Class

The **Token** class represents a single lexical unit produced by the lexer. Each token contains two important pieces of information: its type and its textual representation.

The `type` attribute indicates the category of the token, such as `COMMAND`, `TARGET`, or `NUMBER`. The `text` attribute stores the exact substring extracted from the input.

The constructor initializes both attributes.

```java
public Token(TokenType type, String text) {
    this.type = type;
    this.text = text;
}
```

The class also provides getter methods that allow other parts of the program to access the token’s type and text.

For debugging and visualization purposes, the `toString()` method returns a readable representation of the token.

```java
@Override
public String toString() {
    return type + " -> " + text;
}
```

This format makes it easy to inspect the token stream produced by the lexer.

---

## TokenType Enum

The **TokenType** enumeration defines all possible token categories that can be generated during lexical analysis. These types describe the role that each token plays in the command language.

```java
COMMAND
DIRECTION
TARGET
ITEM
COMMA
LPAREN
RPAREN
KEYWORD
NUMBER
IDENTIFIER
EOF
```

These categories allow the lexer to transform raw text input into a structured representation that can later be processed by a parser or interpreter.

---

## Example

Consider the following input command:

```text
move forward to dragon
```

When the lexer processes this command, it generates the following sequence of tokens:

```text
COMMAND -> move
DIRECTION -> forward
KEYWORD -> to
TARGET -> dragon
EOF ->
```

Each token represents a specific component of the command, making it easier for the next stages of the program to understand and execute the instruction.

---

## Summary

This lexer implements a simple lexical analyzer for a small command-based language used in a game environment. By splitting the input into words and matching them against predefined categories, the lexer efficiently converts raw text into structured tokens.

The produced token stream serves as the foundation for further processing steps such as parsing, interpretation, or execution of game commands.## References
- [Wikipedia](https://en.wikipedia.org/wiki/Lexical_analysis)
- [A sample of lexical analyzer](https://llvm.org/docs/tutorial/MyFirstLanguageFrontend/LangImpl01.html)