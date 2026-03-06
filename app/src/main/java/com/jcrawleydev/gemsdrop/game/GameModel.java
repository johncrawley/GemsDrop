package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.game.level.LevelFactory;
import com.jcrawleydev.gemsdrop.game.score.Score;
import com.jcrawleydev.gemsdrop.game.state.GameStateName;

import java.util.List;

public class GameModel {

    public final int GRAVITY_INTERVAL = 70;
    private int dropRate = 500;
    private int dropCount = 0;
    private int dropIntervalCounter;

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private final GemGrid gemGrid = new GemGridImpl(gridProps);
    private final Score score = new Score(50);
    private final DroppingGemsFactory droppingGemsFactory = new DroppingGemsFactory();
    private DroppingGems droppingGems;
    private GameLevel gameLevel = new LevelFactory().getLevel(1);
    private GameStateName gameStateName = GameStateName.AWAITING_GAME_START;
    private final GemMover gemMover = new GemMover();
    private int numberOfRowsAlreadyGreyedOut = 0;


    public GameModel(){
        droppingGemsFactory.setGridProps(gridProps);
        droppingGemsFactory.setLevel(gameLevel);
        gemMover.init(gemGrid, gridProps);
    }


    public void setDroppingGemsEvaluator(DroppingGemsEvaluator evaluator){
        gemMover.setDroppingGemsEvaluator(evaluator);
    }


    public void startNewGame(){
        gameStateName = GameStateName.AWAITING_GAME_START;
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
        setDropRate(level.startingDropDuration());
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


    public void createDroppingGems(){
        droppingGems = droppingGemsFactory.createDroppingGems();
        gemMover.setDroppingGems(droppingGems);
        dropCount++;
        updateDropInterval();
    }


    public GemMover getGemMover(){
        return gemMover;
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
        return dropRate;
    }

    public void setDropRate(int rate){
        this.dropRate = rate;
    }


    public void incrementDropCount(){
        dropCount++;
    }


    public int getDropCount(){
        return dropCount;
    }


    public void resetDropCount(){
        dropCount = 0;
    }


    public void updateDropInterval(){
        int minimumInterval = 120;
        int intervalDecrement = 20;
        dropIntervalCounter++;
        if(dropIntervalCounter > 9){
            dropRate = Math.max(minimumInterval, dropRate - intervalDecrement);
            dropIntervalCounter = 0;
        }
    }


    public void invalidateDroppingGems(){
        droppingGems = null;
    }
}
