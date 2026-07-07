package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.audio.SoundEffect.GEM_HITS_FLOOR;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.EVALUATE_GRID;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.gem.dropping.DroppingGems;


public class GemFreeFallState extends AbstractGameState{

    private DroppingGems droppingGems;

    public GemFreeFallState(Game game){
        super(game);
    }


    @Override
    public void start() {
        cancelTask();
        gemMover.disableControls();
        droppingGems = game.getDroppingGems();
        taskScheduler.scheduleWithRepeats(this::freeFallRemainingGems, game.getGravityInterval());
    }


    private void freeFallRemainingGems(){
        droppingGems.moveDown();
        game.updateDroppingGemsOnView();
        int numberOfGemsAdded = droppingGems.addConnectingGemsTo(gemGrid);

        if(numberOfGemsAdded > 0){
             soundEffectManager.play(GEM_HITS_FLOOR);
        }
        if(droppingGems.areAllAddedToGrid()){
            loadState(EVALUATE_GRID);
        }

    }

}
