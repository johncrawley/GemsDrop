package com.jcrawleydev.gemsdrop.service.game.state;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.service.game.Game;
import com.jcrawleydev.gemsdrop.service.game.GameComponents;
import com.jcrawleydev.gemsdrop.service.game.GemMover;
import com.jcrawleydev.gemsdrop.service.game.TaskScheduler;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.score.Score;

public abstract class AbstractGameState {

    protected Game game;
    private final StateManager stateManager;
    protected GemGrid gemGrid;
    protected GemMover gemMover;
    protected TaskScheduler taskScheduler;
    protected SoundEffectManager soundEffectManager;
    protected Score score;
    protected GameComponents gameComponents;

    public AbstractGameState(Game game){
        this.game = game;
        this.gameComponents = game.getGameComponents();
        stateManager = game.getStateManager();
        gemGrid = gameComponents.getGemGrid();
        gemMover = gameComponents.getGemMover();
        taskScheduler = gameComponents.getTaskScheduler();
        soundEffectManager = gameComponents.getSoundEffectManager();
        score = gameComponents.getScore();
    }


    protected void cancelTask(){
        taskScheduler.cancelTask();
    }


    public void start() {

    }

    protected void loadState(GameStateName gameStateName){
        stateManager.load(gameStateName, this.getClass().getSimpleName());
    }

    public void log(String msg){
        System.out.println("^^^ " + this.getClass().getName() + " : " + msg);
    }

    public void rotate(){

    }

    public void left(){

    }

    public void right(){

    }

    public void down(){

    }
}
