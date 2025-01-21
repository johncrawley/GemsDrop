package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.utils.MovementChecker;
import com.jcrawleydev.gemsdrop.service.game.utils.RotationChecker;

import java.util.concurrent.atomic.AtomicBoolean;

public class GemMover {


    private final MovementChecker movementChecker;
    private final RotationChecker rotationChecker;
    private final Game game;

    private DroppingGems droppingGems;
    private final AtomicBoolean isControlEnabled = new AtomicBoolean(true);
    private final AtomicBoolean areFutureSyncMovementsAllowed = new AtomicBoolean(true);


    public GemMover(Game game, GemGrid gemGrid, GridProps gridProps){
        this.game = game;
        movementChecker = new MovementChecker(gemGrid, gridProps);
        rotationChecker = new RotationChecker(gemGrid, gridProps);
    }


    public void setDroppingGems(DroppingGems droppingGems){
        this.droppingGems = droppingGems;
        areFutureSyncMovementsAllowed.set(true);
        isControlEnabled.set(true);
    }


    public void disableControls(){
        isControlEnabled.set(false);
    }


    private void enableControls(){
        isControlEnabled.set(true);
    }


    public void rotateGems(){ syncUserMovement(this::rotate);}


    public void moveLeft(){
        syncUserMovement(this::left);
    }


    public void moveRight(){
        syncUserMovement(this::right);
    }


    public void dropGems(){
        syncMovement(() -> droppingGems.moveDown());
    }


    private void syncUserMovement(Runnable runnable){
        if (isControlEnabled.get()) {
            syncMovement(runnable);
        }
    }


    private void log(String msg){
        System.out.println("^^^ GemMover: " + msg);
    }


    private synchronized void syncMovement(Runnable runnable){
        //log("syncMovement() are futureSyncMovementsAllowed: "  + areFutureSyncMovementsAllowed.get());
        if(!areFutureSyncMovementsAllowed.get()){
            return;
        }
        disableControls();
        boolean wereAnyGemsAdded = game.evaluateTouchingGems();

        if(wereAnyGemsAdded){
          //  cancelFutureSyncMovements();
            return;
        }
        runnable.run();
        game.updateDroppingGemsOnView();
        enableControls();
    }


    private void cancelFutureMovements(){
        log("Entered cancelFutureSyncMovements()");
        areFutureSyncMovementsAllowed.set(false);
    }


    private void rotate(){
        if(rotationChecker.canRotate(droppingGems)){
            droppingGems.rotate();
        }
    }


    private void left(){
        if(movementChecker.canMoveLeft(droppingGems)){
            droppingGems.moveLeft();
        }
    }


    private void right(){
        if(movementChecker.canMoveRight(droppingGems)){
            droppingGems.moveRight();
        }
    }


    private void up(){
        droppingGems.moveUp();
    }


    private void down(){
        if(movementChecker.canMoveDown(droppingGems)){
            droppingGems.moveDown();
        }
        //  switchToFreeFallMode();
    }



}
