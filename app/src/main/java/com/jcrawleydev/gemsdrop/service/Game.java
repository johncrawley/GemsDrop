package com.jcrawleydev.gemsdrop.service;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.gem.GemPosition;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid2;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private GameService gameService;
    private final int NUMBER_OF_ROWS = 14;
    private final int NUMBER_OF_COLUMNS = 7;
    private final Gem gem = new Gem(GemColor.BLUE);
    private List<Gem> droppingGems = new ArrayList<>();
    private GameView gameView;
    private Random random;
    private final List<GemColor> gemColors = List.of(GemColor.RED, GemColor.BLUE, GemColor.PURPLE, GemColor.GREEN, GemColor.YELLOW);
    private boolean isOrientationVertical = true;

    private final ScheduledExecutorService gemDropExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gemDropFuture;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    private GemGrid2 gemGrid = new GemGrid2(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS);


    public void init(GameService gameService){
        this.gameService = gameService;
        random = new Random(System.currentTimeMillis());
        createDroppingGems();
    }


    private void createDroppingGems(){
        droppingGems.clear();
        droppingGems.add(new Gem(getRandomColor(), GemPosition.TOP));
        droppingGems.add(new Gem(getRandomColor(), GemPosition.CENTRE));
        droppingGems.add(new Gem(getRandomColor(), GemPosition.BOTTOM));
    }


    public void rotateGems(){
        if(canRotate()){
            isOrientationVertical = !isOrientationVertical;
            for(Gem gem: droppingGems){
                gem.rotate();
            }
            gameView.updateGems(droppingGems);
            //gem.rotate();
            //gameView.updateGems(List.of(gem));
        }
    }


    private boolean canRotate(){
        return true;
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
        gem.setPosition(GemPosition.TOP);
        log("Entered startGame()");
        int dropRate = 500;
        gemDropFuture = gemDropExecutor.scheduleWithFixedDelay(this::drop, 0, dropRate, TimeUnit.MILLISECONDS);
        for(Gem gem : droppingGems){
            gem.setDepth(-1);
            gem.setColumn(NUMBER_OF_COLUMNS / 2);
        }
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
        log("Entered drop()");
        int numberOfGemsPerDrop = 3;
       droppingGems.forEach(this::incHeightOf);
       gameView.updateGems(droppingGems);
       droppingGems = gemGrid.addGems(droppingGems, isOrientationVertical);
       if(droppingGems.isEmpty()){
           switchToEvalMode();
           return;
       }
       if(droppingGems.size() < numberOfGemsPerDrop){
           switchToFreeFallMode();
       }
    }


    private void switchToEvalMode(){

    }


    private void switchToFreeFallMode(){

    }




    private void incHeightOf(Gem gem){
        gem.incDepth();
        if(gem.getDepth() > 12){
            gem.setDepth(0);
        }
    }


    public void quit(){

    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }

}
