package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.HeightExceededAnimator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class HeightExceededState implements GameState{


    private final GameStateManager gameStateManager;
    private final ScheduledExecutorService animateEndingService;
    private ScheduledFuture<?> future;
    private HeightExceededAnimator heightExceededAnimator;

    public HeightExceededState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        animateEndingService = Executors.newSingleThreadScheduledExecutor();
        heightExceededAnimator = new HeightExceededAnimator(gameStateManager.getGemGridLayer().getGemGrid());
    }

    @Override
    public void start() {
      //  future = animateEndingService.schedule(heightExceededAnimator.turnLevelGrey());
    }

    @Override
    public void stop() {

    }


}
