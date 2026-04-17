package com.jcrawleydev.gemsdrop.game.score;

import static com.jcrawleydev.gemsdrop.game.score.ScoreUtils.createPropStrFrom;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class HighScores {

    private Context context;
    private enum PrefName { HIGH_SCORES_STR}
    public static final String DEFAULT_SCORES = "1000000,80000,50000,30000,20000,10000,8000,5000";


    public void init(Context context){
        this.context = context;
        initHighScores();
    }


    public SharedPreferences getPrefs(){
        return context.getSharedPreferences("GEM_DROP_PREFS", Context.MODE_PRIVATE);
    }


    public void saveScore(int score){
        var existingScores =  getHighScoresList();
        if(isHigherThanAnyOf(score, existingScores)){
            var amendedHighScores = ScoreUtils.addToHighScores(score, getHighScoresList());
            save(amendedHighScores);
        }
    }


    private boolean isHigherThanAnyOf(int score, List<String> existingScores){
       return existingScores.stream()
                .map(Integer::parseInt)
                .anyMatch(x -> x < score);
    }


    public List<String> getOrderedHighScores(){
        return ScoreUtils.getOrderedHighScoreStrings(getHighScoresStr());
    }


    public void initHighScores(){
        var existingHighScores = getHighScoresStr();
        if(existingHighScores.isEmpty()){
            reset();
        }
    }


    public void reset(){
        save(DEFAULT_SCORES);
    }


    private void save(List<String> scoresList){
        var scoresStr = createPropStrFrom(scoresList);
        save(scoresStr);
    }


    private void save(String highScoresStr){
        getPrefs().edit()
                .putString(PrefName.HIGH_SCORES_STR.name(), highScoresStr)
                .apply();
    }


    private String getHighScoresStr(){
        return getPrefs().getString(PrefName.HIGH_SCORES_STR.name(), "");
    }


    private List<String> getHighScoresList(){
        var str = getPrefs().getString(PrefName.HIGH_SCORES_STR.name(), "");
        return Arrays.asList(str.split(","));
    }


}
