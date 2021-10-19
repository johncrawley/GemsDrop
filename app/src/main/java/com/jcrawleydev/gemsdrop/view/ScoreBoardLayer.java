package com.jcrawleydev.gemsdrop.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.View;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

import androidx.core.content.res.ResourcesCompat;

public class ScoreBoardLayer implements TextItem, DrawableItem {

    private final TransparentView transparentView;
    private final Score score;
    private final int scoreBarHeight;
    private int scoreBarWidth;
    private int textX, textY;
    private float textSize;
    private final Context context;
    private final int height, width;
    private Paint scorePaint;
    private Paint scoreboardPaint;
    private Rect scoreTextBackgroundRect;

    public ScoreBoardLayer(Context context, View view, Score score, int width, int height, int scoreBarHeight){
        this.context = context;
        this.transparentView = (TransparentView) view;
        this.score = score;
        this.width = width;
        this.height = height;
        transparentView.addDrawableItem(this);
        this.scoreBarHeight = scoreBarHeight;
        setupDimensions();
        setupScorePaint();
        setupBackgroundPaint();
        setupScoreTextBackgroundRect();
    }


    private void setupScoreTextBackgroundRect(){
        int verticalBorder = scoreBarHeight / 4;
        int top = verticalBorder / 2;
        int height = scoreBarHeight - verticalBorder;
        int bottom = top + height;
        int length = (int)(width / 1.5);
        int left = textX - 12;
        int right = left + length;
        scoreTextBackgroundRect = new Rect(left, top, right, bottom);
    }


    private void setupScorePaint(){
        scorePaint = new Paint();
        int topColor = context.getColor(R.color.score_text_top);
        int bottomColor = context.getColor(R.color.score_text_bottom);
        scorePaint.setShader(new LinearGradient( 0,
                0,
                0,
                scoreBarHeight,
                topColor,
                bottomColor,
                Shader.TileMode.CLAMP));
        scorePaint.setTextSize(textSize);
        Typeface customTypeface = ResourcesCompat.getFont(context, R.font.pcalc_font);
        scorePaint.setTypeface(customTypeface);
    }


    private void setupBackgroundPaint(){
        int topColor = context.getColor(R.color.scoreboard_top);
        int bottomColor = context.getColor(R.color.scoreboard_bottom);
        scoreboardPaint = new Paint();
        scoreboardPaint.setShader(new LinearGradient( 0,
                0,
                0,
                scoreBarHeight,
                topColor,
                bottomColor,
                Shader.TileMode.CLAMP));
    }


    private void setupDimensions(){
        textSize = (float) height / getInt(R.integer.score_board_text_height_ratio);
        scoreBarWidth = width;
        //scoreBarHeight = height / getInt(R.integer.score_board_height_ratio);
        textX = scoreBarWidth / getInt(R.integer.score_position_relative_x);
        textY = scoreBarHeight - (scoreBarHeight / 3);//- (height / getInt(R.integer.score_board_text_position_y_offset));
    }


    private int getInt(int resId){
        return context.getResources().getInteger(resId);
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
        canvas.drawRect(scoreBoardBackgroundRect, scoreboardPaint);
        canvas.drawRect(scoreTextBackgroundRect, paint);
        canvas.drawText(getText(), textX, textY, scorePaint);
    }


    @Override
    public int getColor(){
        return Color.WHITE;
    }



    public void draw(){
        transparentView.invalidate();
    }



}
