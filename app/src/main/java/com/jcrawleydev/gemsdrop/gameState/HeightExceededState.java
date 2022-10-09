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
        log("Entered stop()");
        future.cancel(false);
    }


    private void updateGrid(){
        log("Entered updateGrid()");
        try {
            if (heightExceededAnimator.haveAllLevelsBeenChanged()) {
                log("HeightExceededAnimator: all levels have been changed, loading game over state");
                gameStateManager.loadState(Type.GAME_OVER);
                return;
            }
            heightExceededAnimator.turnNextGridLevelGrey();
            log("updateGrid() heightExceededAnimator turned next grid level grey");
            gameStateManager.getGemGridLayer().drawIfUpdated();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }


    private void log(String msg){
        System.out.println("HeightExceededState: " +  msg);
    }

}
