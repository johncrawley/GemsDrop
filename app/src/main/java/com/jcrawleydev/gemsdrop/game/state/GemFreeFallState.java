package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.audio.SoundEffect.GEM_HITS_FLOOR;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.EVALUATE_GRID;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEM_FREE_FALL;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;


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
        int alreadyConnecting = droppingGems.countConnectingToGrid();
        droppingGems.moveDown();
        game.updateDroppingGemsOnView();
        droppingGems.addConnectingGemsTo(gemGrid);

        if(droppingGems.areAllAddedToGrid()){
            loadState(EVALUATE_GRID);
            return;
        }
        if(droppingGems.countConnectingToGrid() > alreadyConnecting){
            soundEffectManager.playSoundEffect(GEM_HITS_FLOOR);
        }
    }

}
