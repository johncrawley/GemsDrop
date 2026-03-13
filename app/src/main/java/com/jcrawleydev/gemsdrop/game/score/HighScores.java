package com.jcrawleydev.gemsdrop.game.score;

import static com.jcrawleydev.gemsdrop.game.score.ScoreRecordsUtils.createPropStrFrom;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HighScores {

    private Context context;
    private enum PrefName { PREVIOUS_SCORE, HIGH_SCORES }
    private enum ScorePrefName { SCORE_1, SCORE_2, SCORE_3, SCORE_4, SCORE_5, SCORE_6, SCORE_7, SCORE_8}

    public void init(Context context){
        this.context = context;
        initHighScores();
        reset();
    }


    public SharedPreferences getPrefs(){
        return context.getSharedPreferences("GEM_DROP_PREFS", Context.MODE_PRIVATE);
    }


    public void saveScore(int score){
        log("entered saveScore()");
        saveInt(PrefName.PREVIOUS_SCORE, score);
        var highScoreSet = getHighScores();
        log("saveScore() : highScoreSet retrieved");
        var amendedHighScores = ScoreRecordsUtils.mergeHighScores(score, highScoreSet);
        log("about to saveHighScores()");
        saveHighScores(amendedHighScores);
        log("saveScore() highScore saved!");
    }


    private void log(String msg){
        System.out.println("^^^ HighScores: " + msg);
    }


    public List<String> getOrderedHighScores(){
        return ScoreRecordsUtils.getOrderedHighScoreStrings(getHighScores());
    }


    public int getMostRecentScore(){
        return getInt(PrefName.PREVIOUS_SCORE);
    }


    public void initHighScores(){
        var existingHighScores = getHighScores();
        if(existingHighScores.isEmpty()){
            reset();
        }
    }


    public void reset(){
           // var scoreStr = "1000000, 800000, 500000, 200000, 100000, 80000, 70000, 50000";
            var scoreStr = "10000, 8000, 5000, 2000, 1000, 800, 700, 500";
            var scoreArray= scoreStr.split(",");
            var scoreList = Arrays.asList(scoreArray);
            saveHighScores(scoreList);
    }


    private void saveHighScores(List<String> scores){
        var scoresStr = createPropStrFrom(scores);
        getPrefs().edit()
            .putString(PrefName.HIGH_SCORES.name(), scoresStr)
                .apply();
    }




    private Set<String> getHighScores(){
        return getPrefs().getStringSet(PrefName.HIGH_SCORES.name(), Collections.emptySet());
    }


    private int getInt(PrefName prefName){
        return getPrefs().getInt(prefName.name(), 0);
    }


    private void saveInt(PrefName prefName, int value){
        getPrefs()
                .edit()
                .putInt(prefName.name(), value)
                .apply();
    }

}
