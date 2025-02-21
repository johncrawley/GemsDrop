package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.grid.GridEvaluator;


public class EvaluateGridState extends AbstractGameState implements GameState{

    private final GridEvaluator evaluator;

    public EvaluateGridState(Game game){
        super(game);
        evaluator = new GridEvaluator(gemGrid.getGemColumns(), game.getGridProps().numberOfRows());
    }


    @Override
    public void onStart() {
        evaluateGemGrid();
    }


    public void evaluateGemGrid(){
        cancelTask();
        long[] markedGemsIds = evaluator.evaluateGemGrid();
        int numberOfGemsToRemove = markedGemsIds.length;
        if(numberOfGemsToRemove > 0){
            game.removeGemsFromView(markedGemsIds);
            game.updateScore(numberOfGemsToRemove);
            soundEffectManager.playGemsRemovedSound();
        }
        else{
            checkForHeightExceeded();
        }
    }


    private void checkForHeightExceeded(){
        if(gemGrid.exceedsMaxHeight()){
            stateManager.sendEvent(GameEvent.GEM_COLUMN_EXCEEDS_MAXIMUM_HEIGHT);
        }
        else{
            stateManager.sendEvent(GameEvent.DROP_GEMS);
        }
    }


}
