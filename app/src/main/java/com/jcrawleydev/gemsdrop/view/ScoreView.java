package com.jcrawleydev.gemsdrop.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.score.Score;


import java.util.Collections;

public class ScoreView implements TextItem {

    private TransparentView transparentView;
    private Score score;

    public ScoreView(View view, Score score, BitmapLoader bitmapLoader, int height){
        this.transparentView = (TransparentView) view;
        this.score = score;
        transparentView.setTextItems(Collections.singletonList(this));
        transparentView.setTextColor(Color.WHITE);
        float textSize = (float) height / 15;
        System.out.println("ScoreView, text size: " + textSize);
        transparentView.setTextSize(textSize);
    }

    public Score getScore(){
        return this.score;
    }

    @Override
    public String getText(){
        return "SCORE: " + score.get();
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
    public int getColor(){
        return Color.WHITE;
    }



    public void draw(){
        System.out.println("Score updating transparent view");
        transparentView.invalidate();
    }



}
