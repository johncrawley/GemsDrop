package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.Game;
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
    private final GemsFreeFallAction gemsFreeFallAction;
    private final EvaluateAction evaluateAction;
    private final FlickerMarkedGemsAction flickerMarkedGemsAction;
    private final DeleteMarkedGemsAction deleteMarkedGemsAction;
    private final GemGridGravityDropAction gemGridGravityDropAction;
    private final Score score;
    private final GemGridLayer gemGridLayer;
    private final Game game;
    private final ScoreBoardLayer scoreBoardLayer;
    private final SpeedController speedController;

    private ActionMediator(Game game,
                           SpeedController speedController,
                           GemGroupLayer gemGroupLayer,
                           GemGridLayer gemGridLayer,
                           GemControls gemControls,
                           Evaluator evaluator,
                           GemGroupFactory gemGroupFactory,
                           ScoreBoardLayer scoreboardLayer,
                           GemCountTracker gemCountTracker,
                           SoundPlayer soundPlayer,
                           int gravityInterval,
                           int gridGravityDistanceFactor,
                           int flickerMarkedGemsTime,
                           int maxColumnHeight){

        this.game = game;
        this.speedController = speedController;
        this.score = scoreboardLayer.getScore();
        this.scoreBoardLayer = scoreboardLayer;
        this.gemGridLayer = gemGridLayer;
        gemDropAction = new GemDropAction(speedController, this, gemControls, gemGroupLayer, gemGridLayer, gemGroupFactory, score);
        gemsFreeFallAction = new GemsFreeFallAction(this, gemGroupLayer, gemControls, gemGridLayer, gravityInterval);
        evaluateAction = new EvaluateAction(evaluator, this, gemGridLayer.getGemGrid(), maxColumnHeight);
        flickerMarkedGemsAction = new FlickerMarkedGemsAction(gemGridLayer, this, flickerMarkedGemsTime );
        deleteMarkedGemsAction = new DeleteMarkedGemsAction(this, evaluator, gemGridLayer, scoreboardLayer, gemCountTracker, soundPlayer);
        gemGridGravityDropAction = new GemGridGravityDropAction(this, gemGridLayer, gravityInterval, gridGravityDistanceFactor);
    }

    /*

        In-game sub states:
            - falling state
                - create gem group
                - drop gemGroup
                - check for gemGroup connects
                - if connects:
                    - if vertical, enter evaluate state
                    - if horizontal,
                        if all gems connected, enter evaluate state
                        else enter free-fall state

            - evaluate state
                - if gems match up, enter flicker state
                 else enter falling state


            - flicker state
                - list of gems to be deleted
                - flicker animation for 1 second (for example)
                - delete gems, add to score
                - enter gemGrid gravity state

            - gemGridGravity state
                - drop gems in grid
                - if gems are settled, enter evaluate state


     */

    public void createAndDropGems(){
        gemDropAction.reset();
        gemDropAction.start();
    }


    public void resetVariables(){
        speedController.reset();
        score.clear();
        scoreBoardLayer.draw();
        flickerMarkedGemsAction.cancelFutures();
        gemGridGravityDropAction.cancelFutures();
        gemsFreeFallAction.cancelFutures();
    }


    public void clearGemGrid(){
        gemGridLayer.clearGemGrid();
    }


    public void onAllGemsAdded(){
        log("Entered onAllGemsAdded() about to gemDropAction.cancelFutures()");
        gemDropAction.cancelFutures();
        log("onAllGemsAdded() about to evaluateGemsInGrid()");
        //evaluateGemsInGrid();
        evaluateGemsInGridAllAdded();
    }


    public void evaluateGemsInGrid(){
        log("Entered evaluateGemsInGrid(), about to evaluateAction.start()");
        evaluateAction.start();
    }


    public void evaluateGemsInGridAllAdded(){
        log("Entered evaluateGemsInGridAllAdded(), about to evaluateAction.start()");
        evaluateAction.start();
    }


    public void onAnyGemsAdded(){
        log("Entered onAnyGemsAdded");
        gemDropAction.cancelFutures();
        log("onAnyGemsAdded() starting freeFall action");
        gemsFreeFallAction.start();
    }


    public void startMarkedGemsFlicker(){
        flickerMarkedGemsAction.start();
    }


    public void endGame(){
        System.out.println("Actionmediator: entered endGame()");
        gemDropAction.cancelFutures();
        System.out.println("Actionmediator: cancelled futures()");
        gemGridLayer.turnAllGemsGrey();
        System.out.println("Actionmediator: allgems turned grey");
        game.loadGameOverState();
        System.out.println("ActionMediator: game over state loaded, exiting endGame()");
    }


    public void resetDrop(){
        log("Entered resetDrop()");
        gemDropAction.reset();
        log("resetDrop() after GemDropAction.reset()");
        gemDropAction.start();
        log("exiting resetDrop(), after invoking gemDropAction.start()");
    }

    private void log(String msg){
        System.out.println("ActionMediator: " + msg);
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
        private Game game;
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
        private int maxColumnHeight;


        public Builder game(Game game){
            this.game = game;
            return this;
        }


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


        public Builder maxColumnHeight(int maxColumnHeight){
            this.maxColumnHeight = maxColumnHeight;
            return this;
        }


        public ActionMediator build() {
            verify();
            return new ActionMediator(game,
                    speedController,
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
                    flickerMarkedGemsTime,
                    maxColumnHeight);
        }


        private void verify () {
            str = new StringBuilder();
            appendErrorIfNull(game, "game");
            appendErrorIfNull(gemGroupView, "gemGroupView");
            appendErrorIfNull(gemGridView, "gemGridView");
            appendErrorIfNull(gemControls, "gemControls");
            appendErrorIfNull(gemGroupFactory, "gemGroupFactory");
            appendErrorIfNull(speedController, "speedController");
            appendErrorIfNull(gravityInterval, "gravityInterval");
            appendErrorIfNull(gridGravityDistanceFactor, "gridGravityDistanceFactor");
            appendErrorIfNull(flickerMarkedGemsTime, "flickerMarkedGemsTime");
            appendErrorIfNull(maxColumnHeight, "maxColumnHeight");
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
