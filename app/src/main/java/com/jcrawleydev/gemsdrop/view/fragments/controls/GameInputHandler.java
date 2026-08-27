package com.jcrawleydev.gemsdrop.view.fragments.controls;

import android.view.ViewGroup;

public class GameInputHandler {

    private GameInputHandler(){}

    public static void handleInput(float x, float y, ViewGroup gamePane, Controllable controllable){
        if(controllable == null){
            return;
        }
        int height = gamePane.getMeasuredHeight();
        int width = gamePane.getMeasuredWidth();
        if( y > (height / 8f) * 7){
            controllable.moveDown();
            return;
        }
        if(x < width / 3f){
            controllable.moveLeft();
            return;
        }
        if(x < width / 1.5f ){
           controllable.rotateGems();
            return;
        }
        controllable.moveRight();
    }


}
