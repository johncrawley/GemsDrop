package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
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
    private final ScheduledExecutorService executor;
    private final Evaluator evaluator;


    public FlickerState(GameStateManager gameStateManager, Evaluator evaluator){
        this.gameStateManager = gameStateManager;
        this.gemGridLayer = gameStateManager.getGemGridLayer();
        this.evaluator = evaluator;
        this.flickerMarkedGemsTime = 200;
        executor = Executors.newScheduledThreadPool(2);
    }


    @Override
    public void start(){
        final int FLICKER_SPEED = 80;
        gemsFlickerFuture = executor.scheduleWithFixedDelay(gemGridLayer::flickerGemsMarkedForDeletion, 0, FLICKER_SPEED, TimeUnit.MILLISECONDS);
        executor.schedule(this::cancelFlickerAndDeleteMarkedGems, flickerMarkedGemsTime, TimeUnit.MILLISECONDS);
    }


    private void cancelFlickerAndDeleteMarkedGems(){
        gemsFlickerFuture.cancel(false);
        evaluator.deleteMarkedGems();
        gameStateManager.loadState(Type.GRID_GRAVITY);
    }


    @Override
    public void stop() {

    }

}
