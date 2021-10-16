package com.jcrawleydev.gemsdrop.view.item;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public interface DrawableItem {


    Bitmap getBitmap();
    int getX();
    int getY();
    boolean isVisible();
    void draw(Canvas canvas, Paint paint);

}
