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
        cancelDrop();
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
        cancelDrop();
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


    private void activateGridFreeFall(){


    }


    private void freeFallGridGems(){
        var fallenGems = gemGrid.freeFall();
        gameView.freeFall(fallenGems);
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

    private void cancelDrop(){
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
