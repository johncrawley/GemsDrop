package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class FlickerState extends AbstractGameState{

    private final int flickerMarkedGemsTime;
    private ScheduledFuture<?> gemsFlickerFuture;
    private final ScheduledExecutorService cancelFlickerExecutor;
    private final GemCountTracker gemCountTracker;


    public FlickerState(GameStateManager gameStateManager){
        super(gameStateManager, Type.FLICKER);
        this.gemCountTracker = new GemCountTracker(gemGridLayer.getGemGrid());
        this.flickerMarkedGemsTime = 550;
        cancelFlickerExecutor = Executors.newScheduledThreadPool(2);
    }


    @Override
    public void start(){
        final int FLICKER_SPEED = 80;
        gemsFlickerFuture = cancelFlickerExecutor.scheduleWithFixedDelay(gemGridLayer::flickerGemsMarkedForDeletion, 0, FLICKER_SPEED, TimeUnit.MILLISECONDS);
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
