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
        int alreadyConnecting = droppingGems.getNumberOfGemsAdded();
        droppingGems.moveDown();
        game.updateDroppingGemsOnView();
        droppingGems.addConnectingGemsTo(gemGrid);

        if(droppingGems.areAllAddedToGrid()){
            loadState(EVALUATE_GRID);
            return;
        }
        if(droppingGems.getNumberOfGemsAdded() > alreadyConnecting){
            soundEffectManager.play(GEM_HITS_FLOOR);
        }
    }

}
