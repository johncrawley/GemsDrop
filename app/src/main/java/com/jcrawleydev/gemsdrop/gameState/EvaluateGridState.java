package com.jcrawleydev.gemsdrop.gameState;


public class EvaluateGridState extends AbstractGameState{


    private final int maxColumnHeight;

    public EvaluateGridState(GameStateManager gameStateManager){
        super(gameStateManager);
        maxColumnHeight = gameStateManager.getMaxColumnHeight();
    }


    @Override
    public void start() {
        evaluator.evaluate();
        loadNextState();
    }


    private boolean hasGemGridExceedHeight(){
        return gemGrid.getColumnHeights()
                .stream()
                .peek(x -> System.out.println("col height: " + x))
                .anyMatch(x -> x > maxColumnHeight);
    }


    private void loadNextState(){
        GameState.Type gameStateType = evaluator.hasMarkedGems() ? Type.FLICKER :
                hasGemGridExceedHeight() ? Type.HEIGHT_EXCEEDED : Type.CREATE_NEW_GEMS;

        gameStateManager.loadState(gameStateType);
    }


    @Override
    public void stop() {
        //do nothing
    }

}
