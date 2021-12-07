package com.epam.rd.autocode.floodfill;

import java.util.*;

public class FloodFillMethod implements FloodFill {
    private String NEWLINE = "\n";

    @Override
    public void flood(String earthMap, FloodLogger logger) {
        logger.log(earthMap);
        if (!earthMap.contains(String.valueOf(LAND))) {
            return;
        }
        String[] mapLines = earthMap.split(NEWLINE);

        HashMap<Integer, List<Integer>> coordinates = searchWaterArea(mapLines);
        StringBuilder newMap = floodNeighbours(mapLines, new StringBuilder(earthMap), coordinates);
        flood(newMap.toString(), logger);
    }

    private HashMap<Integer, List<Integer>> searchWaterArea(String[] mapLines) {
        HashMap<Integer, List<Integer>> coordinates = new HashMap<>();
        for (int i = 0; i < mapLines.length; i++) {
            for (int j = 0; j < mapLines[i].length(); j++) {
                if (mapLines[i].charAt(j) == WATER) {
                    HashMap<Integer, List<Integer>> tempCoordinates = checkNeighbours(mapLines, i, j);
                    for (Map.Entry<Integer, List<Integer>> entry : tempCoordinates.entrySet()) {
                        if (coordinates.containsKey(entry.getKey())) {
                            coordinates.get(entry.getKey()).addAll(entry.getValue());
                        } else {
                            coordinates.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
        }
        return coordinates;
    }

    private StringBuilder floodNeighbours(String[] mapLines, StringBuilder mapBuilder, HashMap<Integer, List<Integer>> coordinates) {
        int indexForMapBuilder = mapLines[0].length() + 1;
        for (Map.Entry<Integer, List<Integer>> entry : coordinates.entrySet()) {
            for (Integer column : entry.getValue()) {
                int position = entry.getKey() * indexForMapBuilder + column;
                mapBuilder.replace(position, position + 1, String.valueOf(WATER));
            }
        }
        return mapBuilder;
    }

    private HashMap<Integer, List<Integer>> checkNeighbours(String[] mapLines, int line, int column) {
        HashMap<Integer, List<Integer>> coordinates = new HashMap<>();
        if ((line - 1 >= 0) && (mapLines[line - 1].charAt(column) == LAND)) {
            coordinates.put(line - 1, new ArrayList<>(Arrays.asList(column)));
        }
        if ((column + 1 < mapLines[line].length()) && (mapLines[line].charAt(column + 1) == LAND)) {
            coordinates.put(line, new ArrayList<>(Arrays.asList(column + 1)));
        }
        if ((line + 1 < mapLines.length) && (mapLines[line + 1].charAt(column) == LAND)) {
            coordinates.put(line + 1, new ArrayList<>(Arrays.asList(column)));
        }
        if ((column - 1 >= 0) && (mapLines[line].charAt(column - 1) == LAND)) {
            if (coordinates.containsKey(line)) {
                coordinates.get(line).add(column - 1);
            } else {
                coordinates.put(line, new ArrayList(Arrays.asList(column - 1)));
            }
        }
        return coordinates;
    }
}
