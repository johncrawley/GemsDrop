package com.jcrawleydev.gemsdrop.gameState;

public class GameOverState extends AbstractGameState{

    public GameOverState(GameStateManager gameStateManager){
        super(gameStateManager, Type.GAME_OVER);
    }

    @Override
    public void start() {
        gameStateManager.getGame().loadGameOverState();
    }


    @Override
    public void stop() {
        //do nothing
    }
}

