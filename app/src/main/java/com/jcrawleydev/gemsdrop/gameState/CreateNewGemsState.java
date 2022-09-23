package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

public class CreateNewGemsState implements GameState {

    private final GameStateManager gameStateManager;
    private GemGroupFactory gemGroupFactory;

    public CreateNewGemsState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        gemGroupFactory = gameStateManager.getGemGroupFactory();
    }


    @Override
    public void start() {

        gameStateManager.setGemGroup(gemGroupFactory.createGemGroup());

    }


    @Override
    public void stop() {
        //do nothing
    }
}