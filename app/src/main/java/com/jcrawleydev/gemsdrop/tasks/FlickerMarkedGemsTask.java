package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.GemGridView;

//Makes gems flicker before they are deleted
public class FlickerMarkedGemsTask implements  Runnable {

    private GemGrid gemGrid;
    private GemGridView gemGridView;

    public FlickerMarkedGemsTask(GemGrid gemGrid, GemGridView gemGridView){
        this.gemGrid = gemGrid;
        this.gemGridView = gemGridView;
    }

    public void run(){
        gemGrid.flickerGemsMarkedForDeletion();
        gemGridView.draw();
    }

}
