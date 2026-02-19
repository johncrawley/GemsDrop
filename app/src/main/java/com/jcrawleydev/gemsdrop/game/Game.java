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
    private GridProps gridProps;


    public Game(GameModel gameModel){
        this.gameModel = gameModel;
    }


    public void init(SoundPlayer soundPlayer, ScoreRecords scoreRecords){
        this.scoreRecords = scoreRecords;
        soundEffectManager.init(soundPlayer);
        this.gridProps = gameModel.getGridProps();
        var droppingGemsEvaluator = new DroppingGemsEvaluator(this);
        gemMover.init(gameModel.getGemGrid(), gridProps, droppingGemsEvaluator);
        soundEffectManager.setScore(gameModel.getScore());
        droppingGemsFactory.setGridProps(gridProps);
        droppingGemsFactory.setGameModel(gameModel);
        stateManager.init(this);
    }


    public GameModel getGameModel(){
        return gameModel;
    }


    public void createDroppingGems(){
        var isFactoryNull = droppingGemsFactory == null;
        log("entered createDroppingGems() is factory null: " + isFactoryNull);
        var droppingGems = droppingGemsFactory.createDroppingGems();
        if(droppingGems == null){
            log("createDroppingGems() gems are null!");
        }
        gameModel.setDroppingGems(droppingGemsFactory.createDroppingGems());
    }


    public void setCurrentGameLevel(GameLevel level){
        gameModel.setGameLevel(level);
        droppingGemsFactory.setLevel(level);
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
        var droppingGems = gameModel.getDroppingGems();
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
        return gameModel.getScore();
    }


    public void saveScore(){
        scoreRecords.saveScore(gameModel.getScore().get());
    }


    public TaskScheduler getTaskScheduler(){
        return taskScheduler;
    }


    public GemMover getGemMover(){
        return gemMover;
    }


    public GemGrid getGemGrid(){
        return gameModel.getGemGrid();
    }


    public void clearScore(){
        gameModel.getScore().clear();
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
        gameModel.getScore().addPointsFor(numberOfRemovedGems);
        gameView.updateScore(gameModel.getScore().get());
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
        var gemGrid = gameModel.getGemGrid();
        if(gemGrid == null){
            return;
        }
        var gridGems = gemGrid.getGems();
        if(!gridGems.isEmpty()){
            gameView.createGems(gridGems);
        }
    }
}
