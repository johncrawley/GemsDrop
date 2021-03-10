package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.GemGridView;

public class DeleteMarkedGemsAction {


    private ActionMediator actionManager;
    private Evaluator evaluator;
    private GemGridView gemGridView;
    private Score score;
    private GemCountTracker gemCountTracker;

    public DeleteMarkedGemsAction(ActionMediator actionManager, Evaluator evaluator, GemGridView gemGridView, Score score, GemCountTracker gemCountTracker){
        this.actionManager = actionManager;
        this.evaluator = evaluator;
        this.gemGridView = gemGridView;
        this.score = score;
        this.gemCountTracker = gemCountTracker;
    }


    public void start(){
        gemCountTracker.startTracking();
        evaluator.deleteMarkedGems();
        int count = gemCountTracker.getDifference();
        score.addPointsFor(count);
        score.incMultiplier();
        gemGridView.draw();
        actionManager.startGemGridGravityDrop();
    }
}
