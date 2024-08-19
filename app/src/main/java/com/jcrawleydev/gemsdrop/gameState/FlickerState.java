package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.SoundPlayer;
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
    private final SoundPlayer soundPlayer;


    public FlickerState(GameStateManager gameStateManager){
        super(gameStateManager, Type.FLICKER);
        this.gemCountTracker = new GemCountTracker(gemGridLayer.getGemGrid());
        this.flickerMarkedGemsTime = 550 + INITIAL_DELAY;
        cancelFlickerExecutor = Executors.newScheduledThreadPool(2);
        soundPlayer = gameStateManager.getSoundPlayer();
    }


    @Override
    public void start(){
        super.start();
        final int FLICKER_SPEED = 60;
        gemsFlickerFuture = cancelFlickerExecutor.scheduleWithFixedDelay(gemGridLayer::flickerGemsMarkedForDeletion, INITIAL_DELAY, FLICKER_SPEED, TimeUnit.MILLISECONDS);
        cancelFlickerExecutor.schedule(this::cancelFlickerAndDeleteMarkedGems, flickerMarkedGemsTime, TimeUnit.MILLISECONDS);
        registerFuture(gemsFlickerFuture);
    }


    private void cancelFlickerAndDeleteMarkedGems(){
        gemsFlickerFuture.cancel(false);
        gemCountTracker.startTracking();
        evaluator.deleteMarkedGemsOLD();
        int count = gemCountTracker.getDifference();
        score.addPointsFor(count);
        soundPlayer.playSound(SoundPlayer.Sound.DISAPPEAR);

        score.incMultiplier();
        scoreBoardLayer.draw();
        loadState(Type.GRID_GRAVITY);
    }


    @Override
    public void stop() {

    }

}
