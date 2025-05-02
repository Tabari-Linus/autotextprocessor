package lii.autotexttprocessor.service;

import java.util.*;
import java.util.stream.Collectors;

public class DataProcessingService {

    public Map<String, Long> wordFrequency(String text) {
        return Arrays.stream(text.split("\\W+"))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }



    public List<String> findMostFrequentWords(Map<String, Long> wordFrequencyMap, int topN) {
        return wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }



    public Map<String, Long> summarizeText(String inputText) {
        return Arrays.stream(inputText.split("\\R")) // Split by lines
                .flatMap(line -> Arrays.stream(line.split("\\W+"))) // Split lines into words
                .filter(word -> !word.isBlank()) // Filter out blank words
                .collect(Collectors.collectingAndThen(
                        Collectors.toMap(
                                word -> "Word Count", // Key for word count
                                word -> 1L, // Increment word count
                                Long::sum, // Merge function for word count
                                LinkedHashMap::new // Maintain insertion order
                        ),
                        map -> {
                            map.put("Line Count", inputText.lines().count());
                            map.put("Character Count", inputText.chars().count());
                            return map;
                        }
                ));
    }

    public List<String> extractSentences(String text) {
        return Arrays.stream(text.split("[.!?]\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

//    public String summarizeText(String text, int maxSentences) {
//        List<String> sentences = extractSentences(text);
//        Map<String, Long> wordFrequencies = wordFrequencyAnalysis(text);
//
//        // Simple scoring - sentences with more frequent words are more important
//        Map<String, Long> sentenceScores = new HashMap<>();
//        for (String sentence : sentences) {
//            int score = Arrays.stream(sentence.split("\\s+"))
//                    .mapToInt(word -> wordFrequencies.getOrDefault(word.toLowerCase(), 0))
//                    .sum();
//            sentenceScores.put(sentence, score);
//        }
//
//        return sentenceScores.entrySet().stream()
//                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
//                .limit(maxSentences)
//                .map(Map.Entry::getKey)
//                .collect(Collectors.joining(". ")) + ".";
//    }
}