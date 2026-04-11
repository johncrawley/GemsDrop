package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.audio.SoundEffect.GEM_HITS_FLOOR;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GAME_OVER;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.CREATE_GEMS;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.grid.GridEvaluator;


public class EvaluateGridState extends AbstractGameState{

    private final GridEvaluator evaluator;

    public EvaluateGridState(Game game){
        super(game);
        evaluator = new GridEvaluator(gemGrid.getGemColumns(), game.getGridProps().numberOfRows());
    }


    @Override
    public void start() {
        evaluateGemGrid();
    }


    public void evaluateGemGrid(){
        cancelTask();
        var markedGemsIds = evaluator.evaluateGemGrid();
        if(doAnyExist(markedGemsIds)){
            game.removeGemsFromView(markedGemsIds);
            soundEffectManager.playGemsRemovedSound();
        }
        else{
            onNoGemsToBeRemoved();
        }
    }


    private void onNoGemsToBeRemoved(){
        log("entered no gems to be removed!");
        if(gemGrid.exceedsMaxHeight()){
            loadState(GAME_OVER);
        }
        else{
            soundEffectManager.play(GEM_HITS_FLOOR);
            loadState(CREATE_GEMS);
        }
    }


    private boolean doAnyExist(long[] markedGemIds){
        return markedGemIds.length > 0;
    }

}
