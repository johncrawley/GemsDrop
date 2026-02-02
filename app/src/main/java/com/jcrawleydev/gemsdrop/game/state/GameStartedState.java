package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.LOAD_LEVEL;

import com.jcrawleydev.gemsdrop.game.Game;

public class GameStartedState extends AbstractGameState{


    public GameStartedState(Game game){
        super(game);
    }


    @Override
    public void start() {
        if(game.isStarted()){
            return;
        }
        game.setStarted();
        gameComponents.clearScore();
        loadState(LOAD_LEVEL);
    }

}
