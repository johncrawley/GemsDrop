package com.jcrawleydev.gemsdrop.service.game;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.service.game.utils.MovementChecker;
import com.jcrawleydev.gemsdrop.service.game.utils.RotationChecker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GemMover {


    private final MovementChecker movementChecker;
    private final RotationChecker rotationChecker;

    private DroppingGems droppingGems;
    private final AtomicBoolean isControlEnabled = new AtomicBoolean(true);
    private final AtomicBoolean isMovementEnabled = new AtomicBoolean(true);
    private final AtomicBoolean areFutureSyncMovementsAllowed = new AtomicBoolean(true);
    private DroppingGemsEvaluator droppingGemsEvaluator;
    private enum Movement { LEFT, RIGHT, ROTATE, DOWN }
    private final Queue<Movement> movementQueue = new ConcurrentLinkedQueue<>();


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
        isMovementEnabled.set(true);
    }


    public void disableControls(){
        isControlEnabled.set(false);
    }


    public void enableControls(){
        isControlEnabled.set(true);
    }


    public void rotateGems(){ syncUserMovement(Movement.ROTATE);}


    public void moveLeft(){
        syncUserMovement(Movement.LEFT);
    }


    public void moveRight(){
        syncUserMovement(Movement.RIGHT);
    }


    public void dropGems(){
        log("Entered dropGems()");
        syncMovement(Movement.DOWN);
    }

    private void log(String msg){
        System.out.println("^^^ GemMover: " + msg);
    }

    private void syncUserMovement(Movement movement){
        if (isControlEnabled.get()) {
            syncMovement(movement);
        }
    }


    private Runnable getMethodForMovement(Movement movement){
        return switch (movement){
            case ROTATE -> this::rotate;
            case LEFT -> this::left;
            case RIGHT -> this::right;
            case DOWN -> this::down;
        };
    }


    private synchronized void syncMovement(Movement movement){
        if(!areFutureSyncMovementsAllowed.get()){
            return;
        }
        if(!isMovementEnabled.get()){
            movementQueue.add(movement);
            return;
        }
      //  disableControls();
        isMovementEnabled.set(false);
        droppingGemsEvaluator.evaluateTouchingGems(droppingGems, getMethodForMovement(movement));
    }


    public void enableMovement(){
        isMovementEnabled.set(true);
    }


    public void syncNextQueuedMovement(){
        if(movementQueue.isEmpty()){
            return;
        }
        syncMovement(movementQueue.remove());
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

    private void down(){
        droppingGems.moveDown();
    }
}
