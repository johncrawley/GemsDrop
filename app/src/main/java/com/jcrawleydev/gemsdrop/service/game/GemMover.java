package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.utils.MovementChecker;
import com.jcrawleydev.gemsdrop.service.game.utils.RotationChecker;

import java.util.concurrent.atomic.AtomicBoolean;

public class GemMover {


    private final MovementChecker movementChecker;
    private final RotationChecker rotationChecker;

    private DroppingGems droppingGems;
    private final AtomicBoolean isControlEnabled = new AtomicBoolean(true);
    private final AtomicBoolean areFutureSyncMovementsAllowed = new AtomicBoolean(true);
    private DroppingGemsEvaluator droppingGemsEvaluator;


    public GemMover(GemGrid gemGrid, GridProps gridProps){
        movementChecker = new MovementChecker(gemGrid, gridProps);
        rotationChecker = new RotationChecker(gemGrid, gridProps);
    }

    public void setDroppingGemsEvaluator(DroppingGemsEvaluator droppingGemsEvaluator){
        this.droppingGemsEvaluator = droppingGemsEvaluator;
        droppingGemsEvaluator.setGemMover(this);
    }


    public void setDroppingGems(DroppingGems droppingGems){
        this.droppingGems = droppingGems;
        areFutureSyncMovementsAllowed.set(true);
        isControlEnabled.set(true);
    }


    public void disableControls(){
        isControlEnabled.set(false);
    }


    public void enableControls(){
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


    private synchronized void syncMovement(Runnable runnable){
        if(!areFutureSyncMovementsAllowed.get()){
            return;
        }
        disableControls();
        droppingGemsEvaluator.evaluateTouchingGems(droppingGems, runnable);
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

}
