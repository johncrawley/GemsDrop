package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GemDropQuickAction {

    private ScheduledFuture<?> quickDropFuture;
    private final ActionMediator actionMediator;
    private final GemGroupLayer gemGroupLayer;
    private final GemControls controls;
    private final GemGridLayer gemGridLayer;
    private final int gravityInterval;
    private GemGroup gemGroup;
    private GemGrid gemGrid;
    private final ScheduledExecutorService executor;

    public GemDropQuickAction(ActionMediator actionMediator,
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
        gemGrid = gemGridLayer.getGemGrid();
        gemGroup = gemGroupLayer.getGemGroup();
        gemGroupLayer.getGemGroup().enableQuickDrop();
        controls.deactivate();
        quickDropFuture = executor.scheduleWithFixedDelay(this::quickDrop, 0, gravityInterval, TimeUnit.MILLISECONDS);
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


    public void quickDrop(){
        if(gemGroup.haveAllGemsSettled()){
            quickDropFuture.cancel(false);
            actionMediator.evaluateGemsInGrid();
            return;
        }
        dropAndUpdateLayers(gemGroup);
    }


    private void dropAndUpdateLayers(GemGroup gemGroup){
        gemGroup.drop();
        gemGroupLayer.drawIfUpdated();
        if(gemGridLayer.getGemGrid().addAnyFrom(gemGroup)){
            gemGridLayer.draw();
        }
    }





}
