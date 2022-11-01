package com.jcrawleydev.gemsdrop.control;


public class ClickHandler {

    private final GemControls gemControls;
    private final int leftLimit, rotateLimit;
    private final int dropYThreshold;

    public ClickHandler(GemControls gemControls, int screenWidth, int screenHeight){
        this.gemControls = gemControls;
        leftLimit = screenWidth /3;
        rotateLimit = screenWidth /3 * 2;
        dropYThreshold = (screenHeight / 5) * 4;
    }


    public void click(int x, int y){
        if(x < leftLimit){
            moveLeft();
        }
        else if(x < rotateLimit && y < dropYThreshold){
            rotate();
        }
        else if(x > rotateLimit){
            moveRight();
        }
        else {
            quickDrop();
        }
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
        gemControls.enableQuickDrop();
    }

}
