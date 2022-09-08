package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;


public class EvaluateAction {

    private final Evaluator evaluator;
    private final ActionMediator actionMediator;
    private final GemGrid gemGrid;
    private final int maxColumnHeight;


    EvaluateAction(Evaluator evaluator, ActionMediator actionManager, GemGrid gemGrid, int maxColumnHeight){
        this.evaluator = evaluator;
        this.actionMediator = actionManager;
        this.gemGrid = gemGrid;
        this.maxColumnHeight = maxColumnHeight;
    }


    public void start(){
        evaluator.evaluate();
        if(evaluator.hasMarkedGems()){
            log("start() ->> evaluate.hasMarkedGems is true, about to startMarkedGemsFlicker()");
            actionMediator.startMarkedGemsFlicker();
        }
        else if(gemGrid.getColumnHeights().stream().peek(x -> System.out.println("col height: " + x)).anyMatch(x -> x > maxColumnHeight)){
            log("start() ->> column height exceeds maxColumn height, calling actionMediator.endGame");
            actionMediator.endGame();
        }
        else{
            log("start() ->> Resetting Drop()");
            actionMediator.resetDrop();
        }
    }

    private void log(String msg){
        System.out.println("EvaluateAction: " + msg);
    }


}
