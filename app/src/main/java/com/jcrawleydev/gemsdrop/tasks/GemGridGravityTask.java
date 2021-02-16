package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.GemGridView;

// runs after gems have beeen deleted
public class GemGridGravityTask implements Runnable {

    private GemGrid gemGrid;
    private GemGridView gemGridView;
    private MainActivity mainActivity;

    public GemGridGravityTask(GemGrid gemGrid, GemGridView gemGridView, MainActivity mainActivity){
        this.gemGrid = gemGrid;
        this.gemGridView = gemGridView;
        this.mainActivity = mainActivity;
    }

    public void run(){
        gemGrid.dropGems();
        if(!gemGrid.isStable()) {
            gemGridView.draw();
        }
         else{
             mainActivity.stopGravity();
        }
    }

}
