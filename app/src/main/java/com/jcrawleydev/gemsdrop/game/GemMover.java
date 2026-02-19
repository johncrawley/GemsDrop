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
    private final AtomicBoolean isFirstDrop = new AtomicBoolean(false);
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
        disableControls();
        enableMovement();
        isFirstDrop.set(true);
    }


    public void disableControls(){
        isControlEnabled.set(false);
    }


    public boolean areControlsDisabled(){
        return !isControlEnabled.get();
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
        if(isFirstDrop.get()){
            isFirstDrop.set(false);
            isControlEnabled.set(true);
        }
    }


    private void log(String msg){
        System.out.println("^^^ GemMover: " + msg);
    }


    private void syncUserMovement(Movement movement){
        if(isControlEnabled.get()){
            syncMovement(movement);
        }
    }


    public void cancelQueuedMovements(){
        movementQueue.clear();
    }


    public void attemptNextQueuedMovement(){
        if(!movementQueue.isEmpty()){
            syncMovement(movementQueue.remove());
        }
    }


    private synchronized void syncMovement(Movement movement){
        if(droppingGems == null){
            log("dropping gems are null! Won't proceed with attempted movement!");
            return;
        }
        if(!isMovementEnabled.get()){
            log("entered syncMovement() control is disabled, adding to queue");
            movementQueue.add(movement);
        }
        disableMovement();
        droppingGemsEvaluator.evaluateTouchingGems(droppingGems, getMethodForMovement(movement));
    }


    public void disableMovement(){
        isMovementEnabled.set(false);
    }


    public void enableMovement(){
        isMovementEnabled.set(true);
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
