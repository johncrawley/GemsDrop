package com.jcrawleydev.gemsdrop.service.game.score;


public class Score {

    private int currentScore = 0;
    private final int basePoints;
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
        multiplier = 1;
    }


    public int getMultiplier(){
        return multiplier;
    }


    public void addPointsFor(int numberOfGems){
        currentScore += basePoints * numberOfGems * multiplier;
        incMultiplier();
    }


}
