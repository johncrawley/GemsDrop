package com.jcrawleydev.gemsdrop.service;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private GameService gameService;
    private int difficulty;
    private final int NUMBER_OF_ROWS = 14;
    private final int NUMBER_OF_COLUMNS = 7;
    private int initialPosition = -1;
    private int dropRate = 500;
    private Gem gem = new Gem(GemColor.BLUE);
    private List<Gem> droppingGems = new ArrayList<>();
    private GameView gameView;
    private Random random;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);

    private ScheduledExecutorService gemDropExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gemDropFuture;
    private AtomicBoolean isStarted = new AtomicBoolean(false);




    public void init(GameService gameService){
        this.gameService = gameService;
        random = new Random(System.currentTimeMillis());
    }


    public void setDifficulty(int difficulty){
        this.difficulty = difficulty;
    }


    private void createDroppingGems(){
        final int numberOfDroppingGems = 3;
        droppingGems.clear();
        for(int i = 0; i < numberOfDroppingGems; i++ ){
            droppingGems.add(new Gem(getRandomColor()));
        }
    }


    private void rotateGems(){


    }


    public GemColor getRandomColor(){
        int index = random.nextInt(gemColors.size());
        return gemColors.get(index);
    }



    public void startGame(){
        if(isStarted.get()){
            return;
        }
        isStarted.set(true);
        log("Entered startGame()");
        gemDropFuture = gemDropExecutor.scheduleWithFixedDelay(this::drop, 0, dropRate, TimeUnit.MILLISECONDS);
        gem.setPosition(-1);
        gem.setColumn(NUMBER_OF_COLUMNS / 2);
    }


    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }


    public void onDestroy(){
        if(gemDropFuture != null && !gemDropFuture.isCancelled()){
            gemDropFuture.cancel(false);
        }
        isStarted.set(false);
    }


    public void drop(){
        gem.incPosition();
        gameView.updateGems(gem);
        if(gem.getPosition() > 12){
            gem.setPosition(0);
        }
    }


    public void quit(){

    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }

}
