package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.SoundPlayer;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;

public class DeleteMarkedGemsAction {


    private final ActionMediator actionMediator;
    private final Evaluator evaluator;
    private final GemGridLayer gemGridView;
    private final Score score;
    private final GemCountTracker gemCountTracker;
    private final ScoreBoardLayer scoreView;
    private final SoundPlayer soundPlayer;

    public DeleteMarkedGemsAction(ActionMediator actionMediator,
                                  Evaluator evaluator,
                                  GemGridLayer gemGridView,
                                  ScoreBoardLayer scoreView,
                                  GemCountTracker gemCountTracker,
                                  SoundPlayer soundPlayer){
        this.actionMediator = actionMediator;
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
        actionMediator.startGemGridGravityDrop();
    }


}
