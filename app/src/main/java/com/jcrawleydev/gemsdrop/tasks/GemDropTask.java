package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.TaskProfiler;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadLocalRandom;

public class GemDropTask implements Runnable{
    private GemGroup gemGroup;
    private GemGroupView gemGroupView;
    private MainActivity mainActivity;
    private TaskProfiler taskProfiler;
    private GemGrid gemGrid;
    private GemGridView gemGridView;

    public GemDropTask(GemGroup gemGroup,
                       GemGrid gemGrid,
                       GemGroupView gemGroupView,
                       GemGridView gemGridView,
                       MainActivity mainActivity,
                       TaskProfiler taskProfiler){
        this.gemGroup = gemGroup;
        this.gemGroupView = gemGroupView;
        this.mainActivity = mainActivity;
        this.taskProfiler = taskProfiler;
        this.gemGrid = gemGrid;
        this.gemGridView = gemGridView;

    }

    public void run(){
        //taskProfiler.start();

        if(gemGroup.getBottomPosition() <=0 || gemGrid.shouldAdd(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGroup.setGemsInvisible();
            gemGridView.draw();
            gemGroupView.updateAndDraw();
            mainActivity.resetDrop();
            mainActivity.cancelFuture();
            //taskProfiler.end();
            //taskProfiler.print();
            return;
        }
            gemGroup.drop();
            gemGroupView.updateAndDraw();
            //taskProfiler.end();
    }


}