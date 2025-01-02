package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.service.GameService;

import java.util.function.Consumer;

public class GameInputHandler {

    private GameFragment gameFragment;

    public GameInputHandler(GameFragment gameFragment){
        this.gameFragment = gameFragment;
    }

    public void onDestroy(){
        gameFragment = null;
    }

    public void handleInput(float x, float y, ViewGroup gamePane){

        log("handleInput(" + (int)x + "," +  (int)y + ")" + " gemContainer AbsoluteY: "+ gamePane.getY() + " height: " + gamePane.getMeasuredHeight());
        int height = gamePane.getMeasuredHeight();
        int width = gamePane.getMeasuredWidth();
        if( y < height/4f){
            log("handleInput() move up!");
            moveUp();
            return;
        }
        if( y > (height / 3f) * 2){
            log("handleInput() move down!");
            moveDown();
            return;
        }
        if(x < width / 3f){
            log("handleInput() move left!");
            moveLeft();
            return;
        }
        if(x < width / 1.5f ){
            log("handleInput() rotate!");
            rotateGems();
            return;
        }
        log("handleInput() move right!!");
        moveRight();
    }

    private void log(String msg){
        System.out.println("^^^ GameInputHandler: " + msg);
    }


    private void moveLeft(){
        runOnService(GameService::moveLeft);
    }


    private void moveRight(){
        runOnService(GameService::moveRight);
    }


    private void moveUp(){
        runOnService(GameService::moveUp);
    }


    private void moveDown(){
        runOnService(GameService::moveDown);
    }


    private void runOnService(Consumer<GameService> consumer){
        if(gameFragment != null){
            gameFragment.getService().ifPresent(consumer);
        }
    }


    private void rotateGems(){
        log("Entered rotateGems()");
        runOnService(GameService::rotateGems);
    }


}
