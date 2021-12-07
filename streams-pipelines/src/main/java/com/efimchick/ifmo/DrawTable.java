package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DrawTable {
    protected List<CourseResult> allCourseResult = new ArrayList<>();
    private final static DecimalFormat formatForDouble = new DecimalFormat("##.00");
    private final static String STUDENT = "Student";
    private final static String MARK = "Mark";
    private final static String TOTAL = "Total";
    private final static String AVERAGE = "Average";
    private final static String DASH = " |";

    public void parseCourseResult(CourseResult courseResult) {
        for (int i = 0; i < allCourseResult.size(); i++) {
            if (allCourseResult.get(i).getPerson().getLastName().compareTo(courseResult.getPerson().getLastName()) > 0) {
                allCourseResult.add(i, courseResult);
                return;
            }
        }
        allCourseResult.add(courseResult);
    }

    public String table() {
        Map<Person, Double> totalScoresByPerson = new Collecting().totalScores(allCourseResult.stream());
        HashSet<String> allTasks = allCourseResult
                .stream()
                .flatMap(k -> k.getTaskResults().keySet().stream())
                .sorted()
                .collect(Collectors.toCollection(LinkedHashSet::new));
        List<List<String>> rowsWithFinalResult = new ArrayList<>();
        rowsWithFinalResult.add(createFirstRow(allTasks));
        createAllRowsWithoutFirstAndLast(totalScoresByPerson, allTasks, rowsWithFinalResult);
        rowsWithFinalResult.add(createLastRow(totalScoresByPerson, allTasks));
        return createString(rowsWithFinalResult);
    }

    private List<String> createFirstRow(HashSet<String> allTasks) {
        List<String> headers = new ArrayList<>(allTasks);
        headers.add(0, STUDENT);
        headers.add(TOTAL);
        headers.add(MARK);
        return headers;
    }

    private void createAllRowsWithoutFirstAndLast(Map<Person, Double> totalScoresByPerson, HashSet<String> allTasks, List<List<String>> rowsWithFinalResult) {
        Map<Person, CharSequence> personMarks = new Collecting().defineMarks(allCourseResult.stream());
        for (CourseResult element : allCourseResult) {
            List<String> newRow = new ArrayList<>(checkMarks(element.getTaskResults(), allTasks));
            String fullNamePerson = element.getPerson().getLastName() + " " + element.getPerson().getFirstName();
            newRow.add(0, fullNamePerson);
            newRow.add(formatForDouble.format(totalScoresByPerson.get(element.getPerson())));
            newRow.add(personMarks.get(element.getPerson()).toString());
            rowsWithFinalResult.add(newRow);
        }
    }

    private List<String> createLastRow(Map<Person, Double> totalScoresByPerson, HashSet<String> allTasks) {
        Map<String, Double> allTaskWithAverageMarks = new Collecting().averageScoresPerTask(allCourseResult.stream());
        List<String> lastRow = new ArrayList<>();
        lastRow.add(AVERAGE);
        for (String str : allTasks) {
            for (Map.Entry entry : allTaskWithAverageMarks.entrySet()) {
                if (str.equals(entry.getKey())) {
                    lastRow.add(formatForDouble.format(entry.getValue()));
                }
            }
        }
        Double totalAverage = totalScoresByPerson.values().stream().mapToDouble(Double::doubleValue).average().getAsDouble();
        lastRow.add(formatForDouble.format(totalAverage));
        lastRow.add(new Collecting().markByAverage(totalAverage).toString());
        return lastRow;
    }

    public String createString(List<List<String>> rows) {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }
        StringBuilder formatBuilder = new StringBuilder("%-" + maxLengths[0] + "s" + DASH);
        for (int i = 1; i < maxLengths.length; i++) {
            formatBuilder.append("%").append(maxLengths[i] + 1).append("s").append(DASH);
        }
        StringBuilder result = new StringBuilder();
        for (List<String> row : rows) {
            result.append(String.format(formatBuilder.toString(), row.toArray())).append("\n");
        }
        return result.deleteCharAt(result.length() - 1).toString();
    }

    public List<String> checkMarks(Map<String, Integer> tasksAndResults, HashSet<String> allTasks) {
        List<String> marks = new ArrayList<>();
        for (String task : allTasks) {
            if (tasksAndResults.containsKey(task)) {
                marks.add(tasksAndResults.get(task).toString());
            } else {
                marks.add("0");
            }
        }
        return marks;
    }
}
