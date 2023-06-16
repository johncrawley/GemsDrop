package com.jcrawleydev.gemsdrop.gameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class DropState extends AbstractGameState{

    private final ScheduledExecutorService gemDropService, gemDrawService;
    private int evalCount;
    private ScheduledFuture<?> dropFuture, drawFuture;
    private final AtomicBoolean hasBeenProcessed;


    public DropState(GameStateManager gameStateManager){
        super(gameStateManager, Type.DROP);
        hasBeenProcessed = new AtomicBoolean(false);
        gemDropService = Executors.newSingleThreadScheduledExecutor();
        gemDrawService = Executors.newSingleThreadScheduledExecutor();
    }


    @Override
    public void start() {
        super.start();
        evalCount = 0;
        hasBeenProcessed.set(false);
        int redrawInterval = 20;
        gemGroup = gameStateManager.getGemGroup();
        dropFuture = gemDropService.scheduleWithFixedDelay(this::drop, 0, speedController.getInterval(), TimeUnit.MILLISECONDS);
        drawFuture = gemDrawService.scheduleWithFixedDelay(this::addConnectedGemsAndUpdateAnimation, 0, redrawInterval, TimeUnit.MILLISECONDS);
        registerFuture(dropFuture);
        registerFuture(drawFuture);
    }


    @Override
    public void stop() {
        dropFuture.cancel(false);
        drawFuture.cancel(false);
        gemGroupLayer.drawIfUpdated();
    }


    void drop() {
        if (gemGroup.isQuickDropEnabled()) {
            loadState(Type.QUICK_DROP);
        }
        else {
            enableControlsAfterFirstDrop();
            gemGroup.drop();
            dropCounter.increment();
        }
    }


    void addConnectedGemsAndUpdateAnimation(){
        if(hasBeenProcessed.get()){
            return;
        }
        if(gemGroup.wasUpdated()) {
            if (addConnectedGemsToGrid()) {
                evalCount = 0;
            }
        }
        gemGroupLayer.drawIfUpdated();
    }


    boolean addConnectedGemsToGrid(){
        if (isCurrentDropOnAHalfStep()) {
            return false;
        }
        if(gemGrid.shouldAddAll(gemGroup)) {
            hasBeenProcessed.set(true);
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            loadState(Type.EVALUATE_GRID);
            return true;
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            hasBeenProcessed.set(true);
            gemGridLayer.draw();
            loadState(Type.FREE_FALL);
            return true;
        }
        return false;
    }


    void enableControlsAfterFirstDrop(){
        evalCount++;
        if(evalCount ==2){
            gameStateManager.getControls().reactivate();
        }
    }


    boolean isCurrentDropOnAHalfStep(){
        return dropCounter.get() % 2 != 0;
    }

}
