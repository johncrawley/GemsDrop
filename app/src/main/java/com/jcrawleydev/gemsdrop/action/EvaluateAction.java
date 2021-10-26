package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;


public class EvaluateAction {

    private final Evaluator evaluator;
    private final ActionMediator actionManager;
    private final GemGrid gemGrid;
    private final int maxColumnHeight;


    EvaluateAction(Evaluator evaluator, ActionMediator actionManager, GemGrid gemGrid, int maxColumnHeight){
        this.evaluator = evaluator;
        this.actionManager = actionManager;
        this.gemGrid = gemGrid;
        this.maxColumnHeight = maxColumnHeight;
    }


    public void start(){
        evaluator.evaluate();
        if(evaluator.hasMarkedGems()){
            actionManager.startMarkedGemsFlicker();
        }
        else if(gemGrid.getColumnHeights().stream().anyMatch(x -> x > maxColumnHeight)){
            actionManager.endGame();
        }
        else{
            actionManager.resetDrop();
        }
    }




}
