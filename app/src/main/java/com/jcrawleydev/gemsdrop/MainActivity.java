package com.jcrawleydev.gemsdrop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.TransparentView;


public class MainActivity extends Activity implements View.OnTouchListener {

    private int height, width;
    private Game game;
    private int gemWidth;
    private int gemGridBorder;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assignScreenDimensions();
        TransparentView gemGroupTransparentView = findViewById(R.id.gemGroupView);
        TransparentView gemGridTransparentView = findViewById(R.id.gemGridView);
        TransparentView scoreTransparentView = findViewById(R.id.scoreView);
        TransparentView borderView = findViewById(R.id.borderView);

        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGridTransparentView.setDimensions(width - gemGridBorder, height);
        scoreTransparentView.setDimensions(width, height);
        borderView.setDimensions(width, height);

        gemGroupTransparentView.setOnTouchListener(this);
        gemGridTransparentView.setOnTouchListener(this);

        game = new Game(getApplicationContext(), width, height, gemWidth);
        BitmapLoader bitmapLoader = new BitmapLoader(this, gemWidth);
        game.initGemGridView(gemGridTransparentView);
        game.initGemGroupView(gemGroupTransparentView, bitmapLoader);
        game.initScoreView(scoreTransparentView, bitmapLoader);
        game.initBorder(borderView, bitmapLoader);
        game.init();
    }


    @SuppressLint("ClickableViewAccessibility")
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
        gemGridBorder = gemWidth /2;
    }


    private void assignGemWidth(int width, int height){
        int shortestDimension = Math.min(width, height);
        int numberOfColumns = getResources().getInteger(R.integer.number_of_columns);
        this.gemWidth = shortestDimension / (numberOfColumns + 1);
    }

}
