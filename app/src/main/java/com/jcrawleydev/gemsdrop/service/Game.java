package com.jcrawleydev.gemsdrop.service;

import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.service.grid.GemGridImpl;
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
    private Evaluator evaluator;

    private final ScheduledExecutorService gemDropExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> gemDropFuture;
    private final AtomicBoolean isStarted = new AtomicBoolean(false);

    private final GemGridImpl gemGrid = new GemGridImpl(gridProps);


    public void init(){
        evaluator = new Evaluator(gemGrid.getGemColumns(), gridProps.numberOfRows());
        droppingGems = new DroppingGems(gridProps);
        droppingGems.create();
    }



    public void rotateGems(){
        if(canRotate()){
            droppingGems.rotate();
            gameView.updateGems(droppingGems.get());
        }
    }


    public void moveLeft(){
        if(canMoveLeft()){
            droppingGems.moveLeft();
            gameView.updateGems(droppingGems.get());
        }
    }


    public void moveRight(){
        if(canMoveRight()){
            droppingGems.moveRight();
            updateGemsOnView();
        }
    }


    private void updateGemsOnView(){
        gameView.updateGems(droppingGems.get());
    }


    public boolean canMoveLeft(){

        return true;
    }


    public void create(){

        droppingGems.create();
        updateGemsOnView();
    }


    public boolean canMoveRight(){
        return true;
    }


    private boolean canRotate(){
        return  !droppingGems.isOrientationVertical()
                    || (droppingGems.getLeftmostColumn() > 0
                        || droppingGems.getRightmostColumn() < gridProps.numberOfColumns());
    }


    public void startGame(){
        if(isStarted.get()){
            return;
        }
        isStarted.set(true);
        startDroppingGems(500);
    }


    private void startDroppingGems(int dropRate){
        gemDropFuture = gemDropExecutor.scheduleWithFixedDelay(this::drop, 0, dropRate, TimeUnit.MILLISECONDS);
    }


    private Integer getThreadId(){
       return  android.os.Process.myTid();
    }


    private void log(String msg){
      //  System.out.println("^^^ Game: " + msg);
    }


    public void onDestroy(){
        if(gemDropFuture != null && !gemDropFuture.isCancelled()){
            gemDropFuture.cancel(false);
        }
        isStarted.set(false);
    }


    public void drop(){
        log("entered drop() thread id : " + getThreadId());
       int numberOfGemsPerDrop = 3;
       droppingGems.drop();
       updateGemsOnView();
       droppingGems.setGems(gemGrid.addGems(droppingGems.get(), droppingGems.isOrientationVertical()));
       if(droppingGems.isEmpty()){
           switchToEvalMode();
           return;
       }
       if(droppingGems.haveReducedInNumber()){
           log("drop() about to free fall");
           switchToFreeFallMode();
       }
    }


    private void switchToEvalMode(){
        log("Entered evalMode");
        long [] markedGemIds = new long[]{};
        gemDropFuture.cancel(true);
        try {
            evaluator.evaluate();
            markedGemIds = evaluator.evalAndDelete();
        }catch (RuntimeException e){
            e.printStackTrace();
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


    private void switchToFreeFallMode(){

    }


    private void switchToDropMode(){
        create();
        startDroppingGems(500);
    }


    public void quit(){

    }


    public void setView(GameView gameView){
        this.gameView = gameView;
    }

}
