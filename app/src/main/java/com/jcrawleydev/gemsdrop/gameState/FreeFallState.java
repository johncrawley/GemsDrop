package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FreeFallState implements GameState{


    private final GameStateManager gameStateManager;
    private ScheduledFuture<?> freeFallFuture;
    private final GemGroupLayer gemGroupLayer;
    private final GemControls controls;
    private final GemGridLayer gemGridLayer;
    private final int gravityInterval;
    private GemGroup gemGroup;
    private final ScheduledExecutorService executor;
    private int dropCount;

    public FreeFallState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;

        this.gemGroupLayer = gameStateManager.getGemGroupLayer();
        this.controls = gameStateManager.getControls();
        this.gemGridLayer = gameStateManager.getGemGridLayer();
        this.gravityInterval = 50;
        executor = Executors.newScheduledThreadPool(3);
    }


    public void start(){
        controls.deactivate();
        gemGroup = gemGroupLayer.getGemGroup();
        dropCount = 0;
        freeFallFuture = executor.scheduleWithFixedDelay(this::freeFall, 0, gravityInterval, TimeUnit.MILLISECONDS);
    }


    public void stop(){
        if(freeFallFuture != null){
            freeFallFuture.cancel(false);
        }
    }


    public void freeFall(){
        if(gemGroup.haveAllGemsSettled()){
            freeFallFuture.cancel(false);
            gameStateManager.loadState(Type.EVAL);
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
