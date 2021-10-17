package com.jcrawleydev.gemsdrop.gem;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;

public class GemPaintOptions {

    private final Paint paint;

    public GemPaintOptions(int gemSize){
        paint = new Paint();
        int radius = gemSize / 6;
        paint.setMaskFilter(new BlurMaskFilter(radius, BlurMaskFilter.Blur.SOLID));
    }

    public Paint getGemPaint(){
        return paint;
    }

}
