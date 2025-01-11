package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.service.game.grid.GridEvaluator;
import com.jcrawleydev.gemsdrop.service.game.utils.MovementChecker;
import com.jcrawleydev.gemsdrop.service.game.utils.RotationChecker;
import com.jcrawleydev.gemsdrop.view.GameView;

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


     */

public class Game {

    private final GridProps gridProps = new GridProps(14, 7, 2);
    private DroppingGems droppingGems;

    private GameView gameView;
    private GridEvaluator evaluator;
    private MovementChecker movementChecker;
    private RotationChecker rotationChecker;

    private final ScheduledExecutorService gemDropExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gemDropFuture;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);
    private final AtomicBoolean isControlEnabled = new AtomicBoolean(true);

    private final GemGridImpl gemGrid = new GemGridImpl(gridProps);


    public void init(){
        log("entered init()");
        evaluator = new GridEvaluator(gemGrid.getGemColumns(), gridProps.numberOfRows());
        movementChecker = new MovementChecker(gemGrid, gridProps);
        rotationChecker = new RotationChecker(gemGrid, gridProps);
        createGems();
    }


    public void rotateGems(){
        log("entered rotateGems()");
        syncMovement(this::rotate);
    }


    public void moveLeft(){
        syncMovement(this::left);
    }


    public void moveRight(){
        syncMovement(this::right);
    }


    public void moveUp(){
        syncMovement(this::up);
    }


    public void createGems(){
        droppingGems = new DroppingGems(gridProps);
        droppingGems.create();
        isControlEnabled.set(true);
        printGemGridColumnHeights();
    }


    public void destroyGems(){

    }


    private void printGemGridColumnHeights(){
        String colHeights = gemGrid.getColumnHeights().stream().map(String::valueOf).reduce("", (total, colHeight) -> total + " " + colHeight);
        log("printGemGridColumnHeights() : " + colHeights);
    }


    public void moveDown(){
        log("Entered moveDown()");
        syncMovement(this::down);
    }


    private void rotate(){
        if(rotationChecker.canRotate(droppingGems)){
            droppingGems.rotate();
            gameView.updateGems(droppingGems.get());
        }
    }


    private void left(){
        log("Entering left");
        if(movementChecker.canMoveLeft(droppingGems)){
            droppingGems.moveLeft();
            gameView.updateGems(droppingGems.get());
        }
        log("Exiting left!");
    }


    private void right(){
        log("Entering right()");
        if(movementChecker.canMoveRight(droppingGems)){
            droppingGems.moveRight();
            updateGemsOnView();
        }
        log("Exiting right()!");
    }


    private void up(){
        log("Entering up()");
        droppingGems.moveUp();
        updateGemsOnView();
        log("Exiting up()!");
    }


    private void down(){
       dropGems();
      //  switchToFreeFallMode();
    }


    public void downTEST(){
        log("Entering down()");
        if(movementChecker.canMoveDown(droppingGems)){
            droppingGems.moveDown();

            updateGemsOnView();
        }
        log("Exiting down()!");
    }


    private synchronized void syncMovement(Runnable runnable){
        runnable.run();
        log("Exiting syncMovement!");
    }


    private synchronized void syncUserMovement(Runnable runnable){
        if (isControlEnabled.get()) {
            runnable.run();
        }
    }


    private void updateGemsOnView(){
        gameView.updateGems(droppingGems.get());
    }


    public void create(){
        droppingGems.create();
        updateGemsOnView();
    }


    public void startGame(){
        if(isStarted.get()){
            return;
        }
        isStarted.set(true);

        //startDroppingGems(500);
    }


    private void startDroppingGems(int dropRate){
        gemDropFuture = gemDropExecutor.scheduleWithFixedDelay(this::drop, 0, dropRate, TimeUnit.MILLISECONDS);
    }


    private Integer getThreadId(){
       return  android.os.Process.myTid();
    }


    private void log(String msg){
       System.out.println("^^^ Game: " + msg);
    }


    public void onDestroy(){
        cancelTask();
        isStarted.set(false);
    }


    private void drop(){
        syncMovement(this::dropGems);
    }


    private void dropGems(){
        log("entered dropGems()");
        droppingGems.moveDown();
        updateGemsOnView();
        droppingGems.addConnectingGemsTo(gemGrid);
        isControlEnabled.set(!droppingGems.areAnyAddedToGrid());
        if(droppingGems.areAllAddedToGrid()){
            //switchToEvalMode();
            createGems();
            return;
        }
        if(droppingGems.areAnyAddedToGrid()){
           // switchToFreeFallMode();
        }
       log("Exiting dropGems()");
    }


    private void switchToEvalMode(){
        log("Entered evalMode");
        long [] markedGemIds = new long[]{};
        cancelTask();
        try {
            markedGemIds = evaluator.evaluateGemGrid();
        }catch (RuntimeException e){
            printStackTrace(e);
        }
       log("switchToEvalMode() number of markedGemIds: " + markedGemIds.length);
        if(markedGemIds.length == 0){
            log("markedGemIds length is 0, switching back to drop mode");
            switchToDropMode();
            return;
        }
        log("wiping out ids on game view");
        gameView.wipeOut(markedGemIds);
    }


    public void onGemRemovalAnimationDone(){
        int initialGemCount = gemGrid.gemCount();
        gemGrid.removeMarkedGems();
        int removedGemsCount = initialGemCount - gemGrid.gemCount();
        updateScore(removedGemsCount);
        if(removedGemsCount > 0){
            activateGridFreeFall();
        }
    }


    private void freeFallGems(){


    }


    private void activateGridFreeFall(){
        int freeFallDelay = 300;
        gemDropFuture = gemDropExecutor.scheduleWithFixedDelay(this::freeFallGridGems, 0, freeFallDelay, TimeUnit.MILLISECONDS);
    }


    private void lockControls(){
        isControlEnabled.set(false);
    }


    private void freeFallGridGems(){
        var fallenGems = gemGrid.freeFallOnePosition();
        if(fallenGems.length == 0){
            cancelTask();
        }
        else{
            gameView.freeFall(fallenGems);
        }
    }


    private void updateScore(int numberOfRemovedGems){

    }


    // public access for the sake of testing via direct calls from the game fragment
    public void evalGems(){
       long[] markedGemsIds = evaluator.evaluateGemGrid();
       gameView.wipeOut(markedGemsIds);
    }


    private void printStackTrace(Exception e){
        log("Exception: " + e.getMessage());
    }

    private void cancelTask(){
        if(gemDropFuture != null && !gemDropFuture.isCancelled()){
            gemDropFuture.cancel(true);
        }
    }


    /* should be the same for quick drop (when the user presses "down")
     and when one or two gems are added to the grid
     */
    private void switchToFreeFallMode(){
        // cancel current drop task
        // disable user controls
        // start drop task again, but with faster time
    }


    private void switchToDropMode(){
        create();
        //startDroppingGems(500);
    }


    public void quit(){

    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }

}
