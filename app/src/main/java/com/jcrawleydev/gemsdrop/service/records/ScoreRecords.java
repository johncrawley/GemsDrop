package com.jcrawleydev.gemsdrop.service.records;

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


    public List<String> getOrderedHighScores(){
        return ScoreRecordsUtils.getOrderedHighScores(getHighScores());

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


    public Set<String> getHighScores(){
        return getPrefs().getStringSet(PrefName.HIGH_SCORES.name(), Collections.emptySet());
    }


    private void saveInt(PrefName prefName, int value){
        getPrefs()
                .edit()
                .putInt(prefName.name(), value)
                .apply();
    }

}
