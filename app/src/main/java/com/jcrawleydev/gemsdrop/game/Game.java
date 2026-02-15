package com.jcrawleydev.gemsdrop.game;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GAME_STARTED;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEM_REMOVAL_ANIMATION_COMPLETE;

import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.game.score.ScoreRecords;
import com.jcrawleydev.gemsdrop.game.state.AbstractGameState;
import com.jcrawleydev.gemsdrop.game.state.GameStateName;
import com.jcrawleydev.gemsdrop.game.state.StateManager;
import com.jcrawleydev.gemsdrop.view.fragments.game.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private DroppingGems droppingGems;
    private GameView gameView;
    private final GameComponents gameComponents = new GameComponents();
    private final StateManager stateManager = new StateManager();
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final AtomicBoolean hasQuitBeenInvoked = new AtomicBoolean(false);
    private final GameModel gameModel;


    public Game(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public void init(SoundPlayer soundPlayer, ScoreRecords scoreRecords){
        gameComponents.init(this, soundPlayer, scoreRecords);
        stateManager.init(this);
    }


    public GameModel getGameModel(){
        return gameModel;
    }


    public GameComponents getGameComponents(){
        return this.gameComponents;
    }


    public void setCurrentGameLevel(GameLevel level){
        setCurrentDropRate(level.startingDropDuration());
    }


    public void resetDropCount(){
        gameModel.resetDropCount();
    }


    public void setCurrentDropRate(int dropRate){
        gameModel.setDropRate(dropRate);
    }


    public int getGravityInterval(){
        return gameModel.GRAVITY_INTERVAL;
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


    public StateManager getStateManager(){
        return stateManager;
    }


    public void rotateGems(){
        stateManager.getCurrentGameState().rotate();
    }


    public void moveLeft(){
        stateManager.getCurrentGameState().left();
    }

    public void moveRight() {
        stateManager.getCurrentGameState().right();
    }


    public void moveUp(){
       // do nothing
    }


    public void moveDown(){
        stateManager.getCurrentGameState().down();
    }


    public void updateDroppingGemsOnView(){
        updateGemsOnView(droppingGems.getFreeGems());
    }


    public int getCurrentDropRate(){
        return gameModel.getDropRate();
    }


    public void incrementDropCount(){
        gameModel.incrementDropCount();
    }


    public int getDropCount(){
        return gameModel.getDropCount();
    }


    public void updateDropInterval(){
       gameModel.updateDropInterval();
    }


    public void createGemsOnView(DroppingGems droppingGems){
        if(droppingGems != null){
            gameView.createGems(droppingGems.get());
        }
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


    public void onGameOverAnimationComplete(){
        var taskScheduler = gameComponents.getTaskScheduler();
        taskScheduler.cancelTask();
        taskScheduler.scheduleOnce(this::quit, 5000);
    }


    public void quit(){
        if(hasQuitBeenInvoked.get()){
            return;
        }
        hasQuitBeenInvoked.set(true);
        gameView.showHighScores();
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
