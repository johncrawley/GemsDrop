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


    public FreeFallState(GameStateManager gameStateManager){
        super(gameStateManager, Type.FREE_FALL);
        this.controls = gameStateManager.getControls();
        this.gravityInterval = 55;
        executor = Executors.newScheduledThreadPool(3);
    }


    public void start(){
        controls.deactivate();
        gemGroup = gemGroupLayer.getGemGroup();
        freeFallFuture = executor.scheduleWithFixedDelay(this::freeFall, 0, gravityInterval, TimeUnit.MILLISECONDS);
        registerFuture(freeFallFuture);
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
        }
    }


    private void dropAndUpdateLayers(){
        drop();
        gemGroupLayer.drawIfUpdated();
    }


    void drop() {
        if (dropCounter.get() % 2 == 0) {
            if(gemGrid.addAnyFrom(gemGroup)) {
                gemGridLayer.draw();
            }
        }
        gemGroup.drop();
        gemGroupLayer.drawIfUpdated();
        dropCounter.increment();
    }



}
