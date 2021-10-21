package com.jcrawleydev.gemsdrop.state;

import com.jcrawleydev.gemsdrop.Game;

public class TitleState implements GameState {

    public Game game;

    public TitleState(Game game){
        this.game = game;
    }


    public void start(){

    }


    public void stop(){

    }

    @Override
    public void click(int x, int y) {

    }
}
