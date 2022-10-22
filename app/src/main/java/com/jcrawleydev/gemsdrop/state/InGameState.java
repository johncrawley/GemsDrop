package com.jcrawleydev.gemsdrop.state;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.gameState.GameStateManager;

import static com.jcrawleydev.gemsdrop.gameState.GameState.Type.BEGIN_NEW_GAME;

public class InGameState implements GameState {


    public Game game;
    //private final ActionMediator actionMediator;
    private final ClickHandler clickHandler;
    private boolean hasClicked;
    private GameStateManager gameStateManager;

    public InGameState(Game game, GameStateManager gameStateManager, ClickHandler clickHandler){
        this.game = game;
        this.gameStateManager = gameStateManager;
        this.clickHandler = clickHandler;
    }


    @Override
    public void start(){
       // actionMediator.resetVariables();
       // actionMediator.clearGemGrid();
     //   actionMediator.createAndDropGems();
        gameStateManager.loadState(BEGIN_NEW_GAME);
    }


    @Override
    public void stop(){

    }


    @Override
    public void click(int x, int y) {

        if(hasClicked){
            return;
        }
        hasClicked = true;
        clickHandler.click(x,y);
        hasClicked = false;
    }
}
