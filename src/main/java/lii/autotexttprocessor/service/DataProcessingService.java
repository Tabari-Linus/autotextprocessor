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

}