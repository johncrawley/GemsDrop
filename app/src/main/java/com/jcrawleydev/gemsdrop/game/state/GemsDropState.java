package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEM_QUICK_DROP;

import com.jcrawleydev.gemsdrop.game.Game;

import java.util.concurrent.atomic.AtomicBoolean;


public class GemsDropState extends AbstractGameState{

    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    public GemsDropState(Game game){
        super(game);
    }


    @Override
    public void start() {
        taskScheduler.scheduleWithRepeats(() -> gemMover.dropGems(), game.getCurrentDropRate());
        isStarted.set(true);
    }


    public void rotate(){
        if(isStarted.get()){
            gemMover.rotateGems();
        }
    }


    public void left(){
        if(isStarted.get()){
            gemMover.moveLeft();
        }
    }


    public void right(){
        if(isStarted.get()){
            gemMover.moveRight();
        }
    }


    public void down(){
        var droppingGems = game.getDroppingGems();
        if(droppingGems == null
                || gemMover.areControlsDisabled()
                || droppingGems.areAllAddedToGrid()
                || !isStarted.get()){
            return;
        }
        loadState(GEM_QUICK_DROP);
    }
}
