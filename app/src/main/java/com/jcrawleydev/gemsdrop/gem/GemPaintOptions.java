package com.jcrawleydev.gemsdrop.gem;

import android.graphics.BlurMaskFilter;
import android.graphics.Paint;

public class GemPaintOptions {

    private final Paint paint;

    public GemPaintOptions(float gemSize){
        paint = new Paint();
        float radius = gemSize / 8f;
        paint.setMaskFilter(new BlurMaskFilter(radius, BlurMaskFilter.Blur.SOLID));
    }

    public Paint getGemPaint(){
        return paint;
    }

}
