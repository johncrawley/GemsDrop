package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.game.level.LevelFactory;
import com.jcrawleydev.gemsdrop.game.score.Score;
import com.jcrawleydev.gemsdrop.game.state.GameStateName;

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


    public GameModel(){
        droppingGemsFactory.setGridProps(gridProps);
        droppingGemsFactory.setLevel(gameLevel);
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


    public DroppingGemsFactory getDroppingGemsFactory(){
        return droppingGemsFactory;
    }


    public void createDroppingGems(){
        droppingGems = droppingGemsFactory.createDroppingGems();
    }


    public DroppingGems getDroppingGems(){
        return droppingGems;
    }


    public GemGrid getGemGrid(){
        return gemGrid;
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
}
