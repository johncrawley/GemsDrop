package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.game.Game;

public class GemQuickDropState extends AbstractGameState{


    public GemQuickDropState(Game game){
        super(game);
    }


    @Override
    public void start() {
        taskScheduler.cancelTask();
        gemMover.disableControls();
        taskScheduler.scheduleWithRepeats(()-> gemMover.dropGems(), 80);
    }
}
