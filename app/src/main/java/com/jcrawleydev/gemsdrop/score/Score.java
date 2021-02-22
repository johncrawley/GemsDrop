package com.jcrawleydev.gemsdrop.score;

public class Score {

    private int currentScore = 0;
    private int baseScore;


    public Score(int baseScore){
        this.baseScore = baseScore;
    }


    public int get(){
        return currentScore;
    }

    public void clear(){
        currentScore = 0;
    }

    public void addPointsFor(int numberOfGems, int multiplier){
        currentScore += baseScore * numberOfGems * multiplier;
    }




}
