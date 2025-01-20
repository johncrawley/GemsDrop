package com.jcrawleydev.gemsdrop.service.game.score;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

public class GemCountTracker {

    private final GemGrid gemGrid;
    private int startCount = 0;


    public GemCountTracker(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }

    public void startTracking(){
        startCount = gemGrid.gemCount();
    }

    public int getDifference(){
        int currentDifference = startCount - gemGrid.gemCount();
        return Math.max(currentDifference, 0);
    }

}
