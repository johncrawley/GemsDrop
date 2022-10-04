package com.jcrawleydev.gemsdrop.gameState;


import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

public class EvaluateGridState implements GameState{

    private final GameStateManager gameStateManager;
    private final Evaluator evaluator;
    private final GemGrid gemGrid;
    private final int maxColumnHeight;

    public EvaluateGridState(GameStateManager gameStateManager){
        this.gameStateManager = gameStateManager;
        gemGrid = gameStateManager.getGemGridLayer().getGemGrid();
        evaluator = new Evaluator(gemGrid, 3);
        maxColumnHeight = gameStateManager.getMaxColumnHeight();
    }


    @Override
    public void start() {
        evaluator.evaluate();
        GameState.Type gameStateType =
                evaluator.hasMarkedGems() ? Type.FLICKER :
                hasGemGridExceedHeight()  ? Type.HEIGHT_EXCEEDED : Type.CREATE_NEW_GEMS;
        gameStateManager.loadState(gameStateType);
    }


    private boolean hasGemGridExceedHeight(){
        return gemGrid.getColumnHeights()
                .stream()
                .peek(x -> System.out.println("col height: " + x))
                .anyMatch(x -> x > maxColumnHeight);
    }


    @Override
    public void stop() {
        //do nothing
    }

}
