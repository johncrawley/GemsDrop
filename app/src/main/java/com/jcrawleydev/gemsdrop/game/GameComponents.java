package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.audio.SoundEffectManager;
import com.jcrawleydev.gemsdrop.audio.SoundPlayer;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
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
    private DroppingGems droppingGems;




    public void createDroppingGems(){
        droppingGems = droppingGemsFactory.createDroppingGems();
    }


    public DroppingGems getDroppingGems(){
        return droppingGems;
    }


    public void onDestroy(){
        taskScheduler.cancelTask();
    }



}
