package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
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
        if(gemGrid.shouldAdd(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridView.draw();
            gemGroup.setGemsInvisible();
            mainActivity.cancelDropAndAnimateFutures();
            mainActivity.evaluateStep();
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridView.draw();
            mainActivity.quickDropRemainingGems();
            mainActivity.cancelDropAndAnimateFutures();
        }
        else {
            gemGroup.drop();
        }
    }


}