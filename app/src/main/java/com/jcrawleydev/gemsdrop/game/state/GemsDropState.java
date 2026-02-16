package com.jcrawleydev.gemsdrop.game.state;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEM_QUICK_DROP;

import com.jcrawleydev.gemsdrop.game.Game;


public class GemsDropState extends AbstractGameState{


    public GemsDropState(Game game){
        super(game);
    }


    @Override
    public void start() {
        createDrop();
    }


    private void createDrop(){
        game.createDroppingGems();
        game.incrementDropCount();
        game.updateDropInterval();
        gemMover.setDroppingGems(game.getDroppingGems());
        gemGrid.printColumnHeights();
        score.resetMultiplier();
        game.createGemsOnView(game.getDroppingGems());
        taskScheduler.scheduleWithRepeats(() -> gemMover.dropGems(), game.getCurrentDropRate());
    }


    public void rotate(){
        gemMover.rotateGems();
    }


    public void left(){
        gemMover.moveLeft();
    }


    public void right(){
        gemMover.moveRight();
    }


    public void down(){
        var droppingGems = game.getDroppingGems();
        if(droppingGems == null
                || gemMover.areControlsDisabled()
                || droppingGems.areAllAddedToGrid()){
            return;
        }
        loadState(GEM_QUICK_DROP);
    }
}
