package com.jcrawleydev.gemsdrop.game;

import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.DroppingGemsEvaluator;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.utils.MovementChecker;
import com.jcrawleydev.gemsdrop.game.utils.RotationChecker;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class GemMover {


    private MovementChecker movementChecker;
    private RotationChecker rotationChecker;

    private DroppingGems droppingGems;
    private final AtomicBoolean isControlEnabled = new AtomicBoolean(true);
    private final AtomicBoolean isMovementEnabled = new AtomicBoolean(true);
    private DroppingGemsEvaluator droppingGemsEvaluator;
    private enum Movement { LEFT, RIGHT, ROTATE, DOWN }
    private final Queue<Movement> movementQueue = new ConcurrentLinkedQueue<>();


    public void init(GemGrid gemGrid, GridProps gridProps, DroppingGemsEvaluator droppingGemsEvaluator){
        movementChecker = new MovementChecker(gemGrid, gridProps);
        rotationChecker = new RotationChecker(gemGrid, gridProps);
        this.droppingGemsEvaluator = droppingGemsEvaluator;
        droppingGemsEvaluator.setGemMover(this);
    }


    public void setDroppingGems(DroppingGems droppingGems){
        this.droppingGems = droppingGems;
        movementQueue.clear();
        enableControls();
        enableMovement();
    }


    public void disableControls(){
        isControlEnabled.set(false);
    }


    public boolean areControlsDisabled(){
        return !isControlEnabled.get();
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
        syncMovement(Movement.DOWN);
    }


    private void syncUserMovement(Movement movement){
        if(droppingGems.areInInitialPosition()){
            return;
        }
        if (isControlEnabled.get()) {
            syncMovement(movement);
        }
    }


    public void cancelQueuedMovements(){
        movementQueue.clear();
    }

    public void disableMovement(){
        isMovementEnabled.set(false);
    }


    public void enableMovement(){
        isMovementEnabled.set(true);
    }


    public void attemptNextQueuedMovement(){
        if(movementQueue.isEmpty()){
            return;
        }
        syncMovement(movementQueue.remove());
    }


    private synchronized void syncMovement(Movement movement){
        if(!isMovementEnabled.get()){
            movementQueue.add(movement);
            if(movementQueue.size() < 4){
                return;
            }
        }
        disableMovement();
        droppingGemsEvaluator.evaluateTouchingGems(droppingGems, getMethodForMovement(movement));
    }


    private Runnable getMethodForMovement(Movement movement){
        return switch (movement){
            case ROTATE -> this::rotate;
            case LEFT -> this::left;
            case RIGHT -> this::right;
            case DOWN -> this::down;
        };
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
