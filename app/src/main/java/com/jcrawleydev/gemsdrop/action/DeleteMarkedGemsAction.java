package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.view.GemGridView;

public class DeleteMarkedGemsAction {


    private ActionMediator actionManager;
    private Evaluator evaluator;
    private GemGridView gemGridView;

    public DeleteMarkedGemsAction(ActionMediator actionManager, Evaluator evaluator, GemGridView gemGridView){
        this.actionManager = actionManager;
        this.evaluator = evaluator;
        this.gemGridView = gemGridView;
    }


    public void start(){
        evaluator.deleteMarkedGems();
        gemGridView.draw();
        actionManager.startGemGridGravityDrop();
    }
}
