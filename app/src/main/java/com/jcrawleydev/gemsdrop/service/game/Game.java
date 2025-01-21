package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.service.game.grid.GridEvaluator;
import com.jcrawleydev.gemsdrop.view.GameView;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


    /*
        There are four kinds of gem downward movement

        - gem drop, handled by task, the gem group drops a position at a scheduled rate
        - rotation, invoked by the user, a gem in the top position, can move downwards in a clockwise motion
        - quick drop, invoked by the user, locks controls, gem group falls at a faster pace.
        - free fall, if one or two gems gets added to the grid the remaining gem(s) will drop, controls are locked
        - grid gravity, if gems are removed from the grid, any gems above will fall down to occupy the empty space


        game flow:
            - create gems
            - drop gems
              - check if falling gems are touching grid
                    - yes:
                            - stop drop task
                            - evaluate grid, mark any gems to be removed
                            - if there are gems to be removed
                                - animate removal of gems on view, once finished:
                                    - update score
                                    - remove gems from grid columns
                                    - start grid gravity task, move out-of-position gems one position per task invocation
                                        - once gems are all dropped via grid gravity, run eval step

                             - if there are no gems to be removed:
                                - start drop task

                             - if there are no falling gems left, create new gems and start drop task



        dropping gems are moved -> are any touching? -> evaluate gem grid -> wipe out marked gems ->


     */

public class Game {

    private final GridProps gridProps = new GridProps(15, 7, 2);
    private DroppingGems droppingGems;

    private GameView gameView;
    private GridEvaluator evaluator;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private GemMover gemMover;
    private GameOverAnimator gameOverAnimator;
    private final int GRAVITY_INTERVAL = 70;

    private final GemGridImpl gemGrid = new GemGridImpl(gridProps);
    private int currentDropRate = 500;
    private int dropIntervalCounter;


    public void init(){
        evaluator = new GridEvaluator(gemGrid.getGemColumns(), gridProps.numberOfRows());
        gemMover = new GemMover(this, gemGrid, gridProps);
        gameOverAnimator = new GameOverAnimator(this, gemGrid, gridProps);
        createGems();
    }


    public void rotateGems(){ gemMover.rotateGems(); }


    public void moveLeft(){
        gemMover.moveLeft();
    }


    public void moveRight(){ gemMover.moveRight(); }


    public void moveUp(){
      //  gemMover.moveUp();
    }


    public void createGems(){
        log("entered createGems()");
        droppingGems = new DroppingGems(gridProps);
        droppingGems.create();
        updateDropInterval();
        gemMover.setDroppingGems(droppingGems);
        gemGrid.printColumnHeights();
    }


    public void updateDroppingGemsOnView(){
        updateGemsOnView(droppingGems.getFreeGems());
    }


    private void updateDropInterval(){
        int minimumInterval = 120;
        int intervalDecrement = 20;
        dropIntervalCounter++;
        if(dropIntervalCounter > 9){
            currentDropRate = Math.max(minimumInterval, currentDropRate - intervalDecrement);
            dropIntervalCounter = 0;
        }
    }


    public void updateGemsOnView(List<Gem> gems){
        gameView.updateGems(gems);
    }


    public void startGame(){
        if(isStarted.get()){
            return;
        }
        isStarted.set(true);
        startDroppingGems(currentDropRate);
    }


    private final ScheduledExecutorService firstEx = Executors.newSingleThreadScheduledExecutor();


    private void startDroppingGems(int dropRate){
        firstEx.schedule(()->{
            log("started dropping gems!");
            createGems();
            updateDroppingGemsOnView();
            future = executor.scheduleWithFixedDelay(()-> gemMover.dropGems(), 0, dropRate, TimeUnit.MILLISECONDS);
        }, 1000, TimeUnit.MILLISECONDS);
    }


    public void moveDown(){
        if(droppingGems.areAllAddedToGrid() || droppingGems.areInInitialPosition()){
            return;
        }
        cancelTask();
        gemMover.disableControls();
        future = executor.scheduleWithFixedDelay(()-> gemMover.dropGems(), 0, 80, TimeUnit.MILLISECONDS);

        // gemMover.moveDown();
    }


    public DroppingGems getDroppingGems(){
        return droppingGems;
    }


    private void log(String msg){
       System.out.println("^^^ Game: " + msg);
    }


    public void onDestroy(){
        cancelTask();
        isStarted.set(false);
    }


    public boolean evaluateTouchingGems(){
        boolean haveAnyGemsBeenAdded = false;
        droppingGems.addConnectingGemsTo(gemGrid);

        if(droppingGems.areAllAddedToGrid()){
            gemMover.disableControls();
            evaluateGemGrid();
            haveAnyGemsBeenAdded = true;
        }
        else if(droppingGems.areAnyAddedToGrid()){
            gemMover.disableControls();
            startGemFreeFall();
            haveAnyGemsBeenAdded = true;
        }
        return haveAnyGemsBeenAdded;
    }


    public void onGemRemovalAnimationDone(){
        int initialGemCount = gemGrid.gemCount();
        gemGrid.removeMarkedGems();
        int removedGemsCount = initialGemCount - gemGrid.gemCount();
        updateScore(removedGemsCount);
        if(removedGemsCount > 0){
            activateGridGravity();
        }
    }


    public void startGemFreeFall(){
        cancelTask();
        gemMover.disableControls();
        future = executor.scheduleWithFixedDelay(this::freeFallRemainingGems, 0, GRAVITY_INTERVAL, TimeUnit.MILLISECONDS);
    }


    private void freeFallRemainingGems(){
        droppingGems.moveDown();
        updateDroppingGemsOnView();
        droppingGems.addConnectingGemsTo(gemGrid);
        if(droppingGems.areAllAddedToGrid()){
            evaluateGemGrid();
        }
    }


    private void activateGridGravity(){
        future = executor.scheduleWithFixedDelay(this::applyGravityToGridGems, 0, GRAVITY_INTERVAL, TimeUnit.MILLISECONDS);
    }


    private void applyGravityToGridGems(){
        var fallenGems = gemGrid.gravityDropOnePosition();
        if(fallenGems.length == 0){
            cancelTask();
            evaluateGemGrid();
        }
        else{
            gameView.dropOnePosition(fallenGems);
        }
    }


    private void updateScore(int numberOfRemovedGems){

    }


    // public access for the sake of testing via direct calls from the game fragment
    public void evaluateGemGrid(){
       cancelTask();
       long[] markedGemsIds = evaluator.evaluateGemGrid();
       if(markedGemsIds.length > 0){
           gameView.wipeOut(markedGemsIds);
       }
       else{
           checkForHeightExceeded();
       }
    }


    public void end(){

    }


    private void checkForHeightExceeded(){
        if(gemGrid.exceedsMaxHeight()){
            gameOverAnimator.startGameOverSequence();
        }
        else{
            startDroppingGems(currentDropRate);
        }
    }



    public void create(){
    }


    private void printStackTrace(Exception e){
        log("Exception: " + e.getMessage());
    }


    private void cancelTask(){
        if(future != null && !future.isCancelled()){
            future.cancel(true);
        }
    }


    public void quit(){

    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }

}
