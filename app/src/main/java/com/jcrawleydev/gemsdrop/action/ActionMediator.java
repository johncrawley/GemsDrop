package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;

public class ActionMediator {

    private GemDropAction gemDropAction;
    private QuickDropGemsAction quickDropGemsAction;
    private EvaluateAction evaluateAction;
    private FlickerMarkedGemsAction flickerMarkedGemsAction;
    private DeleteMarkedGemsAction deleteMarkedGemsAction;
    private GemGridGravityDropAction gemGridGravityDropAction;


    private ActionMediator(GemGroupView gemGroupView, GemGridView gemGridView, GemControls gemControls, Evaluator evaluator, GemGroupFactory gemGroupFactory, Score score){
        gemDropAction = new GemDropAction(this, gemControls, gemGroupView, gemGridView, gemGroupFactory);
        quickDropGemsAction = new QuickDropGemsAction(this, gemGroupView, gemControls, gemGridView);
        evaluateAction = new EvaluateAction(evaluator, this);
        flickerMarkedGemsAction = new FlickerMarkedGemsAction(gemGridView, this);
        deleteMarkedGemsAction = new DeleteMarkedGemsAction(this, evaluator, gemGridView);
        gemGridGravityDropAction = new GemGridGravityDropAction(this, gemGridView);
    }

    public void createAndDropGems(){
        gemDropAction.start();
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



    public static class Builder{

        private Score score;
        private GemGroupView gemGroupView;
        private GemGridView gemGridView;
        private GemControls gemControls;
        private Evaluator evaluator;
        private StringBuilder str;
        private GemGroupFactory gemGroupFactory;


        public Builder gemGroupView(GemGroupView gemGroupView){
            this.gemGroupView = gemGroupView;
            return this;
        }

        public Builder gemControls(GemControls gemControls){
            this.gemControls = gemControls;
            return this;
        }

        public Builder evaluator(Evaluator evaluator){
            this.evaluator = evaluator;
            return this;
        }

        public Builder gemGroupFactory(GemGroupFactory gemGroupFactory){
            this.gemGroupFactory = gemGroupFactory;
            return this;
        }

        public Builder gridView(GemGridView gemGridView){
            this.gemGridView = gemGridView;
            return this;
        }


        public Builder score(Score score){
            this.score = score;
            return this;
        }


        public ActionMediator build() {
            verify();
            return new ActionMediator(gemGroupView, gemGridView, gemControls, evaluator, gemGroupFactory, score);
        }


        private void verify () {
            str = new StringBuilder("");
            appendErrorIfNull(gemGroupView, "gemGroupView");
            appendErrorIfNull(gemGridView, "gemGridView");
            appendErrorIfNull(gemControls, "gemControls");
            appendErrorIfNull(gemGroupFactory, "gemGroupFactory");
            appendErrorIfNull(score, "score");
            String errorStr = str.toString();

           if(!errorStr.isEmpty()){
               throw new RuntimeException("The following items need to be set for the ActionMediator builder: " + errorStr);
           }
        }


        private void appendErrorIfNull(Object obj, String name){
            if(obj == null){
                str.append(" ");
                str.append(name);
            }

        }
    }
}
