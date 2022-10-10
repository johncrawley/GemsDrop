package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gameState.dropcounter.DropCounter;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.speed.SpeedController;

public class CreateNewGemsState implements GameState {

    private final GameStateManager gameStateManager;
    private final GemGroupFactory gemGroupFactory;
    private final DropCounter dropCounter;
    private final GemControls gemControls;
    private final SpeedController speedController;
    private final Score score;


    public CreateNewGemsState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        gemGroupFactory = gameStateManager.getGemGroupFactory();
        dropCounter = gameStateManager.getDropCounter();
        gemControls = gameStateManager.getControls();
        speedController = gameStateManager.getSpeedController();
        score = gameStateManager.getScoreBoardLayer().getScore();
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