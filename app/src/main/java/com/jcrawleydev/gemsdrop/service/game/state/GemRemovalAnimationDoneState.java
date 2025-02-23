package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GRID_GRAVITY;

import com.jcrawleydev.gemsdrop.service.game.Game;

public class GemRemovalAnimationDoneState extends AbstractGameState{

    public GemRemovalAnimationDoneState(Game game){
        super(game);
    }


    @Override
    public void start(){
        int numberOfGemsRemoved = gemGrid.removeMarkedGems();
        game.updateScore(numberOfGemsRemoved);
        if(numberOfGemsRemoved > 0){
            loadState(GRID_GRAVITY);
        }
    }
}
