package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;

public class AwaitingGameStartState extends AbstractGameState{


    public AwaitingGameStartState(Game game){
        super(game);
    }


    @Override
    public void start() {
        game.startGame();
        loadState(GameStateName.LOAD_LEVEL);
    }

}
