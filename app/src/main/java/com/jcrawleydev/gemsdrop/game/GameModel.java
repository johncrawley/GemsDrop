package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.game.level.GameLevel;
import com.jcrawleydev.gemsdrop.game.score.Score;

public class GameModel {

    public final int GRAVITY_INTERVAL = 70;
    private int dropRate = 500;
    private int dropCount = 0;
    private int dropIntervalCounter;

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private final GemGrid gemGrid = new GemGridImpl(gridProps);
    private final Score score = new Score(50);
    private DroppingGems droppingGems;
    private int numberOfNormalGemsDropped;
    private GameLevel gameLevel;


    public void setGameLevel(GameLevel gameLevel){
        this.gameLevel = gameLevel;
    }


    public GameLevel getGameLevel(){
        return gameLevel;
    }


    private void log(String msg){
        System.out.println("^^^ GameModel : " + msg);
    }


    public void setDroppingGems(DroppingGems droppingGems){
        var isNull = droppingGems == null;
        log("Entered setDroppingGems() gems are null: " + isNull);
        this.droppingGems = droppingGems;
    }


    public void incNumberOfNormalsGemsDropped(){
        numberOfNormalGemsDropped++;
    }


    public int getNumberOfNormalGemsDropped(){
        return numberOfNormalGemsDropped;
    }


    public void resetNumberOfNormalGemsDropped(){
        numberOfNormalGemsDropped = 0;
    }


    public DroppingGems getDroppingGems(){
        var isNull = droppingGems == null;
        log("entered getDroppingGems() isNull:" + isNull);
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
