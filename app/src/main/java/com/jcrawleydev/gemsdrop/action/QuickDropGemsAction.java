package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.tasks.QuickDropTask;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class QuickDropGemsAction {

    private ScheduledFuture<?> quickDropFuture;
    private ActionMediator actionManager;
    private GemGroupView gemGroupView;
    private GemControls controls;
    private GemGridView gemGridView;


    public QuickDropGemsAction(ActionMediator actionManager, GemGroupView gemGroupView, GemControls controls, GemGridView gemGridView){
        this.actionManager = actionManager;
        this.gemGroupView = gemGroupView;
        this.controls = controls;
        this.gemGridView = gemGridView;
    }


    public void start(){
        int quickDropAnimationInterval = 70;
        gemGroupView.getGemGroup().enableQuickDrop();
        QuickDropTask quickDropTask = new QuickDropTask(actionManager, gemGroupView, gemGridView);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        controls.deactivate();
        quickDropFuture = executor.scheduleWithFixedDelay(quickDropTask, 0, quickDropAnimationInterval, TimeUnit.MILLISECONDS);
    }


    void stop(){
            quickDropFuture.cancel(false);
            actionManager.evaluateGemsInGrid();
    }
}
