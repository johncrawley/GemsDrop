package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;

public class GemRemovalAnimationDoneState extends AbstractGameState{

    public GemRemovalAnimationDoneState(Game game){
        super(game);
    }


    @Override
    public void onStart(){
        int numberOfGemsRemoved = gemGrid.removeMarkedGems();
        game.updateScore(numberOfGemsRemoved);
        if(numberOfGemsRemoved > 0){
            stateManager.sendEvent(GameEvent.GEMS_REMOVED_FROM_GRID);
        }
    }
}
