package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.TransparentView;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {


    private int height, width;
    private Game game;
    private int gemWidth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assignScreenDimensions();
        TransparentView gemGroupTransparentView = findViewById(R.id.gemGroupView);
        TransparentView gemGridTransparentView = findViewById(R.id.gemGridView);
        TransparentView scoreTransparentView = findViewById(R.id.scoreView);
        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGridTransparentView.setDimensions(width, height);
        scoreTransparentView.setDimensions(width, height);
        gemGroupTransparentView.setOnTouchListener(this);
        gemGridTransparentView.setOnTouchListener(this);
        game = new Game(width, height, gemWidth);
        BitmapLoader bitmapLoader = new BitmapLoader(this, gemWidth);
        game.initGemGridView(gemGridTransparentView);
        game.initGemGroupView(gemGroupTransparentView, bitmapLoader);
        game.initScoreView(scoreTransparentView, bitmapLoader);
        game.init();
    }


    public boolean onTouch(View v, MotionEvent e){
        if(e.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }
        int x = (int)e.getX();
        int y = (int)e.getY();
        game.click(x, y);
        return true;
    }


    private void assignScreenDimensions(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        assignGemWidth(width, height);
    }

    private void assignGemWidth(int width, int height){
        int longestDimen = Math.max(width, height);
        this.gemWidth = longestDimen / 14;
    }

}
