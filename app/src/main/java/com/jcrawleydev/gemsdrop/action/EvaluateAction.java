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
        log("Entered start()");
        evaluator.evaluate();
        if(evaluator.hasMarkedGems()){
            actionMediator.startMarkedGemsFlicker();
        }
        else if(gemGrid.getColumnHeights().stream().peek(x -> System.out.println("col height: " + x)).anyMatch(x -> x > maxColumnHeight)){
            actionMediator.endGame();
        }
        else{
            actionMediator.resetDrop();
        }
    }


    private void log(String msg){
        System.out.println("EvaluateAction: "+  msg);
    }

}
