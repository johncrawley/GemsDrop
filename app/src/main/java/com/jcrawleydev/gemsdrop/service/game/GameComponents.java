package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.service.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsFactory;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.service.game.score.Score;
import com.jcrawleydev.gemsdrop.service.records.ScoreRecords;

public class GameComponents {

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private final GemGrid gemGrid = new GemGridImpl(gridProps);
    private final GemMover gemMover = new GemMover();
    private final Score score = new Score(50);
    private final TaskScheduler taskScheduler = new TaskScheduler();
    private final SoundEffectManager soundEffectManager = new SoundEffectManager(score);
    private final DroppingGemsFactory droppingGemsFactory = new DroppingGemsFactory(gridProps);
    private ScoreRecords scoreRecords;


    public void init(Game game, SoundPlayer soundPlayer, ScoreRecords scoreRecords){
        this.scoreRecords = scoreRecords;
        var droppingGemsEvaluator = new DroppingGemsEvaluator(game,this);
        gemMover.init(gemGrid, gridProps, droppingGemsEvaluator);
        soundEffectManager.init(soundPlayer);
        score.clear();
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
