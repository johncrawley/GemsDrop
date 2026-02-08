package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.score.Score;
import com.jcrawleydev.gemsdrop.game.score.ScoreRecords;

public class GameComponents {

    private final GemMover gemMover = new GemMover();
    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final SoundEffectManager soundEffectManager = new SoundEffectManager();
    private final DroppingGemsFactory droppingGemsFactory = new DroppingGemsFactory();
    private ScoreRecords scoreRecords;
    private GemGrid gemGrid;
    private Score score;
    private GridProps gridProps;


    public void init(Game game, SoundPlayer soundPlayer, ScoreRecords scoreRecords){
        this.scoreRecords = scoreRecords;
        soundEffectManager.init(soundPlayer);
        setModelComponents(game, game.getGameModel());
        score.clear();
    }


    public void setModelComponents(Game game, GameModel gameModel){
        this.gemGrid = gameModel.getGemGrid();
        this.score = gameModel.getScore();
        this.gridProps = gameModel.getGridProps();
        var droppingGemsEvaluator = new DroppingGemsEvaluator(game,this);
        gemMover.init(gemGrid, gridProps, droppingGemsEvaluator);
        soundEffectManager.setScore(score);
        droppingGemsFactory.setGridProps(gridProps);
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

}
