package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FreeFallState extends AbstractGameState{


    private ScheduledFuture<?> freeFallFuture;
    private final GemControls controls;
    private final int gravityInterval;
    private GemGroup gemGroup;
    private final ScheduledExecutorService executor;
    private int dropCount;

    public FreeFallState(GameStateManager gameStateManager){
        super(gameStateManager, Type.FREE_FALL);
        this.controls = gameStateManager.getControls();
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
            gameStateManager.loadState(Type.EVALUATE_GRID);
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
