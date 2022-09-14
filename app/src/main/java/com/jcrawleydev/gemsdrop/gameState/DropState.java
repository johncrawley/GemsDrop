package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DropState  implements GameState{

    private final GameStateManager gameStateManager;
    private final ScheduledExecutorService gemDropService, gemDrawService;
    private GemGroup gemGroup;
    private final GemGroupLayer gemGroupLayer;
    private final GemGridLayer gemGridLayer;
    private final GemGrid gemGrid;
    private final SpeedController speedController;
    private ScheduledFuture <?> dropFuture, drawFuture;
    private int evalCount;

    public DropState(GameStateManager gameStateManager){
            this.gameStateManager = gameStateManager;
            this.speedController = gameStateManager.getSpeedController();
            this.gemGridLayer = gameStateManager.getGemGridLayer();
            this.gemGroupLayer = gameStateManager.getGemGroupLayer();
            this.gemGrid = gemGridLayer.getGemGrid();
            gemDropService = Executors.newSingleThreadScheduledExecutor();
            gemDrawService = Executors.newSingleThreadScheduledExecutor();


    }


    @Override
    public void start() {
        evalCount = 0;
        int redrawInterval = 20;
        gemGroup = gameStateManager.getGemGroup();
        dropFuture = gemDropService.scheduleWithFixedDelay(this::drop, 0, 70, TimeUnit.MILLISECONDS);
        drawFuture = gemDrawService.scheduleWithFixedDelay(gemGroupLayer::drawIfUpdated, 0, redrawInterval, TimeUnit.MILLISECONDS);
    }


    private void drop(){
        evalCount++;
        gemGroup.dropBy();
        if(evalCount %2 == 1){
            gemGroup.decrementMiddleYPosition();
            addConnectedGemsToGrid();
        }
    }


    @Override
    public void stop() {
        dropFuture.cancel(false);
        drawFuture.cancel(false);
        gemGroupLayer.drawIfUpdated();
    }


    private void addConnectedGemsToGrid(){
        if(gemGrid.shouldAddAll(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            gameStateManager.loadState(Type.EVAL);
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridLayer.draw();
            gameStateManager.loadState(Type.FREE_FALL);
        }
    }
}
