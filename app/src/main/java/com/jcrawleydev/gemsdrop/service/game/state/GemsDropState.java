package com.jcrawleydev.gemsdrop.service.game.state;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GEM_QUICK_DROP;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;


public class GemsDropState extends AbstractGameState{

    private DroppingGems droppingGems;

    public GemsDropState(Game game){
        super(game);
    }

    @Override
    public void start() {
        createDrop();
    }


    private void createDrop(){
        createGems();
        score.resetMultiplier();
        game.createGemsOnView(droppingGems);
        taskScheduler.schedule(()-> gemMover.dropGems(), game.getCurrentDropRate());
    }


    public void createGems(){
        droppingGems = new DroppingGems(game.getGridProps());
        game.setDroppingGems(droppingGems);
        game.incrementDropCount();
        droppingGems.create(game.getDropCount());
        game.updateDropInterval();
        gemMover.setDroppingGems(droppingGems);
        gemGrid.printColumnHeights();
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
        if(droppingGems == null
                || gemMover.areControlsDisabled()
                || droppingGems.areAllAddedToGrid()
                || droppingGems.areInInitialPosition()){
            return;
        }
        loadState(GEM_QUICK_DROP);
    }
}
