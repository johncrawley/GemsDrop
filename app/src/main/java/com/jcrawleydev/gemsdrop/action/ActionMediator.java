package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;

public class ActionMediator {

    private GemDropAction gemDropAction;
    private QuickDropGemsAction quickDropGemsAction;
    private EvaluateAction evaluateAction;
    private FlickerMarkedGemsAction flickerMarkedGemsAction;
    private DeleteMarkedGemsAction deleteMarkedGemsAction;
    private GemGridGravityDropAction gemGridGravityDropAction;


    public ActionMediator(GemGroupView gemGroupView, GemGridView gemGridView, GemControls gemControls, Evaluator evaluator){
        gemDropAction = new GemDropAction(gemControls, gemGroupView, gemGridView, this);
        quickDropGemsAction = new QuickDropGemsAction(this, gemGroupView, gemControls, gemGridView);
        evaluateAction = new EvaluateAction(evaluator, this);
        flickerMarkedGemsAction = new FlickerMarkedGemsAction(gemGridView, this);
        deleteMarkedGemsAction = new DeleteMarkedGemsAction(this, evaluator, gemGridView);
        gemGridGravityDropAction = new GemGridGravityDropAction(this, gemGridView);
    }

    public void onAllGemsAdded(){
        gemDropAction.cancelFutures();
        evaluateGemsInGrid();

    }

    public void evaluateGemsInGrid(){
        evaluateAction.start();
    }


    public void onAnyGemsAdded(){
        quickDropGemsAction.start();
        gemDropAction.cancelFutures();
    }

    public void finishQuickDrop(){
        quickDropGemsAction.stop();
    }


    public void startMarkedGemsFlicker(){
        flickerMarkedGemsAction.start();
    }

    public void resetDrop(){
        gemDropAction.reset();
    }


    public void deleteMarkedGems(){
        deleteMarkedGemsAction.start();
    }


    public void startGemGridGravityDrop(){
        gemGridGravityDropAction.start();
    }

    public void stopGridGravity(){
        gemGridGravityDropAction.stop();
    }
}
