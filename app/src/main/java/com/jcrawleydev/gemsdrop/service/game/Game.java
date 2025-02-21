package com.jcrawleydev.gemsdrop.service.game;

import static com.jcrawleydev.gemsdrop.service.game.state.GameEvent.QUICK_DROP_INITIATED;
import static com.jcrawleydev.gemsdrop.service.game.state.GameEvent.START_GAME;

import android.content.Context;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.service.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.service.game.score.Score;
import com.jcrawleydev.gemsdrop.service.game.state.GameEvent;
import com.jcrawleydev.gemsdrop.service.game.state.StateManager;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private DroppingGems droppingGems;

    private GameView gameView;
    private GemMover gemMover;
    public final int GRAVITY_INTERVAL = 70;

    private int currentDropRate = 500;
    private int dropIntervalCounter;

    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final GemGrid gemGrid = new GemGridImpl(gridProps);
    private final Score score = new Score(50);
    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final SoundEffectManager soundEffectManager = new SoundEffectManager(score);
    private DroppingGemsEvaluator droppingGemsEvaluator;

    private GameLevel currentGameLevel;
    private int dropCount = 0;

    private StateManager stateManager;

    public void init(Context context){
        gemMover = new GemMover(this, gemGrid, gridProps);
        soundEffectManager.init(context);
        score.clear();
        droppingGemsEvaluator = new DroppingGemsEvaluator(this);
        stateManager = new StateManager();
        stateManager.init(this);
        stateManager.sendEvent(START_GAME);
    }


    public void clearScore(){
        score.clear();
    }


    public GridProps getGridProps(){
        return gridProps;
    }


    public void resetDropCount(){
        dropCount = 0;
    }


    public void setCurrentGameLevel(GameLevel level){
        this.currentGameLevel = level;
    }


    public void setCurrentDropRate(int dropRate){
        this.currentDropRate = dropRate;
    }

    public void setDroppingGems(DroppingGems droppingGems){
        this.droppingGems = droppingGems;
    }


    public void setStarted(){
        isStarted.set(true);
    }

    public boolean isStarted(){
        return isStarted.get();
    }


    public int getGravityInterval(){
        return GRAVITY_INTERVAL;
    }

    public SoundEffectManager getSoundEffectManager(){
        return soundEffectManager;
    }

    public Score getScore(){
        return score;
    }

    public TaskScheduler getTaskScheduler(){
        return taskScheduler;
    }


    public GemMover getGemMover(){
        return gemMover;
    }


    public GemGrid getGemGrid(){
        return gemGrid;
    }


    public StateManager getStateManager(){
        return stateManager;
    }


    public void rotateGems(){ gemMover.rotateGems(); }


    public void moveLeft(){
        gemMover.moveLeft();
    }


    public void moveRight(){ gemMover.moveRight(); }


    public void moveUp(){
      //  gemMover.moveUp();
    }


    public void updateDroppingGemsOnView(){
        updateGemsOnView(droppingGems.getFreeGems());
    }


    public int getCurrentDropRate(){
        return currentDropRate;
    }

    public void incrementDropCount(){
        dropCount++;
    }

    public int getDropCount(){
        return dropCount;
    }



    public void updateDropInterval(){
        int minimumInterval = 120;
        int intervalDecrement = 20;
        dropIntervalCounter++;
        if(dropIntervalCounter > 9){
            currentDropRate = Math.max(minimumInterval, currentDropRate - intervalDecrement);
            dropIntervalCounter = 0;
        }
    }


    public void createGemsOnView(DroppingGems droppingGems){
        gameView.createGems(droppingGems.get());
    }


    public void updateGemsOnView(List<Gem> gems){
        gameView.updateGems(gems);
    }


    public void updateGemsColorsOnView(List<Gem> gems){
        gameView.updateGemsColors(gems);
    }


    public void startGame(){
        stateManager.sendEvent(START_GAME);
    }


    public void moveDown(){
        if(droppingGems == null
                || droppingGems.areAllAddedToGrid()
                || droppingGems.areInInitialPosition()){
            return;
        }
        stateManager.sendEvent(QUICK_DROP_INITIATED);
    }


    public boolean evaluateTouchingGems(){
       return droppingGemsEvaluator.evaluateTouchingGems(droppingGems);
    }


    public DroppingGems getDroppingGems(){
        return droppingGems;
    }


    private void log(String msg){
       System.out.println("^^^ Game: " + msg);
    }


    public void onDestroy(){
        taskScheduler.cancelTask();
        isStarted.set(false);
    }


    public void onGemRemovalAnimationDone(){
       stateManager.sendEvent(GameEvent.GEM_REMOVAL_ANIMATION_COMPLETE);
    }


    public void updateScore(int numberOfRemovedGems){
        score.addPointsFor(numberOfRemovedGems);
        gameView.updateScore(score.get());
    }


    public void end(){

    }


    public void quit(){

    }


    public void removeGemsFromView(long[] markedGemsIds){
        gameView.wipeOut(markedGemsIds);
    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }


    public void onGameViewReady(){
        updateGridGemsOnView();
    }


    private void updateGridGemsOnView(){
        var gridGems = gemGrid.getGems();
        if(!gridGems.isEmpty()){
            gameView.createGems(gridGems);
        }
    }
}
