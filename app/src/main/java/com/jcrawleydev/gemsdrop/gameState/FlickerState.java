package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FlickerState implements GameState{

    private final GameStateManager gameStateManager;
    private final GemGridLayer gemGridLayer;
    private final int flickerMarkedGemsTime;
    private ScheduledFuture<?> gemsFlickerFuture;
    ScheduledExecutorService executor;


    public FlickerState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        this.gemGridLayer = gameStateManager.getGemGridLayer();
        this.flickerMarkedGemsTime = 200;
        executor = Executors.newScheduledThreadPool(1);
    }


    @Override
    public void start(){
        final int FLICKER_SPEED = 80;
        gemsFlickerFuture = executor.scheduleWithFixedDelay(gemGridLayer::flickerGemsMarkedForDeletion, 0, FLICKER_SPEED, TimeUnit.MILLISECONDS);
        executor.schedule(this::cancelFlickerAndDeleteMarkedGems, flickerMarkedGemsTime, TimeUnit.MILLISECONDS);
    }


    private void cancelFlickerAndDeleteMarkedGems(){
        gemsFlickerFuture.cancel(false);
        gameStateManager.loadState(Type.GRID_GRAVITY);
    }



    @Override
    public void stop() {

    }

}
