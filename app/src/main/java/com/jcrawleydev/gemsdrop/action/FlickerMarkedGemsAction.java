package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.tasks.CancelFutureTask;
import com.jcrawleydev.gemsdrop.tasks.FlickerMarkedGemsTask;
import com.jcrawleydev.gemsdrop.view.GemGridView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FlickerMarkedGemsAction {

    private ActionMediator actionManager;
    private GemGridView gemGridView;


    public FlickerMarkedGemsAction(GemGridView gemGridView, ActionMediator actionManager){
        this.gemGridView = gemGridView;
        this.actionManager = actionManager;
    }


    void start(){

        ScheduledFuture<?> gemsFlickerFuture;
        final int FLICKER_SPEED = 80;
        Runnable markedGemsFlickerTask = new FlickerMarkedGemsTask(gemGridView);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemsFlickerFuture = executor.scheduleWithFixedDelay(markedGemsFlickerTask, 0, FLICKER_SPEED, TimeUnit.MILLISECONDS);

        Runnable flickerTimeoutTask = new CancelFutureTask(gemsFlickerFuture, actionManager);
        executor.schedule(flickerTimeoutTask, 1000, TimeUnit.MILLISECONDS);
    }

}
