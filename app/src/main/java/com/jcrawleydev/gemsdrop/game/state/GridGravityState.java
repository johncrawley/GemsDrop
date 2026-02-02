package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.EVALUATE_GRID;

import com.jcrawleydev.gemsdrop.game.Game;

public class GridGravityState extends AbstractGameState{


    public GridGravityState(Game game){
        super(game);
    }

    @Override
    public void start() {
        cancelTask();
        taskScheduler.schedule(this::applyGravity, game.getGravityInterval());
    }

    private void applyGravity(){
        var fallenGems = gemGrid.gravityDropGemsOnePosition();
        if(fallenGems.isEmpty()){
            loadState(EVALUATE_GRID);
        }
        else{
            game.updateGemsOnView(fallenGems);
        }
    }
}
