package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

public class CreateNewGemsState implements GameState {

    private final GameStateManager gameStateManager;
    private GemGroupFactory gemGroupFactory;

    public CreateNewGemsState(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
        float gemWidth = 50;
        float dropValue = 10;
        float initialY = -100;

        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .dropValue(dropValue)
                .withNumberOfGems(3)
                .withInitialPosition(5)
                .withFloorAt(1800)
                .withBorderWidth(30)
                .withGemGrid(gameStateManager.getGemGridLayer().getGemGrid())
                .build();
    }


    @Override
    public void start() {

    }


    @Override
    public void stop() {
        //do nothing
    }
}