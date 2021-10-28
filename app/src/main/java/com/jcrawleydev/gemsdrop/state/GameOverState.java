package com.jcrawleydev.gemsdrop.state;


import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.score.Score;

public class GameOverState implements GameState {

    private final Game game;
    private final View gameOverView;
    private TranslateAnimation dropInAnimation;
    private final int screenHeight;
    private final View titleView;
    private final Score score;
    private final TextView scoreTextView;
    private boolean hasClicked;
    boolean areClicksAllowed;


    public GameOverState(Game game, View gameOverView, View titleView, int screenHeight){
        this.game = game;
        this.gameOverView = gameOverView;
        this.titleView = titleView;
        this.screenHeight = screenHeight;
        setupGameOverAnimation();
        score = game.getScore();
        scoreTextView = gameOverView.findViewById(R.id.scoreText);
    }


    private void setupGameOverAnimation(){
        dropInAnimation = new TranslateAnimation(
                0,
                0,
                -screenHeight,
                0);
        dropInAnimation.setDuration(500);
        dropInAnimation.setFillAfter(true);

        dropInAnimation.setAnimationListener(new Animation.AnimationListener(){
            @Override
            public void onAnimationStart(Animation arg0) {
            }
            @Override
            public void onAnimationRepeat(Animation arg0) {
            }
            @Override
            public void onAnimationEnd(Animation arg0) {
                areClicksAllowed = true;
            }
        });

    }


    @Override
    public void start(){
        hasClicked = false;
        areClicksAllowed = false;
        final Handler startAnimationHandler = new Handler(Looper.getMainLooper());
        startAnimationHandler.postDelayed(()->{
            gameOverView.setVisibility(View.VISIBLE);
            gameOverView.findViewById(R.id.scoreText);
            String scoreStr = String.valueOf(score.get());
            scoreTextView.setText(scoreStr);
            score.clear();
            gameOverView.startAnimation(dropInAnimation);
        }, 1000);
    }


    @Override
    public void stop(){

    }


    @Override
    public void click(int x, int y) {
        if(!areClicksAllowed || hasClicked){
            return;
        }
        hasClicked = true;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()->{
            gameOverView.setVisibility(View.GONE);
            gameOverView.clearAnimation();
            titleView.clearAnimation();
            titleView.startAnimation(dropInAnimation);
           // titleView.animate().translationX(0).translationY(0).setDuration(2000);
            game.loadTitleState();
        }, 100);
    }

}
