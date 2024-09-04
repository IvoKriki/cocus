package com.kriki.ivo.cocus.tec.test.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WordSquareService {

    private static final Logger logger = LoggerFactory.getLogger(WordSquareService.class);

    private Set<String> wordDictionary;

    public WordSquareService() {
        loadDictionary();
    }

    // Load the dictionary file into a Set for fast lookups
    private void loadDictionary() {
        try (var inputStream = getClass().getClassLoader().getResourceAsStream("engmix.txt");
             var reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // Store all words in a set
            wordDictionary = reader.lines().collect(Collectors.toSet());
            logger.info("File loaded with {} words.", wordDictionary.size());

        } catch (Exception e) {
            // Log any errors that occur while loading the file
            logger.error("Error loading file: {}", e.getMessage(), e);
        }
    }

    // Main method to generate a word square from url input
    public List<String> generateWordSquare(String input) {
        logger.info("Starting word square generation with input: {}", input);

        // Check if input contains only alphabetic characters
        if (!input.chars().allMatch(Character::isLetter)) {
            logger.warn("Invalid input: contains non-alphabetic characters. Input: {}", input);
            return Collections.emptyList(); // Return empty list if input is invalid
        }

        // Calculate the square size (length should be a perfect square 2x2 3x3 4x4 5x5)
        var squareSize = (int) Math.sqrt(input.length());
        if (squareSize * squareSize != input.length()) {
            logger.warn("Invalid input length: {}", input.length());
            return Collections.emptyList(); // Return empty list if length is not a perfect square
        }

        // Filter words that match the input characters and required length
        var filteredWords = filterWordsByLengthAndChars(input.toLowerCase(), squareSize);
        logger.info("Filtered words: {}", filteredWords);

        // List to store the final word square
        var result = new ArrayList<String>();
        if (!buildWordSquare(new ArrayList<>(), result, squareSize, filteredWords)) {
            logger.info("No valid word square found.");
        }

        logger.info("Finished processing input: {}", input);
        return result;
    }

    // Filters the dictionary to find words that match the given size and can be formed with input letters
    private List<String> filterWordsByLengthAndChars(String letters, int size) {
        var letterFrequency = letters.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, Collectors.counting())); // Count the frequency of each letter

        // Filter dictionary based on word length and character availability
        return wordDictionary.stream()
                .filter(word -> word.length() == size && canFormWord(word, new HashMap<>(letterFrequency)))
                .toList();
    }

    // Checks if a word can be formed from the given characters (letter frequency map)
    private boolean canFormWord(String word, Map<Character, Long> letterFrequency) {
        for (var c : word.toCharArray()) {
            var count = letterFrequency.getOrDefault(c, 0L);
            if (count == 0) return false; // If any character is missing, return false
            letterFrequency.put(c, count - 1); // Decrease the count of used character
        }
        return true; // Return true if canFormWord
    }

    // Recursive method to build the word square using backtracking
    private boolean buildWordSquare(List<String> currentSquare, List<String> result, int size, List<String> words) {
        if (currentSquare.size() == size) {
            if (isValidWordSquare(currentSquare)) {
                result.addAll(currentSquare); // If a valid square is found, add to result
                logger.info("Valid word square found: {}", currentSquare);
                return true; // Return true when a valid square is found
            }
            return false;
        }

        // Build a prefix from the current square to match the next word
        var prefix = currentSquare.stream()
                .map(word -> String.valueOf(word.charAt(currentSquare.size())))
                .collect(Collectors.joining());

        logger.info("Current prefix: {}", prefix);

        // Try each word that starts with the current prefix
        for (var word : words) {
            if (word.startsWith(prefix)) {
                currentSquare.add(word); // Add word to the square
                if (buildWordSquare(currentSquare, result, size, words)) {
                    return true; // If square is valid, return true
                }
                currentSquare.remove(currentSquare.size() - 1); // Backtrack if not valid
            }
        }
        return false; // Return false if no valid square can be built
    }

    // Validates if the current list of words forms a valid word square
    private boolean isValidWordSquare(List<String> square) {
        var size = square.size();
        // Check characters to ensure the square is valid (same words across rows and columns)
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (square.get(i).charAt(j) != square.get(j).charAt(i)) {
                    return false; // Return false if any character does not match
                }
            }
        }
        return true; // Return true if all checks pass
    }
}
