package com.jcrawleydev.gemsdrop.state;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.action.ActionMediator;
import com.jcrawleydev.gemsdrop.control.ClickHandler;

public class InGameState implements GameState {


    public Game game;
    private final ActionMediator actionMediator;
    private final ClickHandler clickHandler;

    public InGameState(Game game, ActionMediator actionMediator, ClickHandler clickHandler){
        this.game = game;
        this.actionMediator = actionMediator;
        this.clickHandler = clickHandler;
    }


    @Override
    public void start(){
        actionMediator.resetScore();
        actionMediator.clearGemGrid();
        actionMediator.createAndDropGems();
    }


    @Override
    public void stop(){

    }


    @Override
    public void click(int x, int y) {
        clickHandler.click(x,y);
    }
}
