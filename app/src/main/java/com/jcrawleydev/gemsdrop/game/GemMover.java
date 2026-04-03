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
    private final DroppingGemsEvaluator droppingGemsEvaluator;
    public enum Movement { LEFT, RIGHT, ROTATE, DOWN }
    private final Queue<Movement> movementQueue = new ConcurrentLinkedQueue<>();
    private final Game game;


    public GemMover(Game game, DroppingGemsEvaluator droppingGemsEvaluator){
        this.game = game;
        this.droppingGemsEvaluator = droppingGemsEvaluator;
        droppingGemsEvaluator.setGemMover(this);
    }

    public void init(GemGrid gemGrid, GridProps gridProps){
        movementChecker = new MovementChecker(gemGrid, gridProps);
        rotationChecker = new RotationChecker(gemGrid, gridProps);
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


    public synchronized void rotateGems(){
       boolean canMove = evaluateDroppingGemsBefore(Movement.ROTATE);
        if(canMove){
            rotate();
            afterUserMovement();
        }
    }


    public synchronized void moveLeft(){
        boolean canMove = evaluateDroppingGemsBefore(Movement.LEFT);
        if(canMove){
            left();
            afterUserMovement();
        }
    }


    public synchronized void moveRight(){
        boolean canMove = evaluateDroppingGemsBefore(Movement.RIGHT);
        if(canMove){
            right();
            afterUserMovement();
        }
    }


    public synchronized void dropGems(){
        boolean canMove = evaluateDroppingGemsBefore(Movement.DOWN);
        if(canMove){
            down();
            afterUserMovement();
        }
        if(isFirstDrop.get()){
            isFirstDrop.set(false);
            isControlEnabled.set(true);
        }
    }


    private boolean evaluateDroppingGemsBefore(Movement movement){
        if(droppingGems == null){
            return false;
        }
        if(!isMovementEnabled.get()){
            queue(movement);
            return false;
        }
        disableMovement();
        var haveGemsBeenAdded = droppingGemsEvaluator.evaluate(droppingGems);
        return !haveGemsBeenAdded;
    }


    void afterUserMovement(){
        game.updateDroppingGemsOnView();
        enableMovement();
        attemptNextQueuedMovement();
    }


    private void queue(Movement movement){
        if(movementQueue.size() > 5){
            movementQueue.remove();
        }
        movementQueue.add(movement);
    }



    private void log(String msg){
        System.out.println("^^^ GemMover: " + msg);
    }


    public void cancelQueuedMovements(){
        movementQueue.clear();
    }


    public void attemptNextQueuedMovement(){
        if(!movementQueue.isEmpty()){
            var movement = movementQueue.remove();
            if(movement == Movement.LEFT){
                moveLeft();
            } else if (movement == Movement.RIGHT) {
                moveRight();
            }else if (movement == Movement.DOWN) {
                dropGems();
            }else{
                rotate();
            }
        }
    }


    public void disableMovement(){
        isMovementEnabled.set(false);
    }


    public void enableMovement(){
        isMovementEnabled.set(true);
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
