package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.GemGridView;

// runs after gems have beeen deleted
public class GemGridGravityTask implements Runnable {

    private GemGrid gemGrid;
    private GemGridView gemGridView;
    private ActionMediator actionManager;


    public GemGridGravityTask(GemGridView gemGridView, ActionMediator actionManager){
        this.gemGridView = gemGridView;
        this.gemGrid = gemGridView.getGemGrid();
        this.actionManager = actionManager;
    }


    public void run(){
        gemGrid.dropGems();
        if(!gemGrid.isStable()) {
            gemGridView.draw();
        }
         else{
             actionManager.stopGridGravity();
        }
    }

}
