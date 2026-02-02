package com.jcrawleydev.gemsdrop.game;

public class GameModel {

    public final int GRAVITY_INTERVAL = 70;
    private int dropRate = 500;
    private int dropCount = 0;
    private int dropIntervalCounter;

    public int getDropRate(){
        return dropRate;
    }

    public void setDropRate(int rate){
        this.dropRate = rate;
    }

    public void incrementDropCount(){
        dropCount++;
    }


    public int getDropCount(){
        return dropCount;
    }

    public void resetDropCount(){
        dropCount = 0;
    }


    public void updateDropInterval(){
        int minimumInterval = 120;
        int intervalDecrement = 20;
        dropIntervalCounter++;
        if(dropIntervalCounter > 9){
            dropRate = Math.max(minimumInterval, dropRate - intervalDecrement);
            dropIntervalCounter = 0;
        }
    }
}
