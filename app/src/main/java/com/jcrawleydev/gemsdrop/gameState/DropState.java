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
        dropFuture = gemDropService.scheduleWithFixedDelay(this::dropReal, 0, speedController.getInterval(), TimeUnit.MILLISECONDS);
        drawFuture = gemDrawService.scheduleWithFixedDelay(gemGroupLayer::drawIfUpdated, 0, redrawInterval, TimeUnit.MILLISECONDS);
    }


    @Override
    public void stop() {
        dropFuture.cancel(false);
        drawFuture.cancel(false);
        gemGroupLayer.drawIfUpdated();
    }


    void drop() {
        log("Entered drop() dropCounter: "+  dropCounter.get());
        if (dropCounter.get() % 2 == 0) {
            addConnectedGemsToGrid();
            gemGroup.decrementMiddleYPosition();
        }
        if(wereGemsAdded){
            evalCount = 0;
            return;
        }
        if (gemGroup.isQuickDropEnabled()) {
            loadState(Type.QUICK_DROP);
        }
        else {
            enableControlsAfterFirstDrop();
            gemGroup.dropBy();
            dropCounter.increment();
        }
    }


    void dropReal() {
        log("Entered dropReal() dropCounter: "+  dropCounter.get());
        if (dropCounter.get() % 2 == 0) {
            addConnectedGemsToGridReal();
            gemGroup.decrementMiddleYPosition();
        }
        if(wereGemsAdded){
            evalCount = 0;
            return;
        }
        if (gemGroup.isQuickDropEnabled()) {
            loadState(Type.QUICK_DROP);
        }
        else {
            log("dropReal() else clause, about to enableControls");
            enableControlsAfterFirstDrop();
            gemGroup.dropBy();
            gemGroup.decrementRealBottomPosition();
            dropCounter.increment();
        }
    }


    void addConnectedGemsToGridReal(){
        if(gemGrid.shouldAddAllReal(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            wereGemsAdded = true;
            loadState(Type.EVALUATE_GRID);
        }
        else if(gemGrid.addAnyRealFrom(gemGroup)){
            gemGridLayer.draw();
            wereGemsAdded = true;
            loadState(Type.FREE_FALL);
        }
    }


    void enableControlsAfterFirstDrop(){
        evalCount++;
        if(evalCount > 0){
            gameStateManager.getControls().reactivate();
        }
    }


    void addConnectedGemsToGrid(){
        if(gemGrid.shouldAddAll(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            wereGemsAdded = true;
            loadState(Type.EVALUATE_GRID);
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridLayer.draw();
            wereGemsAdded = true;
            loadState(Type.FREE_FALL);
        }
    }


    private void log(String msg){
        System.out.println("DropState: " + msg);
    }
}
