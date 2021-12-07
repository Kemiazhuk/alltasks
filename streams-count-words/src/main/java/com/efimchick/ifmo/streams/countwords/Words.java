package com.efimchick.ifmo.streams.countwords;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Words {
    private final static int QUANTITY_WORD = 10;
    private final static String REGEX_FOR_SPLIT_WORDS = "[а-яa-z]{4,}\\b";
    private final static String DASH = " - ";
    private final static String NEW_LINE = "\n";

    public String countWords(List<String> lines) {
        Map<String, Long> quantityWords = initMapWordsAndQuantity(lines);
        quantityWords = deleteWords(quantityWords);
        quantityWords = sortByValues(quantityWords);
        return quantityWords.entrySet()
                .stream()
                .map(k -> k.getKey() + DASH + k.getValue())
                .collect(Collectors.joining(NEW_LINE));
    }

    private Map<String, Long> initMapWordsAndQuantity(List<String> lines) {
        Pattern pattern = Pattern.compile(REGEX_FOR_SPLIT_WORDS);
        return lines.stream()
                .flatMap(line -> StreamSupport.stream(new MatchItr(pattern.matcher(line.toLowerCase())), false))
                .collect(Collectors.groupingBy(o -> o, TreeMap::new, Collectors.counting()));
    }

    private Map<String, Long> deleteWords(Map<String, Long> quantityWords) {
        return new TreeMap<>(quantityWords.entrySet()
                .stream()
                .filter(k -> k.getValue() >= QUANTITY_WORD)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private Map<String, Long> sortByValues(Map<String, Long> quantityWords) {
        return quantityWords.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
}
