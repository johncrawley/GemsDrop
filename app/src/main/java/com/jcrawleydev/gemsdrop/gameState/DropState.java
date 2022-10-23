package com.jcrawleydev.gemsdrop.gameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DropState extends AbstractGameState{

    private final ScheduledExecutorService gemDropService, gemDrawService;
    private int evalCount;
    private ScheduledFuture<?> dropFuture, drawFuture;

    public DropState(GameStateManager gameStateManager){
        super(gameStateManager, Type.DROP);
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


    void drop() {
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
