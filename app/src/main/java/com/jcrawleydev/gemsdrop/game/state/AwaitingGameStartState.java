package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.LOAD_LEVEL;

import com.jcrawleydev.gemsdrop.game.Game;

public class AwaitingGameStartState extends AbstractGameState{


    public AwaitingGameStartState(Game game){
        super(game);
    }


    @Override
    public void start() {
      //do nothing
    }

}
