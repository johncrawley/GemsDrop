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
        //TODO: good place to add score updating
        if(evaluator.hasMarkedGems()){
            actionManager.startMarkedGemsFlicker();
            return;
        }
        actionManager.resetDrop();
    }

}
