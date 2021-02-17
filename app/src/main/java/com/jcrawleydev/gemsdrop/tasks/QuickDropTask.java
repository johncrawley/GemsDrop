package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;


public class QuickDropTask implements Runnable{

    private MainActivity mainActivity;
    private GemGroup gemGroup;
    private GemGrid gemGrid;
    private GemGridView gemGridView;
    private GemGroupView gemGroupView;

    public QuickDropTask(MainActivity mainActivity, GemGroup gemGroup, GemGroupView gemGroupView, GemGrid gemGrid, GemGridView gemGridView) {
        this.mainActivity = mainActivity;
        this.gemGroup = gemGroup;
        this.gemGrid = gemGrid;
        this.gemGridView = gemGridView;
        this.gemGroupView = gemGroupView;

    }


    public void run(){
        if(gemGroup.haveAllGemsSettled() || gemGrid.gemCount() > 100){
            mainActivity.finishQuickDrop();
            return;
        }
        gemGroup.drop();
        gemGroupView.drawIfUpdated();
        if(gemGrid.addAnyFrom(gemGroup)){
            gemGridView.draw();
        }
    }

}
