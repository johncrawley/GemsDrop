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
    private final GemGrid gemGrid;
    private final GridProps gridProps;
    private DroppingGems droppingGems;
    private final AtomicBoolean isControlEnabled = new AtomicBoolean(true);


    public GemMover(Game game, GemGrid gemGrid, GridProps gridProps){

        this.game = game;
        this.gemGrid = gemGrid;
        this.gridProps = gridProps;

        movementChecker = new MovementChecker(gemGrid, gridProps);
        rotationChecker = new RotationChecker(gemGrid, gridProps);
    }

    public void setDroppingGems(DroppingGems droppingGems){
        this.droppingGems = droppingGems;
    }


    public void disableControls(){
        isControlEnabled.set(false);
    }



    public void rotateGems(){ syncUserMovement(this::rotate);}


    public void moveLeft(){
        syncUserMovement(this::left);
    }


    public void moveRight(){
        syncUserMovement(this::right);
    }


    public void moveUp(){
        syncUserMovement(this::up);
    }


    public void moveDown(){syncUserMovement(this::down);}


    public void dropGems(){
        syncMovement(() -> droppingGems.moveDown());
    }


    private void syncUserMovement(Runnable runnable){
        if (isControlEnabled.get()) {
            syncMovement(runnable);
        }
    }


    private synchronized void syncMovement(Runnable runnable){
        isControlEnabled.set(false);
        boolean wereAnyGemsTouching = evaluateTouchingGems();

        if(wereAnyGemsTouching){
            return;
        }
        runnable.run();
        game.updateGemsOnView();
        isControlEnabled.set(true);
    }


    public void enableControls(){
        isControlEnabled.set(true);
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
        dropGems();
        //  switchToFreeFallMode();
    }


    private boolean evaluateTouchingGems(){
        droppingGems.addConnectingGemsTo(gemGrid);
        isControlEnabled.set(!droppingGems.areAnyAddedToGrid());
        if(droppingGems.areAllAddedToGrid()){
            game.evaluateGemGrid();
            return true;
        }
        if(droppingGems.areAnyAddedToGrid()){
            game.startGemFreeFall();
            return true;
        }
        return false;
    }


}
