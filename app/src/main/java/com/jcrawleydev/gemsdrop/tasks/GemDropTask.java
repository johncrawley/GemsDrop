package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.TaskProfiler;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.ScheduledFuture;

public class GemDropTask implements Runnable{
    private GemGroup gemGroup;
    private TransparentView transparentView;
    private MainActivity mainActivity;
    private TaskProfiler taskProfiler;

    public GemDropTask(GemGroup gemGroup, TransparentView transparentView, MainActivity mainActivity, TaskProfiler taskProfiler){
        this.gemGroup = gemGroup;
        this.transparentView = transparentView;
        this.mainActivity = mainActivity;
        this.taskProfiler = taskProfiler;

    }

    public void run(){
        taskProfiler.start();
        if(gemGroup.getY() > 2000) {
            mainActivity.resetDrop();
            mainActivity.cancelFuture();
            taskProfiler.end();
            taskProfiler.print();
            return;
        }
            gemGroup.drop();
            transparentView.updateAndDraw();
            transparentView.invalidate();
            taskProfiler.end();
    }


}