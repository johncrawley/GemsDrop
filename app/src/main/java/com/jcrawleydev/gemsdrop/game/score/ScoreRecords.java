package com.jcrawleydev.gemsdrop.game.score;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ScoreRecords {

    private final Context context;
    private enum PrefName { PREVIOUS_SCORE, HIGH_SCORES }


    public ScoreRecords(Context context){
        this.context = context;
        saveDefaultHighScores();
    }


    public SharedPreferences getPrefs(){
        return context.getSharedPreferences("GEM_DROP_PREFS", Context.MODE_PRIVATE);
    }


    public void saveScore(int score){
        saveInt(PrefName.PREVIOUS_SCORE, score);
        var highScoreSet = getHighScores();
        var amendedHighScores = ScoreRecordsUtils.mergeHighScores(score, highScoreSet);
        saveHighScores(amendedHighScores);
    }


    public List<Integer> getOrderedHighScores(){
        return ScoreRecordsUtils.getOrderedHighScores(getHighScores());
    }


    public int getMostRecentScore(){
        return getInt(PrefName.PREVIOUS_SCORE);
    }


    public void saveDefaultHighScores(){
        var existingHighScores = getHighScores();
        if(existingHighScores.isEmpty()){
            saveHighScores(Set.of("1000000", "800000", "5000000", "200000", "100000", "80000", "70000", "50000"));
        }
    }


    private void saveHighScores(Set<String> scores){
        getPrefs()
                .edit()
                .putStringSet(PrefName.HIGH_SCORES.name(), scores)
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
