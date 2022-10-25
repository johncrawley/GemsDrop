package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.score.GemCountTracker;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FlickerState extends AbstractGameState{

    private final int flickerMarkedGemsTime;
    private ScheduledFuture<?> gemsFlickerFuture;
    private final ScheduledExecutorService cancelFlickerExecutor;
    private final GemCountTracker gemCountTracker;
    private final int INITIAL_DELAY = 130;


    public FlickerState(GameStateManager gameStateManager){
        super(gameStateManager, Type.FLICKER);
        this.gemCountTracker = new GemCountTracker(gemGridLayer.getGemGrid());
        this.flickerMarkedGemsTime = 550 + INITIAL_DELAY;
        cancelFlickerExecutor = Executors.newScheduledThreadPool(2);
    }


    @Override
    public void start(){
        final int FLICKER_SPEED = 60;
        gemsFlickerFuture = cancelFlickerExecutor.scheduleWithFixedDelay(gemGridLayer::flickerGemsMarkedForDeletion, INITIAL_DELAY, FLICKER_SPEED, TimeUnit.MILLISECONDS);
        cancelFlickerExecutor.schedule(this::cancelFlickerAndDeleteMarkedGems, flickerMarkedGemsTime, TimeUnit.MILLISECONDS);
    }


    private void cancelFlickerAndDeleteMarkedGems(){
        gemsFlickerFuture.cancel(false);
        gemCountTracker.startTracking();
        evaluator.deleteMarkedGems();
        int count = gemCountTracker.getDifference();
        score.addPointsFor(count);
       // soundPlayer.playGemDisappearSound(score.getMultiplier());
        score.incMultiplier();
        scoreBoardLayer.draw();
        loadState(Type.GRID_GRAVITY);
    }


    @Override
    public void stop() {

    }

}
