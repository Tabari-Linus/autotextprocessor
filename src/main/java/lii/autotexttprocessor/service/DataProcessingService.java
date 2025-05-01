package lii.autotexttprocessor.service;

import java.util.*;
import java.util.stream.Collectors;

public class DataProcessingService {

    public Map<String, Long> analyzeWordFrequency(String text) {
        return Arrays.stream(text.split("\\W+"))
                .filter(word -> !word.isEmpty())
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

    public Map<String, Long> wordFrequencyAnalysis(String text) {
        if (text == null || text.isEmpty()) {
            return Collections.emptyMap();
        }

        return Arrays.stream(text.split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase())
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(
                        word -> word,
                        Collectors.counting()
                ));
    }

    public List<String> findMostFrequentWords(Map<String, Long> wordFrequencyMap, int topN) {
        return wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

//    public String summarizeText(String text, int maxWords) {
//        return Arrays.stream(text.split("\\s+"))
//                .limit(maxWords)
//                .collect(Collectors.joining(" "));
//    }

    public List<String> extractSentences(String text) {
        return Arrays.stream(text.split("[.!?]\\s*"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public String summarizeText(String text, int maxSentences) {
        List<String> sentences = extractSentences(text);
        Map<String, Integer> wordFrequencies = wordFrequencyAnalysis(text);

        // Simple scoring - sentences with more frequent words are more important
        Map<String, Integer> sentenceScores = new HashMap<>();
        for (String sentence : sentences) {
            int score = Arrays.stream(sentence.split("\\s+"))
                    .mapToInt(word -> wordFrequencies.getOrDefault(word.toLowerCase(), 0))
                    .sum();
            sentenceScores.put(sentence, score);
        }

        return sentenceScores.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(maxSentences)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(". ")) + ".";
    }
}