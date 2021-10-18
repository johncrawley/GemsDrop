package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.SoundPlayer;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.ScoreBoardLayer;

public class ActionMediator {

    private final GemDropAction gemDropAction;
    private final QuickDropGemsAction quickDropGemsAction;
    private final EvaluateAction evaluateAction;
    private final FlickerMarkedGemsAction flickerMarkedGemsAction;
    private final DeleteMarkedGemsAction deleteMarkedGemsAction;
    private final GemGridGravityDropAction gemGridGravityDropAction;
    private final Score score;

    private ActionMediator(SpeedController speedController,
                           GemGroupView gemGroupView,
                           GemGridLayer gemGridView,
                           GemControls gemControls,
                           Evaluator evaluator,
                           GemGroupFactory gemGroupFactory,
                           ScoreBoardLayer scoreView,
                           GemCountTracker gemCountTracker,
                           SoundPlayer soundPlayer){

        this.score = scoreView.getScore();
        gemDropAction = new GemDropAction(speedController, this, gemControls, gemGroupView, gemGridView, gemGroupFactory, score);
        quickDropGemsAction = new QuickDropGemsAction(this, gemGroupView, gemControls, gemGridView);
        evaluateAction = new EvaluateAction(evaluator, this);
        flickerMarkedGemsAction = new FlickerMarkedGemsAction(gemGridView, this);
        deleteMarkedGemsAction = new DeleteMarkedGemsAction(this, evaluator, gemGridView, scoreView, gemCountTracker, soundPlayer);
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
        gemDropAction.start();
    }


    public void deleteMarkedGems(){
        deleteMarkedGemsAction.start();
        System.out.println("SCORE: "  + score.get());
    }


    public void startGemGridGravityDrop(){
        gemGridGravityDropAction.start();
    }

    public void stopGridGravity(){
        gemGridGravityDropAction.stop();
    }



    public static class Builder{

        private Score score;
        private ScoreBoardLayer scoreView;
        private GemGroupView gemGroupView;
        private GemGridLayer gemGridView;
        private GemControls gemControls;
        private Evaluator evaluator;
        private StringBuilder str;
        private GemGroupFactory gemGroupFactory;
        private GemCountTracker gemCountTracker;
        private SoundPlayer soundPlayer;
        private SpeedController speedController;


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

        public Builder gridView(GemGridLayer gemGridView){
            this.gemGridView = gemGridView;
            return this;
        }


        public Builder scoreView(ScoreBoardLayer scoreView){
            this.scoreView = scoreView;
            this.score = scoreView.getScore();
            return this;
        }


        public Builder gemCountTracker(GemCountTracker gemCountTracker){
            this.gemCountTracker = gemCountTracker;
            return this;
        }

        public Builder soundPlayer(SoundPlayer soundPlayer){
            this.soundPlayer = soundPlayer;
            return this;
        }


        public Builder speedController(SpeedController speedController){
            this.speedController = speedController;
            return this;
        }


        public ActionMediator build() {
            verify();
            return new ActionMediator(speedController, gemGroupView, gemGridView, gemControls, evaluator, gemGroupFactory, scoreView, gemCountTracker, soundPlayer);
        }


        private void verify () {
            str = new StringBuilder("");
            appendErrorIfNull(gemGroupView, "gemGroupView");
            appendErrorIfNull(gemGridView, "gemGridView");
            appendErrorIfNull(gemControls, "gemControls");
            appendErrorIfNull(gemGroupFactory, "gemGroupFactory");
            appendErrorIfNull(speedController, "speedController");
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
