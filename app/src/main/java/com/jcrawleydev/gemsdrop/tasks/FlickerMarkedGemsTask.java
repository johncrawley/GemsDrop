package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

//Makes gems flicker before they are deleted
public class FlickerMarkedGemsTask implements  Runnable {

    private final GemGrid gemGrid;
    private final GemGridLayer gemGridView;

    public FlickerMarkedGemsTask(GemGridLayer gemGridView){
        this.gemGridView = gemGridView;
        this.gemGrid = gemGridView.getGemGrid();
    }

    public void run(){
        gemGrid.flickerGemsMarkedForDeletion();
        gemGridView.draw();
    }

}
