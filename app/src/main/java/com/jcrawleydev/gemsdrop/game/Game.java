package com.jcrawleydev.gemsdrop.game;

import static com.jcrawleydev.gemsdrop.game.state.GameStateName.AWAITING_GAME_START;
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
import com.jcrawleydev.gemsdrop.game.state.GameStateName;
import com.jcrawleydev.gemsdrop.game.state.StateManager;
import com.jcrawleydev.gemsdrop.view.fragments.game.GameView;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Game {

    private final GameView gameView;
    private final StateManager stateManager = new StateManager();
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final AtomicBoolean hasQuitBeenInvoked = new AtomicBoolean(false);
    private final GameModel gameModel;
    private GemMover gemMover;
    private DroppingGemsEvaluator droppingGemsEvaluator;

    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final SoundEffectManager soundEffectManager = new SoundEffectManager();
    private GridProps gridProps;


    public Game(GameModel gameModel, GameView gameView){
        this.gameModel = gameModel;
        this.gameView = gameView;
    }


    public void init(SoundPlayer soundPlayer){
        soundEffectManager.init(soundPlayer);
        this.gridProps = gameModel.getGridProps();
        soundEffectManager.setScore(gameModel.getScore());
        droppingGemsEvaluator = new DroppingGemsEvaluator(this);
        gemMover = new GemMover(this, droppingGemsEvaluator);
        gemMover.init(gameModel.getGemGrid(), gameModel.getGridProps());
        stateManager.init(this);
        updateScoreOnView();
        addExistingGemViews();
    }


    public DroppingGemsEvaluator getDroppingGemsEvaluator(){
        return droppingGemsEvaluator;
    }


    public void startGame(){
        isStarted.set(false);
        gameModel.resetNumberOfGreyedOutRows();
        gameModel.resetDropCount();
        gameModel.getGemGrid().init();
        clearScore();
    }


    public void setRandomBackgroundOnView(){

    }


    public GameModel getGameModel(){
        return gameModel;
    }


    public void createDroppingGems(){
        var droppingGems = gameModel.createDroppingGems();
        gemMover.setDroppingGems(droppingGems);
        createGemsOnView(droppingGems);
    }


    public void setCurrentGameLevel(GameLevel level){
        gameModel.setGameLevel(level);
    }


    public GemMover getGemMover(){
        return gemMover;
    }


    public void resetDropCount(){
        gameModel.resetDropCount();
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
            gameView.createGems(droppingGems.getFreeGems());
        }
    }


    public void updateGemsOnView(List<Gem> gems){
        gameView.updateGems(gems);
    }


    public void updateGemsColorsOnView(List<Gem> gems){
        gameView.updateGemsColors(gems);
    }


    public void addExistingGemViews(){
        if(gameModel.getGameStateName() != AWAITING_GAME_START){
            createGridGemsOnView();
            createGemsOnView(gameModel.getDroppingGems());
        }
    }


    public DroppingGemsFactory getDroppingGemsFactory(){
        return gameModel.getDroppingGemsFactory();
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
        log("Entered saveScore()");
        gameModel.saveScore();
    }

    private void log(String msg){
        System.out.println("^^^ Game: " + msg);
    }

    public TaskScheduler getTaskScheduler(){
        return taskScheduler;
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


    public void onGameFragmentDestroy(){
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
        updateScoreOnView();
    }


    private void updateScoreOnView(){
        var score = gameModel.getScore().get();
        var displayedScore = score < 2 ? 0 : score;
        gameView.updateScore(displayedScore);
    }


    public void end(){
        gameView.loadGameOver();
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


    public void terminate(){
        if(hasQuitBeenInvoked.get()){
            return;
        }
        hasQuitBeenInvoked.set(true);
        taskScheduler.cancelTask();
        gameModel.invalidateDroppingGems();
    }


    public void removeGemsFromView(long[] markedGemsIds){
        gameView.wipeOut(markedGemsIds);
    }


    public void createGridGemsOnView(){
        gameView.createGems(gameModel.getGridGems());
    }
}
