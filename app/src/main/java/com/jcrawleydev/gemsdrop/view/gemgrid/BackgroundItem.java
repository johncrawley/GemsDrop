package com.jcrawleydev.gemsdrop.view.gemgrid;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

public class BackgroundItem implements DrawableItem {
    Rect backgroundRect;
    private final int parentViewWidth;
    int borderWidth, topBorder, floor;

    public BackgroundItem(int parentViewWidth, int borderWidth, int topBorder, int floor){
        this.borderWidth = borderWidth;
        this.topBorder = topBorder;
        this.parentViewWidth = parentViewWidth;
        this.floor = floor;
    }


    @Override
    public Bitmap getBitmap() {
        return null;
    }

    @Override
    public float getX() {
        return 0f;
    }

    @Override
    public float getY() {
        return 0;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        int oldColor = paint.getColor();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);

        backgroundRect = new Rect(borderWidth, topBorder, parentViewWidth - borderWidth, floor);
        canvas.drawRect(backgroundRect, paint);
        paint.setColor(oldColor);
    }
}
