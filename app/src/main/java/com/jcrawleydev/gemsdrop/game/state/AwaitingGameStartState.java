package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;

public class AwaitingGameStartState extends AbstractGameState{


    public AwaitingGameStartState(Game game){
        super(game);
        game.startGame();
    }


    @Override
    public void start() {
        loadState(GameStateName.LOAD_LEVEL);
    }

}
