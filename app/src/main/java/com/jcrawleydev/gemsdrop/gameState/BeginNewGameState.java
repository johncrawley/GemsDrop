package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.speed.SpeedController;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

public class BeginNewGameState implements GameState{

    private final GameStateManager gameStateManager;
    private final GemGrid gemGrid;
    private final GemGridLayer gemGridLayer;
    private final SpeedController speedController;
    private final Score score;
    private final ScoreBoardLayer scoreBoardLayer;


    public BeginNewGameState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        gemGridLayer = gameStateManager.getGemGridLayer();
        gemGrid = gameStateManager.getGemGridLayer().getGemGrid();
        speedController = gameStateManager.getSpeedController();
        scoreBoardLayer = gameStateManager.getScoreBoardLayer();
        score = scoreBoardLayer.getScore();
    }


    @Override
    public void start() {
        resetVariables();
        clearGemGrid();
        gameStateManager.loadState(Type.CREATE_NEW_GEMS);
    }


    private void resetVariables(){
        speedController.reset();
        score.clear();
        scoreBoardLayer.draw();
    }


    private void clearGemGrid(){
        gemGridLayer.clearGemGrid();
    }

    @Override
    public void stop() {
        //do nothing
    }

}