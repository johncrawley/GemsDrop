package com.jcrawleydev.gemsdrop.state;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.control.ClickHandler;

public class InGameState implements GameState {


    public Game game;
    private final ActionMediator actionMediator;
    private final ClickHandler clickHandler;
    private boolean hasClicked;

    public InGameState(Game game, ActionMediator actionMediator, ClickHandler clickHandler){
        this.game = game;
        this.actionMediator = actionMediator;
        this.clickHandler = clickHandler;
    }


    @Override
    public void start(){
        actionMediator.resetVariables();
        actionMediator.clearGemGrid();
        actionMediator.createAndDropGems();
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
