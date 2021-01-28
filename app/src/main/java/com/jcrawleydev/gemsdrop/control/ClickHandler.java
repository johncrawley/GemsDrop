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
        log("Entered click() x: " + x);
        if(x < leftLimit){
            log("moving left...");
            moveLeft();
            return;
        }
        if(x < rotateLimit){
            log("rotating...");
            rotate();
            return;
        }
        log("moving right...");
        moveRight();

    }

    private void log(String msg){
        System.out.println("ClickHandler  " + msg);
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
