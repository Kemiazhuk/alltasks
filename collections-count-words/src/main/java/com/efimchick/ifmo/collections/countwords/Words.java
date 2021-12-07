package com.efimchick.ifmo.collections.countwords;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Words {
    private final static int MINIMAL_REPEAT_WORDS = 10;
    private final static String REGEX_FOR_SPLIT_WORDS = "[а-яa-z]{4,}\\b";
    private final static String DASH = " - ";
    private final static String NEW_LINE = "\n";

    public String countWords(List<String> lines) {
        Map<String, Integer> quantityWords = initMapWordsAndQuantity(lines);
        deleteWords(new HashMap<>(quantityWords), quantityWords);
        List<Map.Entry<String, Integer>> resultListOfEntry = sortByValues(quantityWords);
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : resultListOfEntry) {
            if (result.length() == 0) {
                result.append(entry.getKey()).append(DASH).append(entry.getValue());
            } else {
                result.append(NEW_LINE).append(entry.getKey()).append(DASH).append(entry.getValue());
            }
        }
        return result.toString();
    }

    private Map<String, Integer> initMapWordsAndQuantity(List<String> lines) {
        Map<String, Integer> quantityWords = new TreeMap<>();
        for (String str : lines) {
            str = str.toLowerCase();
            Pattern pattern = Pattern.compile(REGEX_FOR_SPLIT_WORDS);
            Matcher matcher = pattern.matcher(str);
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String word = str.substring(start, end);
                if (quantityWords.containsKey(word)) {
                    quantityWords.put(word, quantityWords.get(word) + 1);
                } else {
                    quantityWords.put(word, 1);
                }
            }
        }
        return quantityWords;
    }

    private void deleteWords(Map<String, Integer> mapForChecking, Map<String, Integer> quantityWords) {
        for (String key : mapForChecking.keySet()) {
            if (mapForChecking.get(key) < MINIMAL_REPEAT_WORDS) {
                quantityWords.remove(key);
            }
        }
    }

    private List<Map.Entry<String, Integer>> sortByValues( Map<String, Integer> quantityWords) {
        List<Map.Entry<String, Integer>> listEntry = new LinkedList(quantityWords.entrySet());
        Collections.sort(listEntry, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                if (((Comparable) (o2).getValue()).compareTo((o1).getValue()) == 0) {
                    return ((Comparable) (o1).getKey()).compareTo((o2).getKey());
                } else {
                    return ((Comparable) (o2).getValue()).compareTo((o1).getValue());
                }
            }
        });
        return listEntry;
    }
}
