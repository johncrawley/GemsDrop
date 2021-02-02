package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.GemGridView;

public class GemDropTask implements Runnable{
    private GemGroup gemGroup;
    private MainActivity mainActivity;
    private GemGrid gemGrid;
    private GemGridView gemGridView;

    public GemDropTask(GemGroup gemGroup,
                       GemGrid gemGrid,
                       GemGridView gemGridView,
                       MainActivity mainActivity){
        this.gemGroup = gemGroup;
        this.mainActivity = mainActivity;
        this.gemGrid = gemGrid;
        this.gemGridView = gemGridView;
    }

    public void run(){
        if(gemGroup.getBottomPosition() <=0 || gemGrid.shouldAdd(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridView.draw();
            gemGroup.setGemsInvisible();
            mainActivity.resetDrop();
            mainActivity.cancelFuture();
            return;
        }
            gemGroup.drop();
    }



}