package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.score.Score;


import java.util.Collections;

public class ScoreView implements TextItem, DrawableItem {

    private final TransparentView transparentView;
    private final Score score;
    private int scoreBarHeight, scoreBarWidth;
    private int textX, textY;
    private float textSize;
    private final Context context;
    private final int height, width;

    public ScoreView(Context context, View view, Score score, BitmapLoader bitmapLoader, int width, int height){
        this.context = context;
        this.transparentView = (TransparentView) view;
        this.score = score;
        this.width = width;
        this.height = height;

        transparentView.addDrawableItem(this);
        setupDimensions(view);

    }


    private void setupDimensions(View view){

        textSize = (float) height / getInt(R.integer.score_board_text_height_ratio);
        scoreBarWidth = width;
        scoreBarHeight = height / getInt(R.integer.score_board_height_ratio);;
        textX = scoreBarWidth /5;
        textY = scoreBarHeight - (height / getInt(R.integer.score_board_text_position_y_offset));
        log("setupDimensions(), Scorebar text X and Y: " + textX + "," + textY + " ... scoreBarWidth: " + scoreBarWidth);


    }

    private int getInt(int resId){
        return context.getResources().getInteger(resId);
    }

    private void log(String msg){
        System.out.println("^^^ ScoreView : " + msg);
    }


    public Score getScore(){
        return this.score;
    }

    @Override
    public String getText(){
        return context.getString(R.string.score) + " " + score.get();
    }

    
    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public int getX(){
        return 100;
    }

    @Override
    public int getY(){
        return 100;
    }

    @Override
    public boolean isVisible() {
        return false;
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        Rect scoreBoardBackgroundRect = new Rect(0,0, scoreBarWidth, scoreBarHeight);
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(scoreBoardBackgroundRect, paint);
        paint.setColor(Color.GRAY);
        paint.setTextSize(textSize);
        canvas.drawText(getText(), textX, textY, paint);
    }


    @Override
    public int getColor(){
        return Color.WHITE;
    }



    public void draw(){
        System.out.println("Score updating transparent view");

        transparentView.invalidate();
    }



}
