package com.jcrawleydev.gemsdrop.game.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreRecordsUtils {


    public static List<String> mergeHighScores(int score, Set<String> highScoreSet){
        var highScores = new ArrayList<>(highScoreSet);
        var scoreStr = String.valueOf(score);
        highScores.add(scoreStr);
        Collections.sort(highScores);
        highScores.remove(highScores.size()-1);
        return highScores;
    }

/*
    public static List<Integer> getOrderedHighScores(Set<String> highScores){
        return highScores.stream()
                .map(Integer::valueOf)
                .sorted((o1, o2) -> o2 - o1)
                .collect(Collectors.toList());
    }

 */


    public static List<String> getOrderedHighScoreStrings(Set<String> highScores){
        var list = new ArrayList<>(highScores);
        Collections.sort(list);
        return list;
    }


    public static String createPropStrFrom(List<String> scores){
        var str = new StringBuilder();
        for(var score : scores){
            str.append(score);
            str.append(",");
        }
        var output = str.toString();
        return output.substring(0, output.length()-1);
    }
}
