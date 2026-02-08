package com.jcrawleydev.gemsdrop.game.score;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreRecordsUtils {


    public static Set<String> mergeHighScores(int score, Set<String> highScoreSet){
        var highScores = new HashSet<>(highScoreSet);
        highScores.add(String.valueOf(score));

        var highScoreInts = highScores
                .stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList()); // min API 34 for simple toList() call.

        return highScoreInts
                .stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }


    public static List<Integer> getOrderedHighScores(Set<String> highScores){
       return highScores.stream()
                .map(Integer::valueOf)
                .sorted((o1, o2) -> o2 - o1)
                .collect(Collectors.toList());
    }

}
