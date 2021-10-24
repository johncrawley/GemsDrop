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

        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGridTransparentView.setDimensions(width - gemGridBorder, height);
        scoreTransparentView.setDimensions(width, height);
        borderView.setDimensions(width, height);

        gemGroupTransparentView.setOnTouchListener(this);
        gemGridTransparentView.setOnTouchListener(this);
        View titleView = findViewById(R.id.titleViewInclude);

        game = new Game(getApplicationContext(), width, height, gemWidth, gemGridBorder, numberOColumns, scoreBarHeight, floorY, titleView);
        BitmapLoader bitmapLoader = new BitmapLoader(this, gemWidth);
        game.initGemGridView(gemGridTransparentView);
        game.initGemGroupLayer(gemGroupTransparentView, bitmapLoader);
        game.initScoreView(scoreTransparentView);
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
        numberOColumns = getResources().getInteger(R.integer.number_of_columns);
        int maxRows = getResources().getInteger(R.integer.maximum_rows);
        gemGridBorder = (width - (gemWidth * numberOColumns))/2;
        floorY = height - (Math.min(gemWidth,gemGridBorder));
        scoreBarHeight =  floorY - ((gemWidth * maxRows));
        System.out.println("^^^** MainActivity.assignScreenDimensions() scoreBarHeight: " + scoreBarHeight);
    }


    private void assignGemWidth(int width, int height){
        int shortestDimension = Math.min(width, height);
        int numberOfColumns = getResources().getInteger(R.integer.number_of_columns);
        this.gemWidth = shortestDimension / (numberOfColumns + 1);
    }

}
