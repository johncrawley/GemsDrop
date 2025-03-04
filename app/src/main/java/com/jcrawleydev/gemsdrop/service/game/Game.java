package com.jcrawleydev.gemsdrop.service.game;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GAME_STARTED;
import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GEM_REMOVAL_ANIMATION_COMPLETE;

import com.jcrawleydev.gemsdrop.service.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.service.game.state.AbstractGameState;
import com.jcrawleydev.gemsdrop.service.game.state.GameStateName;
import com.jcrawleydev.gemsdrop.service.game.state.StateManager;
import com.jcrawleydev.gemsdrop.service.records.ScoreRecords;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private DroppingGems droppingGems;
    private GameView gameView;
    public final int GRAVITY_INTERVAL = 70;
    private int currentDropRate = 500;
    private int dropIntervalCounter;
    private final GameComponents gameComponents = new GameComponents();
    private final StateManager stateManager = new StateManager();
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private GameLevel currentGameLevel;
    private int dropCount = 0;


    public void init(SoundPlayer soundPlayer, ScoreRecords scoreRecords){
        gameComponents.init(this, soundPlayer, scoreRecords);
        stateManager.init(this);
    }


    public void resetDropCount(){
        dropCount = 0;
    }

    public GameComponents getGameComponents(){
        return this.gameComponents;
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

    public StateManager getStateManager(){
        return stateManager;
    }

    public void rotateGems(){ stateManager.performMovement(AbstractGameState::rotate);}

    public void moveLeft(){
        stateManager.performMovement(AbstractGameState::left);
    }

    public void moveRight() {
        stateManager.performMovement(AbstractGameState::right);
    }


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
        loadState(GAME_STARTED);
    }


    public void moveDown(){
        stateManager.performMovement(AbstractGameState::down);
    }


    public DroppingGems getDroppingGems(){
        return droppingGems;
    }


    private void log(String msg){
       System.out.println("^^^ Game: " + msg);
    }


    public void onDestroy(){
        var taskScheduler = gameComponents.getTaskScheduler();
        if(taskScheduler != null){
            taskScheduler.cancelTask();
        }
        isStarted.set(false);
    }


    public void onGemRemovalAnimationDone(){
        loadState(GEM_REMOVAL_ANIMATION_COMPLETE);
    }


    private void loadState(GameStateName stateName){
        stateManager.load(stateName, "Game");
    }


    public void updateScore(int numberOfRemovedGems){
        var score = gameComponents.getScore();
        if(score == null){
            return;
        }
        score.addPointsFor(numberOfRemovedGems);
        gameView.updateScore(score.get());
    }


    public void end(){
        gameView.showGameOverAnimation();
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
        var gemGrid = gameComponents.getGemGrid();
        if(gemGrid == null){
            return;
        }
        var gridGems = gemGrid.getGems();
        if(!gridGems.isEmpty()){
            gameView.createGems(gridGems);
        }
    }
}
