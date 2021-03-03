package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;

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
            return;
        }
        actionManager.resetDrop();
    }

}
