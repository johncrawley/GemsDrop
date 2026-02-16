package com.jcrawleydev.gemsdrop.game;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GAME_STARTED;
import static com.jcrawleydev.gemsdrop.game.state.GameStateName.GEM_REMOVAL_ANIMATION_COMPLETE;

import com.jcrawleydev.gemsdrop.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.game.score.Score;
import com.jcrawleydev.gemsdrop.game.score.ScoreRecords;
import com.jcrawleydev.gemsdrop.game.state.GameStateName;
import com.jcrawleydev.gemsdrop.game.state.StateManager;
import com.jcrawleydev.gemsdrop.view.fragments.game.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private GameView gameView;
    private final StateManager stateManager = new StateManager();
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final AtomicBoolean hasQuitBeenInvoked = new AtomicBoolean(false);
    private final GameModel gameModel;

    private final GemMover gemMover = new GemMover();
    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final SoundEffectManager soundEffectManager = new SoundEffectManager();
    private final DroppingGemsFactory droppingGemsFactory = new DroppingGemsFactory();
    private ScoreRecords scoreRecords;
    private GemGrid gemGrid;
    private Score score;
    private GridProps gridProps;
    private DroppingGems droppingGems;


    public Game(GameModel gameModel){
        this.gameModel = gameModel;
    }

    public void init(SoundPlayer soundPlayer, ScoreRecords scoreRecords){
        this.scoreRecords = scoreRecords;
        soundEffectManager.init(soundPlayer);
        score.clear();
        this.gemGrid = gameModel.getGemGrid();
        this.score = gameModel.getScore();
        this.gridProps = gameModel.getGridProps();
        var droppingGemsEvaluator = new DroppingGemsEvaluator(this);
        gemMover.init(gemGrid, gridProps, droppingGemsEvaluator);
        soundEffectManager.setScore(score);
        droppingGemsFactory.setGridProps(gridProps);
        stateManager.init(this);
    }


    public GameModel getGameModel(){
        return gameModel;
    }


    public void createDroppingGems(){
        droppingGemsFactory.createDroppingGems();
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
        if(droppingGems != null){
            updateGemsOnView(droppingGems.getFreeGems());
        }
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


    public DroppingGemsFactory getDroppingGemsFactory(){
        return droppingGemsFactory;
    }


    public GridProps getGridProps(){
        return gridProps;
    }


    public SoundEffectManager getSoundEffectManager(){
        return soundEffectManager;
    }


    public Score getScore(){
        return score;
    }


    public void saveScore(){
        scoreRecords.saveScore(score.get());
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


    public void clearScore(){
        score.clear();
    }


    public DroppingGems getDroppingGems(){
        return gameModel.getDroppingGems();
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
        if(gemGrid == null){
            return;
        }
        var gridGems = gemGrid.getGems();
        if(!gridGems.isEmpty()){
            gameView.createGems(gridGems);
        }
    }
}
