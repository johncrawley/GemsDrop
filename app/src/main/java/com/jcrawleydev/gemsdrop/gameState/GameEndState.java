package com.jcrawleydev.gemsdrop.gameState;

public class GameEndState extends AbstractGameState{

    public GameEndState(GameStateManager gameStateManager){
        super(gameStateManager, Type.GAME_END);
    }

    @Override
    public void start() {
        super.start();
        log("Entered start()");
        gameStateManager.getGame().loadGameOverState();
    }

    private void log(String msg){
        System.out.println("^^^ GameOverState: " + msg);
    }

    @Override
    public void stop() {
        //do nothing
    }
}

