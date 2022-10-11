package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

public class CreateNewGemsState extends AbstractGameState {

    GemGroupFactory gemGroupFactory;
    GemControls gemControls;

    public CreateNewGemsState(GameStateManager gameStateManager) {
        super(gameStateManager);
        gemGroupFactory = gameStateManager.getGemGroupFactory();
        gemControls = gameStateManager.getControls();
    }


    @Override
    public void start() {
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        dropCounter.reset();
        score.resetMultiplier();
        gameStateManager.setGemGroup(gemGroup);
        gameStateManager.getGemGroupLayer().setGemGroup(gemGroup);
        gameStateManager.loadState(Type.DROP);
        gemControls.set(gemGroup);
        speedController.update();
    }


    @Override
    public void stop() {
        //do nothing
    }
}