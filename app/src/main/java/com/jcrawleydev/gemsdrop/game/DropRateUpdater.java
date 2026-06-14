package com.jcrawleydev.gemsdrop.game;

public class DropRateUpdater {

    private int dropRate = 500;
    private int dropIntervalCounter;
    private int minimumInterval = 120;


    public void init(int startingInterval, int minimumInterval){
        dropRate = startingInterval;
        dropIntervalCounter = 0;
        this.minimumInterval = minimumInterval;
    }


    public void updateDropInterval(){
        int intervalDecrement = 15;
        int numberOfDropsToIncreaseSpeed = 12;
        dropIntervalCounter++;
        if(dropIntervalCounter > numberOfDropsToIncreaseSpeed){
            dropRate = Math.max(minimumInterval, dropRate - intervalDecrement);
            dropIntervalCounter = 0;
        }
    }


    public int getDropRate(){
        return dropRate;
    }


}
