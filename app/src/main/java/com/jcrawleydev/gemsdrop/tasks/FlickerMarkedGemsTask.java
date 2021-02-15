package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

public class FlickerMarkedGemsTask implements  Runnable {

    private GemGrid gemGrid;

    public FlickerMarkedGemsTask(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }

    public void run(){
        gemGrid.flickerGemsMarkedForDeletion();
    }

}
