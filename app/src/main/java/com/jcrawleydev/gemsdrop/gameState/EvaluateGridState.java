package com.jcrawleydev.gemsdrop.gameState;


public class EvaluateGridState extends AbstractGameState{


    private final int maxColumnHeight;

    public EvaluateGridState(GameStateManager gameStateManager){
        super(gameStateManager, Type.EVALUATE_GRID);
        maxColumnHeight = gameStateManager.getMaxColumnHeight();
    }


    @Override
    public void start() {
        super.start();
        evaluator.evaluate();
        loadNextState();
    }


    private void loadNextState(){
        if(evaluator.hasMarkedGems()){
            loadState(Type.FLICKER);
        }
        else if(hasGemGridExceedHeight()){
            loadState(Type.HEIGHT_EXCEEDED);
        }
        else{
            loadState(Type.CREATE_NEW_GEMS);
        }
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
