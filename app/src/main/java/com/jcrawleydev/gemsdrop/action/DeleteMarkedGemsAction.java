package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.ScoreView;

public class DeleteMarkedGemsAction {


    private ActionMediator actionManager;
    private Evaluator evaluator;
    private GemGridView gemGridView;
    private Score score;
    private GemCountTracker gemCountTracker;
    private ScoreView scoreView;

    public DeleteMarkedGemsAction(ActionMediator actionManager, Evaluator evaluator, GemGridView gemGridView, ScoreView scoreView, GemCountTracker gemCountTracker){
        this.actionManager = actionManager;
        this.evaluator = evaluator;
        this.gemGridView = gemGridView;
        this.score = scoreView.getScore();
        this.scoreView = scoreView;
        this.gemCountTracker = gemCountTracker;
    }


    public void start(){
        gemCountTracker.startTracking();
        evaluator.deleteMarkedGems();
        int count = gemCountTracker.getDifference();
        score.addPointsFor(count);
        score.incMultiplier();
        scoreView.draw();
        gemGridView.draw();
        actionManager.startGemGridGravityDrop();
    }
}
