package com.jcrawleydev.gemsdrop.game.score;

import static com.jcrawleydev.gemsdrop.game.score.ScoreUtils.createPropStrFrom;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class HighScores {

    private Context context;
    private enum PrefName { HIGH_SCORES_STR}
    public static final String DEFAULT_SCORES = "30000,20000,10000,8000,5000,2000,1000,800,700,500";


    public void init(Context context){
        this.context = context;
        initHighScores();
        reset();
    }


    public SharedPreferences getPrefs(){
        return context.getSharedPreferences("GEM_DROP_PREFS", Context.MODE_PRIVATE);
    }


    public void saveScore(int score){
        var amendedHighScores = ScoreUtils.addToHighScores(score, getHighScoresList());
        save(amendedHighScores);
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


    private void save(List<String> highScores){
        var scoresStr = createPropStrFrom(highScores);
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
        var highScoresStr = getHighScoresStr();
        return Arrays.asList(highScoresStr.split(","));
    }

}
