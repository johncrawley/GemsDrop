package com.jcrawleydev.gemsdrop.gameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GridGravityState extends AbstractGameState{

    private ScheduledFuture<?> gemGridGravityFuture;
    private final int gravityInterval;
    private final ScheduledExecutorService executor;

    public GridGravityState(GameStateManager gameStateManager){
        super(gameStateManager, Type.GRID_GRAVITY);
        this.gravityInterval = 50;
        executor = Executors.newScheduledThreadPool(2);
    }


    @Override
    public void start(){
        super.start();
        cancelFutures();
        gemGridGravityFuture = executor.scheduleWithFixedDelay(this::dropLooseGridGems, 0, gravityInterval, TimeUnit.MILLISECONDS);
        registerFuture(gemGridGravityFuture);
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


    private void dropLooseGridGems(){
        gemGrid.dropGems();
        gemGridLayer.draw();
        if(gemGrid.isStable()) {
            loadState(Type.EVALUATE_GRID);
        }
    }

}
