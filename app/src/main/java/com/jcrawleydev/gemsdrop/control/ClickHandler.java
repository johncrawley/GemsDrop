package com.jcrawleydev.gemsdrop.control;

public class ClickHandler {

    private GemControls gemControls;
    private int leftLimit, rotateLimit;

    public ClickHandler(GemControls gemControls, int screenWidth){
        this.gemControls = gemControls;
        leftLimit = screenWidth /3;
        rotateLimit = screenWidth /3 * 2;
    }

    public void click(int x){
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

}
