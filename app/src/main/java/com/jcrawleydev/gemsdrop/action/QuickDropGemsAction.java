package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class QuickDropGemsAction {

    private ScheduledFuture<?> quickDropFuture;
    private final ActionMediator actionMediator;
    private final GemGroupLayer gemGroupLayer;
    private final GemControls controls;
    private final GemGridLayer gemGridLayer;
    private final int gravityInterval;


    public QuickDropGemsAction(ActionMediator actionMediator,
                               GemGroupLayer gemGroupLayer,
                               GemControls controls,
                               GemGridLayer gemGridLayer,
                               int gravityInterval){
        this.actionMediator = actionMediator;
        this.gemGroupLayer = gemGroupLayer;
        this.controls = controls;
        this.gemGridLayer = gemGridLayer;
        this.gravityInterval = gravityInterval;
    }


    public void start(){
        gemGroupLayer.getGemGroup().enableQuickDrop();
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        controls.deactivate();
        quickDropFuture = executor.scheduleWithFixedDelay(this::quickDrop, 0, gravityInterval, TimeUnit.MILLISECONDS);
    }


    public void quickDrop(){
        GemGroup gemGroup = gemGroupLayer.getGemGroup();
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
