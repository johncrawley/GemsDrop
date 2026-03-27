package com.jcrawleydev.gemsdrop.game.score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScoreUtils {


    public static List<String> mergeHighScores(int score, Set<String> highScoreSet){
        var highScores = new ArrayList<>(highScoreSet);
        var scoreStr = String.valueOf(score);
        highScores.add(scoreStr);
        Collections.sort(highScores);
        highScores.remove(highScores.size()-1);
        return highScores;
    }


    public static List<String> addToHighScores(int score, List<String> highScoreList){
        var highScores = new ArrayList<>(highScoreList);
        var scoreStr = String.valueOf(score);
        highScores.add(scoreStr);
        var output = reverseNumericalOrderOf(highScores);
        return output.subList(0, highScoreList.size());
    }


    public static List<String> reverseNumericalOrderOf(List<String> list) {
        return list.stream()
                .map(Integer::valueOf)
                .sorted((f1, f2) -> Integer.compare(f2, f1))
                .map(String::valueOf)
                .collect(Collectors.toList());
    }


    public static List<String> getOrderedHighScoreStrings(String highScoresStr){
        var list = new ArrayList<>(List.of(highScoresStr.split(",")));
        System.out.println("^^^ ScoreUtils getOrderedHighScoreStrings() number of highScores: " + list.size());
        return reverseNumericalOrderOf(list);
    }


    public static String createPropStrFrom(List<String> scores){
        var str = new StringBuilder();
        for(var score : scores){
            str.append(score);
            str.append(",");
        }
        var output = str.toString();
        if(output.isBlank()){
            return "";
        }
        return output.substring(0, output.length()-1);
    }
}
