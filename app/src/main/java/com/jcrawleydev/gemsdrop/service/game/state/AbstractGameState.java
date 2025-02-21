package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.GemMover;
import com.jcrawleydev.gemsdrop.service.game.TaskScheduler;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.score.Score;

import java.util.concurrent.ScheduledFuture;

public abstract class AbstractGameState {

    protected Game game;
    protected StateManager stateManager;
    protected GemGrid gemGrid;
    protected GemMover gemMover;
    protected TaskScheduler taskScheduler;
    protected SoundEffectManager soundEffectManager;
    protected Score score;

    public AbstractGameState(Game game){
        this.game = game;
        stateManager = game.getStateManager();
        gemGrid = game.getGemGrid();
        gemMover = game.getGemMover();
        taskScheduler = game.getTaskScheduler();
        soundEffectManager = game.getSoundEffectManager();
        score = game.getScore();
    }


    protected void sendEvent(GameEvent gameEvent){
        stateManager.sendEvent(gameEvent);
    }


    protected void cancelTask(){
        taskScheduler.cancelTask();
    }


    public void onStart() {

    }
}
