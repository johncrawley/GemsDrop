package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gameState.dropcounter.DropCounter;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.speed.SpeedController;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DropState  implements GameState{

    private final GameStateManager gameStateManager;
    private final ScheduledExecutorService gemDropService, gemDrawService;
    private ScheduledFuture <?> dropFuture, drawFuture;
    GemGroup gemGroup;
    private final GemGroupLayer gemGroupLayer;
    private final GemGridLayer gemGridLayer;
    private final GemGrid gemGrid;
    SpeedController speedController; //used in subclass
    private int evalCount;
    DropCounter dropCounter;

    public DropState(GameStateManager gameStateManager){
            this.gameStateManager = gameStateManager;
            this.speedController = gameStateManager.getSpeedController();
            this.gemGridLayer = gameStateManager.getGemGridLayer();
            this.gemGroupLayer = gameStateManager.getGemGroupLayer();
            this.gemGrid = gemGridLayer.getGemGrid();
            this.dropCounter = gameStateManager.getDropCounter();
            gemDropService = Executors.newSingleThreadScheduledExecutor();
            gemDrawService = Executors.newSingleThreadScheduledExecutor();
    }


    @Override
    public void start() {
        evalCount = 0;
        int redrawInterval = 20;
        gemGroup = gameStateManager.getGemGroup();
        dropFuture = gemDropService.scheduleWithFixedDelay(this::drop, 0, speedController.getInterval(), TimeUnit.MILLISECONDS);
        drawFuture = gemDrawService.scheduleWithFixedDelay(gemGroupLayer::drawIfUpdated, 0, redrawInterval, TimeUnit.MILLISECONDS);
    }


    private void log(String msg){
        System.out.println("DropState: " + msg);
    }


    void drop() {
        log("Entered drop()");
        if (dropCounter.get() % 2 == 0) {
            addConnectedGemsToGrid();
            gemGroup.decrementMiddleYPosition();
        }
        if (gemGroup.isQuickDropEnabled()) {
            gameStateManager.loadState(Type.QUICK_DROP);
        } else {
            enableControlsAfterFirstDrop();
            gemGroup.dropBy();
            dropCounter.increment();
        }
    }


    void enableControlsAfterFirstDrop(){
        evalCount++;
        if(evalCount > 0){
            gameStateManager.getControls().reactivate();
        }
    }


    @Override
    public void stop() {
        dropFuture.cancel(false);
        drawFuture.cancel(false);
        gemGroupLayer.drawIfUpdated();
    }


    void addConnectedGemsToGrid(){
        log("entered addConnectedGemsToGrid()");
        if(gemGrid.shouldAddAll(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            gameStateManager.loadState(Type.EVALUATE_GRID);
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridLayer.draw();
            gameStateManager.loadState(Type.FREE_FALL);
        }
    }
}
