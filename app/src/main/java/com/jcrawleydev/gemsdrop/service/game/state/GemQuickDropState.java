package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;

public class GemQuickDropState extends AbstractGameState implements GameState{


    public GemQuickDropState(Game game){
        super(game);
    }


    @Override
    public void onStart() {
        taskScheduler.cancelTask();
        gemMover.disableControls();
        taskScheduler.schedule(()-> gemMover.dropGems(), 0, 80);
    }
}
