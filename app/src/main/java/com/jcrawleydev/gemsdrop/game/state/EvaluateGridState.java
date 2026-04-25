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
        evaluator = new GridEvaluator();
    }

    private long startTime;

    @Override
    public void start() {
        evaluateGemGrid();
    }


    public void evaluateGemGrid(){
        startTime = System.currentTimeMillis();
        var s3 = System.currentTimeMillis();
        cancelTask();
        var elap3 = System.currentTimeMillis() - s3;
        log("time taken to cancel task: " + elap3);
        evaluator.init(gemGrid.getGemColumns(), game.getGridProps().numberOfRows());
        long s2 = System.currentTimeMillis();
        var markedGemsIds = evaluator.evaluateGemGrid();
        var elap2 = System.currentTimeMillis() - s2;
        log("evaluateGemGrid(), time taken to evaluate: " + elap2);
        if(doAnyExist(markedGemsIds)){
            game.removeGemsFromView(markedGemsIds);
            soundEffectManager.playGemsRemovedSound();
        }
        else{
            onNoGemsToBeRemoved();
        }
    }


    private void onNoGemsToBeRemoved(){
        if(gemGrid.exceedsMaxHeight()){
            loadState(GAME_OVER);
        }
        else{
            var elapsed = System.currentTimeMillis() - startTime;
            log("noGemsToBeRemoved() time elapsed from start of state: " + elapsed);
            soundEffectManager.play(GEM_HITS_FLOOR);
            loadState(CREATE_GEMS);
        }
    }


    private boolean doAnyExist(long[] markedGemIds){
        return markedGemIds.length > 0;
    }

}
