package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.HeightExceededAnimator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeightExceededState extends AbstractGameState{

    private final ScheduledExecutorService animateEndingServiceExecutor;
    private ScheduledFuture<?> future;
    private final HeightExceededAnimator heightExceededAnimator;


    public HeightExceededState(GameStateManager gameStateManager){
        super(gameStateManager, Type.HEIGHT_EXCEEDED);
        animateEndingServiceExecutor = Executors.newSingleThreadScheduledExecutor();
        heightExceededAnimator = new HeightExceededAnimator(gameStateManager.getGemGridLayer().getGemGrid());
    }


    @Override
    public void start() {
        heightExceededAnimator.resetLevel();
        final int updateInterval = 100;
        final int initialDelay = 0;
        future = animateEndingServiceExecutor.scheduleAtFixedRate(this::updateGrid, initialDelay, updateInterval, TimeUnit.MILLISECONDS);
        registerFuture(future);
    }


    @Override
    public void stop() {
        future.cancel(false);
    }


    private void updateGrid(){
        try {
            if (heightExceededAnimator.haveAllLevelsBeenChanged()) {
                loadState(Type.GAME_OVER);
                return;
            }
            heightExceededAnimator.turnNextGridLevelGrey();
            gameStateManager.getGemGridLayer().drawIfUpdated();
        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

}
