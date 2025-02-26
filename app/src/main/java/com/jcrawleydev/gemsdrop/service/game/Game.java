package com.jcrawleydev.gemsdrop.service.game;

import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GAME_STARTED;
import static com.jcrawleydev.gemsdrop.service.game.state.GameStateName.GEM_REMOVAL_ANIMATION_COMPLETE;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.service.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.service.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.service.game.score.Score;
import com.jcrawleydev.gemsdrop.service.game.state.AbstractGameState;
import com.jcrawleydev.gemsdrop.service.game.state.GameStateName;
import com.jcrawleydev.gemsdrop.service.game.state.StateManager;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private DroppingGems droppingGems;

    private GameView gameView;
    public final int GRAVITY_INTERVAL = 70;

    private int currentDropRate = 500;
    private int dropIntervalCounter;

    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final GemGrid gemGrid = new GemGridImpl(gridProps);
    private final GemMover gemMover = new GemMover();
    private final Score score = new Score(50);
    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final SoundEffectManager soundEffectManager = new SoundEffectManager(score);
    private final DroppingGemsFactory droppingGemsFactory = new DroppingGemsFactory(gridProps);

    private GameLevel currentGameLevel;
    private int dropCount = 0;

    private StateManager stateManager;

    public void init(SoundPlayer soundPlayer){
        stateManager = new StateManager();
        gemMover.init(gemGrid, gridProps, new DroppingGemsEvaluator(this));
        soundEffectManager.init(soundPlayer);
        score.clear();
        stateManager.init(this);
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


    public DroppingGemsFactory getDroppingGemsFactory(){
        return droppingGemsFactory;
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


    public void rotateGems(){
        stateManager.performMovement(AbstractGameState::rotate);}


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
        taskScheduler.cancelTask();
        isStarted.set(false);
    }


    public void onGemRemovalAnimationDone(){
        loadState(GEM_REMOVAL_ANIMATION_COMPLETE);
    }


    private void loadState(GameStateName stateName){
        stateManager.load(stateName, "Game");
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
