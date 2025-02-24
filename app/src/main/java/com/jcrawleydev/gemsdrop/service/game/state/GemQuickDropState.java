package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.game.Game;

public class GemQuickDropState extends AbstractGameState{


    public GemQuickDropState(Game game){
        super(game);
    }


    @Override
    public void start() {
        log("Entered start()");
        taskScheduler.cancelTask();
        log("task cancelled");
        gemMover.disableControls();
        log("controls disabled()");
        taskScheduler.schedule(()-> gemMover.dropGems(), 80);
        log("task scheduled!");
    }
}
