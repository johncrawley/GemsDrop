package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.graphics.PointF;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.game.Game;
import com.jcrawleydev.gemsdrop.service.GameService;

import java.util.function.Consumer;

public class GameInputHandler {

    private GameInputHandler(){}

    public static void handleInput(PointF p, ViewGroup gamePane, Game game){
        if(game == null){
            return;
        }
        //log("handleInput(" + (int)x + "," +  (int)y + ")" + " gemContainer AbsoluteY: "+ gamePane.getY() + " height: " + gamePane.getMeasuredHeight());
        int height = gamePane.getMeasuredHeight();
        int width = gamePane.getMeasuredWidth();
        if( p.y < height/4f){
            game.moveUp();
            return;
        }
        if( p.y > (height / 3f) * 2){
            game.moveDown();
            return;
        }
        if(p.x < width / 3f){
            game.moveLeft();
            return;
        }
        if(p.x < width / 1.5f ){
           game.rotateGems();
            return;
        }
        game.moveRight();
    }


}
