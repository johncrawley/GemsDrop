package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;


public class QuickDropTask implements Runnable{

    private ActionMediator actionManager;
    private GemGroup gemGroup;
    private GemGrid gemGrid;
    private GemGridView gemGridView;
    private GemGroupView gemGroupView;

    public QuickDropTask(ActionMediator actionManager, GemGroupView gemGroupView, GemGridView gemGridView) {
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
