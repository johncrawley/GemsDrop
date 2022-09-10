package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GemGridGravityDropAction {

    private ScheduledFuture<?> gemGridGravityFuture;
    private final GemGridLayer gemGridLayer;
    private final ActionMediator actionMediator;
    private final int gravityInterval;
    private final int distanceFactor;
    private final GemGrid gemGrid;

    public GemGridGravityDropAction(ActionMediator actionMediator, GemGridLayer gemGridLayer, int gravityInterval, int distanceFactor){
        this.actionMediator = actionMediator;
        this.gemGridLayer = gemGridLayer;
        this.gemGrid = gemGridLayer.getGemGrid();
        this.gravityInterval = gravityInterval;
        this.distanceFactor = distanceFactor;
    }


    void start(){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemGridGravityFuture = executor.scheduleWithFixedDelay(this::gravity, 0, gravityInterval / distanceFactor, TimeUnit.MILLISECONDS);
    }

    public void cancelFutures(){
        if(gemGridGravityFuture != null){
            gemGridGravityFuture.cancel(false);
        }
    }


    void stop(){
        gemGridGravityFuture.cancel(false);
        actionMediator.evaluateGemsInGrid();
    }


    private void gravity(){
        gemGrid.dropGems();
        if(!gemGrid.isStable()) {
            gemGridLayer.draw();
        }
        else{
            actionMediator.stopGridGravity();
        }
    }

}
