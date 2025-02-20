package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;

public abstract class AbstractGameState {

    protected Game game;
    protected StateManager stateManager;

    public AbstractGameState(Game game){
        this.game = game;
        this.stateManager = game.getStateManager();
    }


    protected void sendEvent(GameEvent gameEvent){
        stateManager.sendEvent(gameEvent);
    }


    public void onStart() {

    }
}
