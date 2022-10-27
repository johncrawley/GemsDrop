package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.speed.FixedSpeedController;

public class QuickDropState extends DropState{

    public QuickDropState(GameStateManager gameStateManager){
        super(gameStateManager);
        stateType = Type.QUICK_DROP;
        gameStateManager.getControls().deactivate();
        this.speedController = new FixedSpeedController(40);
    }


    @Override
    void enableControlsAfterFirstDrop(){
        // do nothing
    }


    @Override
    void dropReal() {
        if (dropCounter.get() % 2 == 0) {
            addConnectedGemsToGridReal();
        }
        gemGroup.dropBy();
        gemGroup.decrementRealBottomPosition();
        dropCounter.increment();
    }

}
