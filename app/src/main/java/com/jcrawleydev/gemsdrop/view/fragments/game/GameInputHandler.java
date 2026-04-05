package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.graphics.PointF;
import android.view.ViewGroup;

import com.jcrawleydev.gemsdrop.game.Game;

public class GameInputHandler {

    private GameInputHandler(){}

    public static void handleInput(float x, float y, ViewGroup gamePane, Game game){
        if(game == null){
            return;
        }
        int height = gamePane.getMeasuredHeight();
        int width = gamePane.getMeasuredWidth();
        if( y > (height / 8f) * 7){
            game.moveDown();
            return;
        }
        if(x < width / 3f){
            game.moveLeft();
            return;
        }
        if(x < width / 1.5f ){
           game.rotateGems();
            return;
        }
        game.moveRight();
    }


}
