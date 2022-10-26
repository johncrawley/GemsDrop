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
        this.gravityInterval = 55;
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
        dropAndUpdateLayers();
        if(gemGroup.haveAllGemsSettled()){
            freeFallFuture.cancel(false);
            loadState(Type.EVALUATE_GRID);
            return;
        }
    }


    private void dropAndUpdateLayers(){
        drop();
        gemGroupLayer.drawIfUpdated();
    }


    public void dropOLD(){
        dropCount++;
        gemGroup.dropBy();
        if(dropCount %2 == 1){
            if(gemGridLayer.getGemGrid().addAnyFrom(gemGroup)){
                gemGridLayer.draw();
            }
            gemGroup.decrementMiddleYPosition();
        }
    }


    void drop() {
        if (dropCounter.get() % 2 == 0) {
            if(gemGrid.addAnyRealFrom(gemGroup)) {
                gemGridLayer.draw();
            }
            gemGroup.decrementMiddleYPosition();
        }
        gemGroup.dropBy();
        gemGroupLayer.drawIfUpdated();
        gemGroup.decrementRealBottomPosition();
        dropCounter.increment();
    }



}
