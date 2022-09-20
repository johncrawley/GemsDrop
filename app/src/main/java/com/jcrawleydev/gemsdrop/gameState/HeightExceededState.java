package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.HeightExceededAnimator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeightExceededState implements GameState{


    private final GameStateManager gameStateManager;
    private final ScheduledExecutorService animateEndingService;
    private ScheduledFuture<?> future;
    private final HeightExceededAnimator heightExceededAnimator;


    public HeightExceededState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        animateEndingService = Executors.newSingleThreadScheduledExecutor();
        heightExceededAnimator = new HeightExceededAnimator(gameStateManager.getGemGridLayer().getGemGrid());
    }


    @Override
    public void start() {
        heightExceededAnimator.resetLevel();
        final int updateInterval = 200;
        future = animateEndingService.schedule(this::updateGrid, updateInterval, TimeUnit.MILLISECONDS);
    }


    @Override
    public void stop() {
        future.cancel(false);
    }


    private void updateGrid(){
        if(heightExceededAnimator.haveAllLevelsBeenChanged()){
            gameStateManager.loadState(Type.GAME_OVER);
            return;
        }
        heightExceededAnimator.turnNextGridLevelGrey();
        gameStateManager.getGemGridLayer().drawIfUpdated();
    }


}
