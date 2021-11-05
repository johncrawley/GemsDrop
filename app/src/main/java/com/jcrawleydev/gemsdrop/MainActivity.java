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
    private int numberOColumns;
    private int scoreBarHeight;
    private int floorY;


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
        TransparentView titleBackgroundView = findViewById(R.id.titleBackgroundView);
        TransparentView gameOverBackgroundView = findViewById(R.id.gameOverBackgroundView);

        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGridTransparentView.setDimensions(width - gemGridBorder, height);
        scoreTransparentView.setDimensions(width, height);
        borderView.setDimensions(width, height);
        titleBackgroundView.setDimensions(width, height);
        gameOverBackgroundView.setDimensions(width, height);

        gemGroupTransparentView.setOnTouchListener(this);
        gemGridTransparentView.setOnTouchListener(this);
        View titleView = findViewById(R.id.titleViewInclude);
        View gameOverView = findViewById(R.id.gameOverViewInclude);

        BitmapLoader bitmapLoader = new BitmapLoader(this, gemWidth);
        game = new Game(this, bitmapLoader, width, height, gemWidth, gemGridBorder, numberOColumns, scoreBarHeight, floorY, titleView, gameOverView);
        game.initGemGridView(gemGridTransparentView);
        game.initGemGroupLayer(gemGroupTransparentView, bitmapLoader);
        game.initScoreboardLayer(scoreTransparentView);
        game.initBorder(borderView, bitmapLoader);
        game.initBorder(titleBackgroundView, bitmapLoader);
        game.initBorder(gameOverBackgroundView, bitmapLoader);
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

        numberOColumns = getResources().getInteger(R.integer.number_of_columns);
        scoreBarHeight = height / 15;
        int bottomBorderHeight = height / 18;
        floorY = height - bottomBorderHeight;
        int maxRows = getResources().getInteger(R.integer.maximum_rows);
        gemWidth = (height - (scoreBarHeight + bottomBorderHeight)) / maxRows;
        gemGridBorder = (this.width - (gemWidth * numberOColumns)) / 2;
    }


}
