package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.SoundPlayer;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.ScoreView;

public class DeleteMarkedGemsAction {


    private ActionMediator actionManager;
    private Evaluator evaluator;
    private GemGridLayer gemGridView;
    private Score score;
    private GemCountTracker gemCountTracker;
    private ScoreView scoreView;
    private SoundPlayer soundPlayer;

    public DeleteMarkedGemsAction(ActionMediator actionManager, Evaluator evaluator, GemGridLayer gemGridView, ScoreView scoreView, GemCountTracker gemCountTracker, SoundPlayer soundPlayer){
        this.actionManager = actionManager;
        this.evaluator = evaluator;
        this.gemGridView = gemGridView;
        this.score = scoreView.getScore();
        this.scoreView = scoreView;
        this.gemCountTracker = gemCountTracker;
        this.soundPlayer = soundPlayer;
    }


    public void start(){
        gemCountTracker.startTracking();
        evaluator.deleteMarkedGems();
        int count = gemCountTracker.getDifference();
        score.addPointsFor(count);
        soundPlayer.playGemDisappearSound(score.getMultiplier());
        score.incMultiplier();
        scoreView.draw();
        gemGridView.draw();
        actionManager.startGemGridGravityDrop();
    }
}
