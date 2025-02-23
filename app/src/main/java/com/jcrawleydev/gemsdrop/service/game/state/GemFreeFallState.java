package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.EVALUATE_GRID;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;


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
        taskScheduler.schedule(this::freeFallRemainingGems, game.getGravityInterval());
    }


    private void freeFallRemainingGems(){
        droppingGems.moveDown();
        game.updateDroppingGemsOnView();
        droppingGems.addConnectingGemsTo(gemGrid);
        if(droppingGems.areAllAddedToGrid()){
            loadState(EVALUATE_GRID);
        }
    }

}
