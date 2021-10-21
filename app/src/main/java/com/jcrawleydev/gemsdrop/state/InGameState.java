package com.jcrawleydev.gemsdrop.state;

import com.jcrawleydev.gemsdrop.Game;

public class InGameState implements GameState {


    public Game game;

    public InGameState(Game game){
        this.game = game;
    }

    @Override
    public void start(){

    }


    @Override
    public void stop(){

    }



    @Override
    public void click(int x, int y) {

    }
}
