package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.Game;

import java.util.concurrent.Executors;

public class LoadGameOverAction {

    private final Game game;

    public LoadGameOverAction(Game game){
        this.game = game;
    }

    public void loadGameOver(){
        game.loadGameOverState();
        //Executors.newSingleThreadExecutor().execute(game::loadGameOverState);
    }
}