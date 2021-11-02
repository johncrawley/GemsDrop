package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FlickerMarkedGemsAction {

    private final ActionMediator actionManager;
    private final GemGridLayer gemGridLayer;
    private final int flickerMarkedGemsTime;
    private ScheduledFuture<?> gemsFlickerFuture;


    public FlickerMarkedGemsAction(GemGridLayer gemGridLayer, ActionMediator actionManager, int flickerMarkedGemsTime){
        this.gemGridLayer = gemGridLayer;
        this.actionManager = actionManager;
        this.flickerMarkedGemsTime = flickerMarkedGemsTime;
    }


    void start(){
        final int FLICKER_SPEED = 80;
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemsFlickerFuture = executor.scheduleWithFixedDelay(gemGridLayer::flickedGemsMarkedForDeletion, 0, FLICKER_SPEED, TimeUnit.MILLISECONDS);
        executor.schedule(this::cancelFlickerAndDeleteMarkedGems, flickerMarkedGemsTime, TimeUnit.MILLISECONDS);
    }


    private void cancelFlickerAndDeleteMarkedGems(){
        gemsFlickerFuture.cancel(false);
        actionManager.deleteMarkedGems();
    }

}
