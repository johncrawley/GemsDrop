package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.tasks.GemGridGravityTask;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GemGridGravityDropAction {

    private ScheduledFuture<?> gemGridGravityFuture;
    private final GemGridLayer gemGridView;
    private final ActionMediator actionManager;
    private final int gravityInterval;
    private final int distanceFactor;

    public GemGridGravityDropAction(ActionMediator actionManager, GemGridLayer gemGridView, int gravityInterval, int distanceFactor){
        this.actionManager = actionManager;
        this.gemGridView = gemGridView;
        this.gravityInterval = gravityInterval;
        this.distanceFactor = distanceFactor;
    }


    void start(){
        Runnable gemGridGravityTask = new GemGridGravityTask(gemGridView, actionManager);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemGridGravityFuture = executor.scheduleWithFixedDelay(gemGridGravityTask, 0, gravityInterval/ distanceFactor, TimeUnit.MILLISECONDS);
    }


    void stop(){
        gemGridGravityFuture.cancel(false);
        actionManager.evaluateGemsInGrid();
    }
}
