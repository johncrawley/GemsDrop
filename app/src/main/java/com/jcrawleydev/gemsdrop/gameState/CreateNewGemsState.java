package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gameState.dropcounter.DropCounter;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

public class CreateNewGemsState implements GameState {

    private final GameStateManager gameStateManager;
    private final GemGroupFactory gemGroupFactory;
    private final DropCounter dropCounter;

    public CreateNewGemsState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        gemGroupFactory = gameStateManager.getGemGroupFactory();
        dropCounter = gameStateManager.getDropCounter();
    }


    @Override
    public void start() {
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        dropCounter.reset();
        gameStateManager.setGemGroup(gemGroup);
        gameStateManager.getGemGroupLayer().setGemGroup(gemGroup);
        gameStateManager.loadState(Type.DROP);
    }


    @Override
    public void stop() {
        //do nothing
    }
}