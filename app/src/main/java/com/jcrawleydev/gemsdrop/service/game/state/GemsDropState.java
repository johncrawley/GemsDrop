package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;


public class GemsDropState extends AbstractGameState implements GameState{

    private DroppingGems droppingGems;

    public GemsDropState(Game game){
        super(game);
    }

    @Override
    public void onStart() {
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



}
