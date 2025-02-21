package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;


public class GemFreeFallState extends AbstractGameState implements GameState{

    private DroppingGems droppingGems;

    public GemFreeFallState(Game game){
        super(game);
        cancelTask();
        gemMover.disableControls();
    }


    @Override
    public void onStart() {
        droppingGems = game.getDroppingGems();
        taskScheduler.schedule(this::freeFallRemainingGems, game.getGravityInterval());
    }


    private void freeFallRemainingGems(){
        droppingGems.moveDown();
        game.updateDroppingGemsOnView();
        droppingGems.addConnectingGemsTo(gemGrid);
        if(droppingGems.areAllAddedToGrid()){
            stateManager.sendEvent(GameEvent.ALL_GEMS_CONNECTED);
        }
    }

}
