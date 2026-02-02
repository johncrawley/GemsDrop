package com.jcrawleydev.gemsdrop.game.state;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.game.GameComponents;
import com.jcrawleydev.gemsdrop.game.GemMover;
import com.jcrawleydev.gemsdrop.game.TaskScheduler;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.score.Score;

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
        //Do Nothing
    }


    protected void loadState(GameStateName gameStateName){
        stateManager.load(gameStateName, this.getClass().getSimpleName());
    }


    public void log(String msg){
        System.out.println("^^^ " + this.getClass().getName() + " : " + msg);
    }


    public void rotate(){
        //do nothing
    }


    public void left(){
        //do nothing
    }


    public void right(){
        //do nothing
    }


    public void down(){
        //do nothing
    }
}
