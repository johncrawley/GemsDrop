package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.speed.FixedSpeedController;

public class QuickDropState extends DropState{

    public QuickDropState(GameStateManager gameStateManager){
        super(gameStateManager);
        gameStateManager.getControls().deactivate();
        this.speedController = new FixedSpeedController(100);
    }

    @Override
    void enableControlsAfterFirstDrop(){
        // do nothing
    }

    @Override
    void drop() {
        if (dropCounter.get() % 2 == 0) {
            addConnectedGemsToGrid();
            gemGroup.decrementMiddleYPosition();
            gemGroup.dropBy();
            dropCounter.increment();
        }
        gemGroup.dropBy();
        dropCounter.increment();
    }

}
