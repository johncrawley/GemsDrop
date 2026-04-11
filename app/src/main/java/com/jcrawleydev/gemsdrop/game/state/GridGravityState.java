package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.CREATE_GEMS;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.EVALUATE_GRID;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GAME_OVER;

import com.jcrawleydev.gemsdrop.audio.SoundEffect;
import com.jcrawleydev.gemsdrop.game.Game;

public class GridGravityState extends AbstractGameState{

    int totalFallenGems;

    public GridGravityState(Game game){
        super(game);
    }

    @Override
    public void start() {
        cancelTask();
        totalFallenGems = 0;
        taskScheduler.scheduleWithRepeats(this::applyGravity, game.getGravityInterval());
    }


    private void applyGravity(){
        var fallenGems = gemGrid.gravityDropGemsOnePosition();
        totalFallenGems += fallenGems.size();
        if(fallenGems.isEmpty()){
            if(totalFallenGems > 0){
                loadState(EVALUATE_GRID);
            }
            else{
                if(gemGrid.exceedsMaxHeight()){
                    loadState(GAME_OVER);
                }
                else{
                    loadState(CREATE_GEMS);
                }
            }
        }
        else{
            game.updateGemsOnView(fallenGems);
        }
    }
}
