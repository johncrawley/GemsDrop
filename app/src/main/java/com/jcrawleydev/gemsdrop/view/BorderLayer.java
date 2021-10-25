package com.jcrawleydev.gemsdrop.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.view.View;


import com.jcrawleydev.gemsdrop.view.item.DrawableItem;


public class BorderLayer implements DrawableItem {

    private final TransparentView transparentView;
    private final BitmapLoader bitmapLoader;
    private Bitmap patternBitmap;


    public BorderLayer(View view, BitmapLoader bitmapLoader){
        transparentView = (TransparentView)view;
        this.bitmapLoader = bitmapLoader;
        transparentView.addDrawableItem(this);
    }


    public void setPatternImage(int drawableId){
        patternBitmap = bitmapLoader.get(drawableId);
    }


    @Override
    public Bitmap getBitmap() {
        return null;
    }


    @Override
    public int getX() {
        return 0;
    }


    @Override
    public int getY() {
        return 0;
    }


    @Override
    public boolean isVisible() {
        return true;
    }


    public void draw(){
        transparentView.invalidate();
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        BitmapShader patternShader = new BitmapShader(patternBitmap,
                Shader.TileMode.REPEAT,
                Shader.TileMode.REPEAT);

        paint.setColor(0xFFFFFFFF);
        paint.setShader(patternShader);
        Rect rect = new Rect(0,0, transparentView.getWidth(), transparentView.getHeight());
        canvas.drawRect(rect, paint);
    }

}
