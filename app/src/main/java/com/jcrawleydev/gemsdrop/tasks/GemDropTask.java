package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

public class GemDropTask implements Runnable{

    private final GemGroup gemGroup;
    private final GemGrid gemGrid;
    private final GemGridLayer gemGridView;
    private final ActionMediator actionManager;

    public GemDropTask(GemGroup gemGroup, GemGridLayer gemGridView, ActionMediator actionManager){
        this.gemGroup = gemGroup;
        this.gemGridView = gemGridView;
        this.gemGrid = gemGridView.getGemGrid();
        this.actionManager = actionManager;
    }


    public void run(){
        if(gemGrid.shouldAdd(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridView.draw();
            gemGroup.setGemsInvisible();
            actionManager.onAllGemsAdded();
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridView.draw();
            actionManager.onAnyGemsAdded();
        }
        else {
            gemGroup.drop();
        }
    }


}