package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.tasks.QuickDropTask;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class QuickDropGemsAction {

    private ScheduledFuture<?> quickDropFuture;
    private ActionMediator actionManager;
    private GemGroupLayer gemGroupView;
    private GemControls controls;
    private GemGridLayer gemGridView;


    public QuickDropGemsAction(ActionMediator actionManager, GemGroupLayer gemGroupView, GemControls controls, GemGridLayer gemGridView){
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
