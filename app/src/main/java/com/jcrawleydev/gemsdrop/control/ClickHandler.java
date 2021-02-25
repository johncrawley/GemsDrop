package com.jcrawleydev.gemsdrop.control;


public class ClickHandler {

    private GemControls gemControls;
    private int leftLimit, rotateLimit;
    private int dropYThreshold;

    public ClickHandler(GemControls gemControls, int screenWidth, int screenHeight){
        this.gemControls = gemControls;
        leftLimit = screenWidth /3;
        rotateLimit = screenWidth /3 * 2;
        dropYThreshold = (screenHeight / 6) * 5;
    }

    public void click(int x, int y){
        if(y > dropYThreshold){
            quickDrop();
            return;
        }

        if(x < leftLimit){
            moveLeft();
            return;
        }
        if(x < rotateLimit){
            rotate();
            return;
        }
        moveRight();

    }


    private void moveLeft(){
        gemControls.moveLeft();
    }


    private void moveRight(){
     gemControls.moveRight();
    }

    private void rotate(){
        gemControls.rotate();
    }

    private void quickDrop(){
        gemControls.deactivate();
        gemControls.engageQuickDrop();
    }

}
