package com.jcrawleydev.gemsdrop.service;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Game {

    private GameService gameService;
    private int difficulty;
    private final int NUMBER_OF_ROWS = 14;
    private final int NUMBER_OF_COLUMNS = 7;
    private int initialPosition = -1;
    private int dropRate = 500;
    private Gem gem = new Gem(GemColor.BLUE);
    private GameView gameView;

    private ScheduledExecutorService gemDropExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gemDropFuture;



    public void init(GameService gameService){
        this.gameService = gameService;
    }


    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }


    public void startGame(){
        gemDropFuture = gemDropExecutor.scheduleWithFixedDelay(this::drop, 0, dropRate, TimeUnit.MILLISECONDS);
        gem.setPosition(-1);
        gem.setColumn(NUMBER_OF_COLUMNS / 2);
    }


    public void onDestroy(){
        if(gemDropFuture != null && !gemDropFuture.isCancelled()){
            gemDropFuture.cancel(false);
        }
    }


    public void drop(){
        gem.incPosition();
        gameView.updateGems(gem);
    }


    public void quit(){

    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }

}
