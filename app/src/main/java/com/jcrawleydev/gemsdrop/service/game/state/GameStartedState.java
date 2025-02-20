package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;

public class GameStartedState extends AbstractGameState implements GameState {

    public GameStartedState(Game game){
        super(game);
    }


    @Override
    public void onStart() {

    }

}
