package com.jcrawleydev.gemsdrop.score;

public class Score {

    private int currentScore = 0;
    private int basePoints;
    private final int MULTIPLIER_INITIAL_VALUE = 1;
    private int multiplier;

    public Score(int basePoints){
        this.basePoints = basePoints;
        resetMultiplier();
    }


    public int get(){
        return currentScore;
    }

    public void clear(){
        currentScore = 0;
    }

    public void incMultiplier(){
        multiplier++;
    }

    public void resetMultiplier(){
        multiplier = MULTIPLIER_INITIAL_VALUE;
    }

    public void addPointsFor(int numberOfGems){
        currentScore += basePoints * numberOfGems * multiplier;
    }




}
