package com.jcrawleydev.gemsdrop.gameState;

import com.jcrawleydev.gemsdrop.gameState.dropcounter.DropCounter;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.speed.SpeedController;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public abstract class AbstractGameState implements GameState{


    final GameStateManager gameStateManager;
    GemGroup gemGroup;
    final GemGroupLayer gemGroupLayer;
    SpeedController speedController;
    final GemGridLayer gemGridLayer;
    final GemGrid gemGrid;
    DropCounter dropCounter;
    final Evaluator evaluator;
    final ScoreBoardLayer scoreBoardLayer;
    final Score score;
    GameState.Type stateType;

    public AbstractGameState(GameStateManager gameStateManager, GameState.Type stateType){
        this.gameStateManager = gameStateManager;
        this.stateType = stateType;
        this.speedController = gameStateManager.getSpeedController();
        this.gemGridLayer = gameStateManager.getGemGridLayer();
        this.gemGroupLayer = gameStateManager.getGemGroupLayer();
        this.evaluator = gameStateManager.getEvaluator();
        this.gemGrid = gemGridLayer.getGemGrid();
        this.dropCounter = gameStateManager.getDropCounter();
        this.scoreBoardLayer = gameStateManager.getScoreBoardLayer();
        this.score = scoreBoardLayer.getScore();
    }

    public GameState.Type getStateType(){
        return stateType;
    }


    void loadState(GameState.Type gameStateType){
        gameStateManager.loadState(gameStateType, this.stateType);
    }
}
