package com.jcrawleydev.gemsdrop.game.score;

import static com.jcrawleydev.gemsdrop.game.score.ScoreUtils.createPropStrFrom;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Arrays;
import java.util.List;

public class HighScores {

    private Context context;
    private enum PrefName { HIGH_SCORES_STR}
    public static final String DEFAULT_SCORES = "30000,20000,10000,8000,5000,2000,1000,800";


    public void init(Context context){
        this.context = context;
        initHighScores();
    }


    public SharedPreferences getPrefs(){
        return context.getSharedPreferences("GEM_DROP_PREFS", Context.MODE_PRIVATE);
    }


    public void saveScore(int score){
        print(getHighScoresList());
        var amendedHighScores = ScoreUtils.addToHighScores(score, getHighScoresList());
        print(amendedHighScores);
        save(amendedHighScores);
    }


    private void print(List<String> list){
        System.out.print("^^^ list ---> ");
        list.forEach(x -> System.out.print(x + " "));
    }


    private void log(String msg){
        System.out.println("^^^ HighScores: " + msg);
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
        log("entered save()");
        print(highScores);
        var scoresStr = createPropStrFrom(highScores);
        log("save() scoreStr: " + scoresStr);
        save(scoresStr);
    }


    private void save(String highScoresStr){
        getPrefs().edit()
                .putString(PrefName.HIGH_SCORES_STR.name(), highScoresStr)
                .apply();
    }


    private String getHighScoresStr(){
        var highScoresStr =  getPrefs().getString(PrefName.HIGH_SCORES_STR.name(), "");
        log("getHighScoresStr() : retrieved scores: " + highScoresStr);
        return getPrefs().getString(PrefName.HIGH_SCORES_STR.name(), "");
    }


    private List<String> getHighScoresList(){
        var highScoresStr = getHighScoresStr();
        return Arrays.asList(highScoresStr.split(","));
    }

}
