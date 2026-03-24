package org.example.Lab4;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class RegexGenerator {
    private static final int MAX_REPETITIONS = 5;
    private static final Random random = new Random();
    private static List<String> processingSteps = new ArrayList<>();
    private static List<String> wordFormationSteps = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Regular Expression String Generator");
        System.out.println("----------------------------------");
        System.out.println("Enter regular expressions (one per line, empty line to finish):");

        List<String> regexes = new ArrayList<>();
        String input;
        while (!(input = scanner.nextLine()).isEmpty()) {
            regexes.add(input);
        }

        System.out.println("\nHow many sample strings to generate for each regex? ");
        int sampleCount = Integer.parseInt(scanner.nextLine());

        System.out.println("\nGenerating valid strings for each regex:");
        for (String regex : regexes) {
            System.out.println("\nRegex: " + regex);

            for (int i = 0; i < sampleCount; i++) {
                processingSteps.clear();
                wordFormationSteps.clear();
                wordFormationSteps.add(""); // Start with an empty string

                String generated = generateValidString(regex);
                System.out.println("\nValid string " + (i+1) + ": " + generated);

                System.out.println("\nWord formation step by step:");
                for (int j = 0; j < wordFormationSteps.size(); j++) {
                    System.out.println((j+1) + ". Current word: \"" + wordFormationSteps.get(j) + "\"");
                }

                System.out.println("\nProcessing sequence:");
                for (int j = 0; j < processingSteps.size(); j++) {
                    System.out.println((j+1) + ". " + processingSteps.get(j));
                }
                System.out.println("\n" + "-".repeat(40));
            }
        }

        scanner.close();
    }

    public static String generateValidString(String regex) {
        StringBuilder result = new StringBuilder();
        processRegex(regex, result, 0);
        return result.toString();
    }

    private static int processRegex(String regex, StringBuilder result, int startIndex) {
        int i = startIndex;
        while (i < regex.length()) {
            char c = regex.charAt(i);

            // Handle escape character
            if (c == '\\' && i + 1 < regex.length()) {
                char escapedChar = regex.charAt(i + 1);
                result.append(escapedChar);
                processingSteps.add("Added escaped character: " + escapedChar);
                recordWordFormation(result.toString());
                i += 2;
                continue;
            }

            // Handle groups (options with | operator)
            if (c == '(') {
                int endIndex = findMatchingParenthesis(regex, i);
                if (endIndex == -1) {
                    throw new IllegalArgumentException("Unmatched parenthesis in regex: " + regex);
                }

                String groupContent = regex.substring(i + 1, endIndex);
                String[] options = groupContent.split("\\|");

                // Check if the group has a quantifier
                char quantifier = endIndex + 1 < regex.length() ? regex.charAt(endIndex + 1) : 0;
                int repetitions = getRepetitionCount(quantifier);

                processingSteps.add("Processing group (" + groupContent + ")" +
                        (isQuantifier(quantifier) ? " with quantifier " + quantifier : ""));

                for (int rep = 0; rep < repetitions; rep++) {
                    String selectedOption = options[random.nextInt(options.length)];
                    processingSteps.add("Selected option: " + selectedOption +
                            (repetitions > 1 ? " (repetition " + (rep + 1) + " of " + repetitions + ")" : ""));

                    StringBuilder optionBuilder = new StringBuilder();
                    processRegex(selectedOption, optionBuilder, 0);
                    result.append(optionBuilder);
                    recordWordFormation(result.toString());
                }

                i = endIndex + (isQuantifier(quantifier) ? 2 : 1);
                continue;
            }

            // Handle quantifiers for single characters
            char nextChar = i + 1 < regex.length() ? regex.charAt(i + 1) : 0;
            if (isQuantifier(nextChar)) {
                int repetitions = getRepetitionCount(nextChar);
                processingSteps.add("Processing character '" + c + "' with quantifier " + nextChar +
                        " (" + repetitions + " repetitions)");

                for (int rep = 0; rep < repetitions; rep++) {
                    result.append(c);
                    recordWordFormation(result.toString());
                }
                i += 2;
            } else {
                // Handle single character with no quantifier
                processingSteps.add("Added character: " + c);
                result.append(c);
                recordWordFormation(result.toString());
                i++;
            }
        }

        return i;
    }

    private static void recordWordFormation(String currentWord) {
        wordFormationSteps.add(currentWord);
    }

    private static int findMatchingParenthesis(String regex, int openIndex) {
        int count = 1;
        for (int i = openIndex + 1; i < regex.length(); i++) {
            if (regex.charAt(i) == '(') {
                count++;
            } else if (regex.charAt(i) == ')') {
                count--;
                if (count == 0) {
                    return i;
                }
            }
        }
        return -1; // No matching parenthesis found
    }

    private static boolean isQuantifier(char c) {
        return c == '?' || c == '*' || c == '+' || c == '²' || c == '³' || c == '⁴' || c == '⁺';
    }

    private static int getRepetitionCount(char quantifier) {
        switch (quantifier) {
            case '?': return random.nextInt(2); // 0 or 1
            case '*': return random.nextInt(MAX_REPETITIONS + 1); // 0 to MAX_REPETITIONS
            case '+': return 1 + random.nextInt(MAX_REPETITIONS); // 1 to MAX_REPETITIONS
            case '²': return 2;
            case '³': return 3;
            case '⁴': return 4;
            case '⁺': return 1 + random.nextInt(MAX_REPETITIONS); // Same as +
            default: return 1; // Default is 1 repetition
        }
    }
}