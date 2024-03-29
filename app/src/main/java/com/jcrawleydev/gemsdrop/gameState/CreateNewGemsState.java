package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

public class CreateNewGemsState extends AbstractGameState {

    GemGroupFactory gemGroupFactory;
    GemControls gemControls;

    public CreateNewGemsState(GameStateManager gameStateManager) {
        super(gameStateManager, Type.CREATE_NEW_GEMS);
        gemGroupFactory = gameStateManager.getGemGroupFactory();
        gemControls = gameStateManager.getControls();
    }


    @Override
    public void start() {
        super.start();
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        dropCounter.reset();
        score.resetMultiplier();
        gameStateManager.setGemGroup(gemGroup);
        gameStateManager.getGemGroupLayer().setGemGroup(gemGroup);
        gemControls.set(gemGroup);
        speedController.update();
        loadState(Type.DROP);
    }


    @Override
    public void stop() {
        //do nothing
    }
}