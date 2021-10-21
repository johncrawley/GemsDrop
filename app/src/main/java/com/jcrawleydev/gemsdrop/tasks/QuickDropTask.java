package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;


public class QuickDropTask implements Runnable{

    private final ActionMediator actionManager;
    private final GemGroup gemGroup;
    private final GemGrid gemGrid;
    private final GemGridLayer gemGridView;
    private final GemGroupLayer gemGroupView;

    public QuickDropTask(ActionMediator actionManager, GemGroupLayer gemGroupView, GemGridLayer gemGridView) {
        this.actionManager = actionManager;
        this.gemGroupView = gemGroupView;
        this.gemGridView = gemGridView;
        this.gemGroup = gemGroupView.getGemGroup();
        this.gemGrid = gemGridView.getGemGrid();
    }


    public void run(){
        if(gemGroup.haveAllGemsSettled() || gemGrid.gemCount() > 100){
            actionManager.finishQuickDrop();
            return;
        }
        gemGroup.drop();
        gemGroupView.drawIfUpdated();
        if(gemGrid.addAnyFrom(gemGroup)){
            gemGridView.draw();
        }
    }

}
