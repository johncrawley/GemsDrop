package com.jcrawleydev.gemsdrop.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Game {

    private GameService gameService;
    private int difficulty;
    private final int NUMBER_OF_ROWS = 14;
    private int initialPosition = -1;
    private int dropRate = 500;

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
    }


    public void onDestroy(){
        if(gemDropFuture != null && !gemDropFuture.isCancelled()){
            gemDropFuture.cancel(false);
        }
    }

    public void drop(){

    }

    public void quit(){

    }
}
