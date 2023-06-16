package com.jcrawleydev.gemsdrop.gameState;

public class BeginNewGameState extends AbstractGameState{

    public BeginNewGameState(GameStateManager gameStateManager){
        super(gameStateManager, Type.BEGIN_NEW_GAME);
    }


    @Override
    public void start() {
        super.start();
        resetVariables();
        clearGemGrid();
        loadState(Type.CREATE_NEW_GEMS);
    }


    @Override
    public void stop() {
        //do nothing
    }


    private void resetVariables(){
        speedController.reset();
        score.clear();
        scoreBoardLayer.draw();
    }


    private void clearGemGrid(){
        gemGridLayer.clearGemGrid();
    }

}