package com.efimchick.ifmo;

import com.efimchick.ifmo.util.CourseResult;
import com.efimchick.ifmo.util.Person;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Collecting {
    private static final int RIGHT_RANGE_A_MARK = 100;
    private static final int LEFT_RANGE_A_MARK = 90;
    private static final int LEFT_RANGE_B_MARK = 83;
    private static final int LEFT_RANGE_C_MARK = 75;
    private static final int LEFT_RANGE_D_MARK = 68;
    private static final int LEFT_RANGE_E_MARK = 60;

    public int sum(IntStream collectionInt) {
        return collectionInt.sum();
    }

    public int production(IntStream collectionInt) {
        return collectionInt.reduce((x, y) -> x * y).getAsInt();
    }

    public int oddSum(IntStream collectionInt) {
        return collectionInt.filter(v -> v % 2 != 0).sum();
    }

    public Map<Integer, Integer> sumByRemainder(Integer remainder, IntStream collectionInt) {
        return collectionInt
                .boxed()
                .collect(Collectors
                        .toMap(o -> o % remainder, Function.identity(), Integer::sum));
    }

    public Map<Person, Double> totalScores(Stream<CourseResult> streamCoursesResult) {
        Map<Person, Map<String, Integer>> allCoursesResult = putInMapStreamCourses(streamCoursesResult);
        long numberOfTasks = getNumberOfAllTasks(allCoursesResult);
        Map<Person, Integer> personAllScores = allCoursesResult
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        v -> v.getValue()
                                .values()
                                .stream()
                                .mapToInt(i -> i)
                                .sum()));
        return personAllScores
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().doubleValue() / numberOfTasks));
    }

    public Double averageTotalScore(Stream<CourseResult> streamCoursesResult) {
        Map<Person, Map<String, Integer>> allCoursesResult = putInMapStreamCourses(streamCoursesResult);
        long numberOfTasks = getNumberOfAllTasks(allCoursesResult);
        long numberOfPersons = allCoursesResult.size();
        return (double) allCoursesResult
                .values()
                .stream()
                .flatMap(k -> k.values()
                        .stream())
                .mapToInt(i -> i)
                .sum() / (numberOfPersons * numberOfTasks);
    }

    public Map<String, Double> averageScoresPerTask(Stream<CourseResult> streamCoursesResult) {
        Map<Person, Map<String, Integer>> allCoursesResult = putInMapStreamCourses(streamCoursesResult);
        Map<String, Integer> sumScoresByTask = allCoursesResult
                .values()
                .stream()
                .flatMap(k -> k.entrySet().stream())
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.summingInt(Map.Entry::getValue)));
        long numberOfPersons = allCoursesResult.size();
        return sumScoresByTask
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().doubleValue() / numberOfPersons));
    }

    public Map<Person, CharSequence> defineMarks(Stream<CourseResult> streamCoursesResult) {
        return totalScores(streamCoursesResult)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> markByAverage(v.getValue())));

    }

    protected CharSequence markByAverage(Double averageMark) {
        return ((averageMark > LEFT_RANGE_A_MARK && averageMark <= RIGHT_RANGE_A_MARK) ? "A" :
                (averageMark >= LEFT_RANGE_B_MARK && averageMark <= LEFT_RANGE_A_MARK) ? "B" :
                        (averageMark >= LEFT_RANGE_C_MARK && averageMark < LEFT_RANGE_B_MARK) ? "C" :
                                (averageMark >= LEFT_RANGE_D_MARK && averageMark < LEFT_RANGE_C_MARK) ? "D" :
                                        (averageMark >= LEFT_RANGE_E_MARK && averageMark < LEFT_RANGE_D_MARK) ? "E" : "F");
    }

    public String easiestTask(Stream<CourseResult> streamCoursesResult) {
        return averageScoresPerTask(streamCoursesResult)
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()).get().getKey();
    }

    public Collector printableStringCollector() {
        return Collector.of(
                DrawTable::new,
                (result, course) ->
                        result.parseCourseResult((CourseResult) course),
                (result1, result2) -> {
                    result1.parseCourseResult(result2.allCourseResult.get(0));
                    return result1;
                },
                DrawTable::table);
    }

    private Map<Person, Map<String, Integer>> putInMapStreamCourses(Stream<CourseResult> streamCoursesResult) {
        return streamCoursesResult
                .collect(Collectors.toMap(CourseResult::getPerson, CourseResult::getTaskResults));
    }

    private long getNumberOfAllTasks(Map<Person, Map<String, Integer>> allCoursesResult) {
        Set<String> allTasks = allCoursesResult.entrySet().stream()
                .flatMap(k -> k.getValue().entrySet().stream())
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        return allTasks.size();
    }

}
