package com.jcrawleydev.gemsdrop.state;

import android.view.View;
import android.view.animation.TranslateAnimation;

import com.jcrawleydev.gemsdrop.Game;

public class TitleState implements GameState {

    private final Game game;
    private final View titleView;


    public TitleState(Game game, View titleView){
        this.game = game;
        this.titleView = titleView;
    }


    public void start(){

    }


    public void stop(){

    }

    @Override
    public void click(int x, int y) {

        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                titleView.getHeight());
        animate.setDuration(500);
        animate.setFillAfter(true);
        titleView.startAnimation(animate);

    }
}
