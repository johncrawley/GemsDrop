package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.tasks.GemGridGravityTask;
import com.jcrawleydev.gemsdrop.view.GemGridView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GemGridGravityDropAction {

    private ScheduledFuture<?> gemGridGravityFuture;
    private GemGridView gemGridView;
    private ActionMediator actionManager;


    public GemGridGravityDropAction(ActionMediator actionManager, GemGridView gemGridView){
        this.actionManager = actionManager;
        this.gemGridView = gemGridView;
    }


    void start(){
        final int GEM_GRID_GRAVITY_INTERVAL = 100;
        Runnable gemGridGravityTask = new GemGridGravityTask(gemGridView, actionManager);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemGridGravityFuture = executor.scheduleWithFixedDelay(gemGridGravityTask, 0, GEM_GRID_GRAVITY_INTERVAL, TimeUnit.MILLISECONDS);
    }


    void stop(){
        gemGridGravityFuture.cancel(false);
        actionManager.evaluateGemsInGrid();
    }
}
