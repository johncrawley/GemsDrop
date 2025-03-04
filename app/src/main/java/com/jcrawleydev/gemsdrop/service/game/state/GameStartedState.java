package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.LOAD_LEVEL;

import com.jcrawleydev.gemsdrop.service.game.Game;

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
