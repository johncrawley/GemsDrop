package com.jcrawleydev.gemsdrop.gameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DropState extends AbstractGameState{

    private final ScheduledExecutorService gemDropService, gemDrawService;
    private int evalCount;
    private ScheduledFuture<?> dropFuture, drawFuture;
    private boolean wereGemsAdded = false;

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
        wereGemsAdded = false;
        dropFuture = gemDropService.scheduleWithFixedDelay(this::drop, 0, speedController.getInterval(), TimeUnit.MILLISECONDS);
        drawFuture = gemDrawService.scheduleWithFixedDelay(gemGroupLayer::drawIfUpdated, 0, redrawInterval, TimeUnit.MILLISECONDS);
    }


    @Override
    public void stop() {
        dropFuture.cancel(false);
        drawFuture.cancel(false);
        gemGroupLayer.drawIfUpdated();
    }


    void drop() {
        if(addConnectedGemsToGrid()){
            evalCount = 0;
            return;
        }
        if (gemGroup.isQuickDropEnabled()) {
            loadState(Type.QUICK_DROP);
        }
        else {
            enableControlsAfterFirstDrop();
            gemGroup.drop();
            dropCounter.increment();
        }
    }


    boolean addConnectedGemsToGrid(){
        if (dropCounter.get() % 2 != 0) {
            return false;
        }
        if(gemGrid.shouldAddAll(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            loadState(Type.EVALUATE_GRID);
            return true;
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridLayer.draw();
            loadState(Type.FREE_FALL);
            return true;
        }
        return false;
    }


    void enableControlsAfterFirstDrop(){
        evalCount++;
        if(evalCount > 0){
            gameStateManager.getControls().reactivate();
        }
    }


    private void log(String msg){
        System.out.println("DropState: " + msg);
    }
}
