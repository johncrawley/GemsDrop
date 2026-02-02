package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GAME_OVER;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEMS_DROP;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.grid.GridEvaluator;


public class EvaluateGridState extends AbstractGameState{

    private final GridEvaluator evaluator;

    public EvaluateGridState(Game game){
        super(game);
        evaluator = new GridEvaluator(gemGrid.getGemColumns(), gameComponents.getGridProps().numberOfRows());
    }


    @Override
    public void start() {
        evaluateGemGrid();
    }


    public void evaluateGemGrid(){
        log("entered evaluateGemGrid()");
        cancelTask();
        long[] markedGemsIds = evaluator.evaluateGemGrid();
        int numberOfGemsToRemove = markedGemsIds.length;
        if(numberOfGemsToRemove > 0){
            game.removeGemsFromView(markedGemsIds);
            game.updateScore(numberOfGemsToRemove);
            soundEffectManager.playGemsRemovedSound();
        }
        else{
            if(gemGrid.exceedsMaxHeight()){
                loadState(GAME_OVER);
            }
            else{
                log("evaluateGemGrid() grid doesn't exceed max height, so loading gems drop state");
                loadState(GEMS_DROP);
            }
        }
    }

}
