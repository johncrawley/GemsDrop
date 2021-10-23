package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.SoundPlayer;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.score.GemCountTracker;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;
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
                           GemGroupLayer gemGroupView,
                           GemGridLayer gemGridView,
                           GemControls gemControls,
                           Evaluator evaluator,
                           GemGroupFactory gemGroupFactory,
                           ScoreBoardLayer scoreView,
                           GemCountTracker gemCountTracker,
                           SoundPlayer soundPlayer,
                           int gravityInterval,
                           int gridGravityDistanceFactor,
                           int flickerMarkedGemsTime){

        this.score = scoreView.getScore();
        gemDropAction = new GemDropAction(speedController, this, gemControls, gemGroupView, gemGridView, gemGroupFactory, score);
        quickDropGemsAction = new QuickDropGemsAction(this, gemGroupView, gemControls, gemGridView, gravityInterval);
        evaluateAction = new EvaluateAction(evaluator, this);
        flickerMarkedGemsAction = new FlickerMarkedGemsAction(gemGridView, this, flickerMarkedGemsTime );
        deleteMarkedGemsAction = new DeleteMarkedGemsAction(this, evaluator, gemGridView, scoreView, gemCountTracker, soundPlayer);
        gemGridGravityDropAction = new GemGridGravityDropAction(this, gemGridView, gravityInterval, gridGravityDistanceFactor);
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
        private GemGroupLayer gemGroupView;
        private GemGridLayer gemGridView;
        private GemControls gemControls;
        private Evaluator evaluator;
        private StringBuilder str;
        private GemGroupFactory gemGroupFactory;
        private GemCountTracker gemCountTracker;
        private SoundPlayer soundPlayer;
        private SpeedController speedController;
        private int gravityInterval;
        private int gridGravityDistanceFactor;
        private int flickerMarkedGemsTime;


        public Builder gemGroupView(GemGroupLayer gemGroupView){
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


        public Builder gridGravityDistanceFactor(int gridGravityDistanceFactor){
            this.gridGravityDistanceFactor = gridGravityDistanceFactor;
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


        public Builder gravityInterval(int gravityInterval){
            this.gravityInterval = gravityInterval;
            return this;
        }


        public Builder flickerMarkedGemsTime(int flickerMarkedGemsTime){
            this.flickerMarkedGemsTime = flickerMarkedGemsTime;
            return this;
        }

        public ActionMediator build() {
            verify();
            return new ActionMediator(speedController,
                    gemGroupView,
                    gemGridView,
                    gemControls,
                    evaluator,
                    gemGroupFactory,
                    scoreView,
                    gemCountTracker,
                    soundPlayer,
                    gravityInterval,
                    gridGravityDistanceFactor,
                    flickerMarkedGemsTime);
        }


        private void verify () {
            str = new StringBuilder();
            appendErrorIfNull(gemGroupView, "gemGroupView");
            appendErrorIfNull(gemGridView, "gemGridView");
            appendErrorIfNull(gemControls, "gemControls");
            appendErrorIfNull(gemGroupFactory, "gemGroupFactory");
            appendErrorIfNull(speedController, "speedController");
            appendErrorIfNull(gravityInterval, "gravityInterval");
            appendErrorIfNull(gridGravityDistanceFactor, "gridGravityDistanceFactor");
            appendErrorIfNull(flickerMarkedGemsTime, "flickerMarkedGemsTime");
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
