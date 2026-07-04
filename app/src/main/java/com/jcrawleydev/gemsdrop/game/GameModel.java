package com.jcrawleydev.gemsdrop.game;

import android.content.Context;

import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.background.RandomBackgroundPicker;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.game.level.LevelFactory;
import com.jcrawleydev.gemsdrop.game.score.HighScores;
import com.jcrawleydev.gemsdrop.game.score.Score;
import com.jcrawleydev.gemsdrop.game.state.GameStateName;

import java.util.List;

public class GameModel {

    public final int GRAVITY_INTERVAL = 58;
    private int dropCount = 0;

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private final GemGrid gemGrid = new GemGridImpl(gridProps);
    private final Score score = new Score(50);
    private final DroppingGemsFactory droppingGemsFactory = new DroppingGemsFactory(gemGrid);
    private final LevelFactory levelFactory;
    private final HighScores highScores;
    private final SoundPlayer soundPlayer;
    private DroppingGems droppingGems, nextDroppingGems;
    private GameLevel gameLevel;
    private GameStateName gameStateName = GameStateName.AWAITING_GAME_START;
    private int numberOfRowsAlreadyGreyedOut = 0;
    private RandomBackgroundPicker randomBackgroundPicker;
    private final DropRateUpdater dropRateUpdater;


    public GameModel(Context context, HighScores highScores, SoundPlayer soundPlayer){
        levelFactory = new LevelFactory(context);
        gameLevel = levelFactory.getLevel(1);
        droppingGemsFactory.setGridProps(gridProps);
        droppingGemsFactory.setLevel(gameLevel);
        this.highScores = highScores;
        this.soundPlayer = soundPlayer;
        dropRateUpdater = new DropRateUpdater();
    }


    public LevelFactory getLevelFactory(){
        return levelFactory;
    }


    public SoundPlayer getSoundPlayer(){
        return soundPlayer;
    }


    public void saveScore(){
        int scoreVal = score.get();
        highScores.saveScore(scoreVal);
    }


    public void startNewGame(){
        randomBackgroundPicker = new RandomBackgroundPicker();
        randomBackgroundPicker.pickRandomBackgroundIndex();
        gameStateName = GameStateName.AWAITING_GAME_START;
        droppingGemsFactory.onGameStart();
    }


    public int getRandomBackgroundIndex(){
        return randomBackgroundPicker.getCurrentBackgroundIndex();
    }


    public GameStateName getGameStateName(){
        return gameStateName;
    }


    public void setGameStateName(GameStateName gameStateName){
        this.gameStateName = gameStateName;
    }


    public void setGameLevel(GameLevel level){
        this.gameLevel = level;
        droppingGemsFactory.setLevel(level);
        setDropRate(level.startingDropDuration(), level.minimumDropDuration());
    }


    public void incNumberOfGreyedOutRows(){
        numberOfRowsAlreadyGreyedOut++;
    }


    public void resetNumberOfGreyedOutRows(){
        numberOfRowsAlreadyGreyedOut = 0;
    }


    public int getNumberOfRowsAlreadyGreyedOut(){
        return numberOfRowsAlreadyGreyedOut;
    }


    public DroppingGemsFactory getDroppingGemsFactory(){
        return droppingGemsFactory;
    }


    public DroppingGems createDroppingGems(){
        if(nextDroppingGems == null){
            createNextGems();
        }
        droppingGems = nextDroppingGems;
        dropCount++;
        dropRateUpdater.updateDropInterval();
        createNextGems();
        return droppingGems;
    }


    public DroppingGems getNextGems(){
        return nextDroppingGems;
    }


    private void createNextGems(){
        nextDroppingGems = droppingGemsFactory.createDroppingGems();
    }


    public DroppingGems getDroppingGems(){
        return droppingGems;
    }


    public GemGrid getGemGrid(){
        return gemGrid;
    }


    public List<Gem> getGridGems(){
        return gemGrid.getGems();
    }


    public GridProps getGridProps(){
        return gridProps;
    }


    public Score getScore(){
        return score;
    }


    public int getDropRate(){
        return dropRateUpdater.getDropRate();
    }


    public void setDropRate(int startingInterval, int minimumInterval){
        dropRateUpdater.init(startingInterval, minimumInterval);
    }


    public void resetDropCount(){
        dropCount = 0;
    }


    public void invalidateDroppingGems(){
        droppingGems = null;
    }
}
