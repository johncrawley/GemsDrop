package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.speed.SpeedController;

public class QuickDropState extends DropState{

    public QuickDropState(GameStateManager gameStateManager, SpeedController speedController){
        super(gameStateManager, speedController);
        gameStateManager.getControls().deactivate();
    }

    @Override
    void enableControlsAfterFirstDrop(){
        // do nothing
    }

}
