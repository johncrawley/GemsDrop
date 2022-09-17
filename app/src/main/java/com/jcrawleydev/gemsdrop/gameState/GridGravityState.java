package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GridGravityState implements GameState{


    private final GameStateManager gameStateManager;
    private ScheduledFuture<?> gemGridGravityFuture;
    private final GemGridLayer gemGridLayer;
    private final int gravityInterval;
    private final GemGrid gemGrid;
    private final ScheduledExecutorService executor;

    public GridGravityState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        this.gemGridLayer = gameStateManager.getGemGridLayer();
        this.gemGrid = gemGridLayer.getGemGrid();
        this.gravityInterval = 50;
        executor = Executors.newScheduledThreadPool(2);
    }


    @Override
    public void start(){
        cancelFutures();
        gemGridGravityFuture = executor.scheduleWithFixedDelay(this::gravity, 0, gravityInterval, TimeUnit.MILLISECONDS);
    }


    @Override
    public void stop(){
        gemGridGravityFuture.cancel(false);
    }


    private void cancelFutures(){
        if(gemGridGravityFuture != null){
            gemGridGravityFuture.cancel(false);
        }
    }


    private void gravity(){
        gemGrid.dropGems();
        gemGridLayer.draw();
        if(gemGrid.isStable()) {
            gameStateManager.loadState(Type.EVAL);
        }
    }

}
