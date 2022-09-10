package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GemsFreeFallAction {

    private ScheduledFuture<?> quickDropFuture;
    private final ActionMediator actionMediator;
    private final GemGroupLayer gemGroupLayer;
    private final GemControls controls;
    private final GemGridLayer gemGridLayer;
    private final int gravityInterval;
    private GemGroup gemGroup;
    private final ScheduledExecutorService executor;
    private int dropCount;

    public GemsFreeFallAction(ActionMediator actionMediator,
                              GemGroupLayer gemGroupLayer,
                              GemControls controls,
                              GemGridLayer gemGridLayer,
                              int gravityInterval){
        this.actionMediator = actionMediator;
        this.gemGroupLayer = gemGroupLayer;
        this.controls = controls;
        this.gemGridLayer = gemGridLayer;
        this.gravityInterval = gravityInterval;
        executor = Executors.newScheduledThreadPool(3);
    }


    public void start(){
        controls.deactivate();
        gemGroup = gemGroupLayer.getGemGroup();
        dropCount = 0;
        quickDropFuture = executor.scheduleWithFixedDelay(this::quickDrop, 0, (long)(gravityInterval / 1.4f), TimeUnit.MILLISECONDS);
    }


    public void cancelFutures(){
        if(quickDropFuture != null){
            quickDropFuture.cancel(false);
        }
    }


    public void quickDrop(){
        if(gemGroup.haveAllGemsSettled()){
            quickDropFuture.cancel(false);
            actionMediator.evaluateGemsInGrid();
            return;
        }
        dropAndUpdateLayers();
    }


    private void dropAndUpdateLayers(){
        drop();
        gemGroupLayer.drawIfUpdated();
    }


    public void drop(){
        dropCount++;
        gemGroup.dropBy();
        if(dropCount %2 == 1){
            if(gemGridLayer.getGemGrid().addAnyFrom(gemGroup)){
             gemGridLayer.draw();
            }
            gemGroup.decrementMiddleYPosition();
        }
    }


}
