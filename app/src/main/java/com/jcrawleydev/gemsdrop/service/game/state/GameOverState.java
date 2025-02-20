package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;

public class GameOverState extends AbstractGameState implements GameState{

    public GameOverState(Game game){
        super(game);
    }

    @Override
    public void onStart() {

    }
}
