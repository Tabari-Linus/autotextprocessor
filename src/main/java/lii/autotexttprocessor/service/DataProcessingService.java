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
    public List<String> findMostFrequentWords(Map<String, Long> wordFrequencyMap, int topN) {
        return wordFrequencyMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(topN)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}