package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.GemGridView;


public class QuickDropTask implements Runnable{

    private MainActivity mainActivity;
    private GemGroup gemGroup;
    private GemGrid gemGrid;
    private GemGridView gemGridView;

    public QuickDropTask(MainActivity mainActivity, GemGroup gemGroup, GemGrid gemGrid, GemGridView gemGridView) {
        this.mainActivity = mainActivity;
        this.gemGroup = gemGroup;
        this.gemGrid = gemGrid;
        this.gemGridView = gemGridView;

    }


    public void run(){
        if(gemGroup.haveAllGemsSettled() || gemGrid.gemCount() > 100){
            mainActivity.finishQuickDrop();
            return;
        }
        gemGroup.drop();
        if(gemGrid.addAnyFrom(gemGroup)){
            gemGridView.draw();
        }
    }

}
