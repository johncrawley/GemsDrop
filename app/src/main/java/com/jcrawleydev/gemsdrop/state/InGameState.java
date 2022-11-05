package com.jcrawleydev.gemsdrop.state;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.MainViewModel;
import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.gameState.GameStateManager;

import static com.jcrawleydev.gemsdrop.gameState.GameState.Type.BEGIN_NEW_GAME;

public class InGameState implements GameState {


    public Game game;
    //private final ActionMediator actionMediator;
    private final ClickHandler clickHandler;
    private boolean hasClicked;
    private final GameStateManager gameStateManager;
    private final MainViewModel viewModel;

    public InGameState(Game game, GameStateManager gameStateManager, ClickHandler clickHandler){
        this.game = game;
        this.gameStateManager = gameStateManager;
        this.viewModel = gameStateManager.getViewModel();
        this.clickHandler = clickHandler;
    }


    @Override
    public void start(){
        gameStateManager.loadState(viewModel.currentGameState);
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
