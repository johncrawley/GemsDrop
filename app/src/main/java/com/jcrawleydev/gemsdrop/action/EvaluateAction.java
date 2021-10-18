package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;


public class EvaluateAction {

    private final Evaluator evaluator;
    private final ActionMediator actionManager;


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
