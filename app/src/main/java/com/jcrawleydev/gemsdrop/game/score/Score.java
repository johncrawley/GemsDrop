package com.jcrawleydev.gemsdrop.game.score;


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


    public String getText(){
        return String.valueOf(currentScore);
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
        int oldScore = currentScore;
        currentScore += basePoints * numberOfGems * multiplier;
        log("addPointsFor() old score: " + oldScore + ", current score after addition: " + currentScore);
        incMultiplier();
    }

    private void log(String msg){
        System.out.print("^^^ Score: "  + msg);
    }

}
