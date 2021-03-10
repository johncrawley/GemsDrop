package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;

public class EvaluateAction {

    private Evaluator evaluator;
    private ActionMediator actionManager;


    EvaluateAction(Evaluator evaluator, ActionMediator actionManager){
        this.evaluator = evaluator;
        this.actionManager = actionManager;
    }


    public void start(){
        evaluator.evaluate();
        if(evaluator.hasMarkedGems()){
            actionManager.startMarkedGemsFlicker();
        }
        else{
            actionManager.resetDrop();
        }
    }

}
