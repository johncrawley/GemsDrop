package com.jcrawleydev.gemsdrop.view;

import android.graphics.Bitmap;

public interface DrawItem {

    Bitmap getBitmap();
    float getX();
    float getY();
    boolean isVisible();
}
