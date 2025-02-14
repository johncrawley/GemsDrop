package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.animation.TypeEvaluator;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

class DrawableEvaluator implements TypeEvaluator<Integer> {

    private final ImageView imageView;
    private final Drawable[] drawables;

    public DrawableEvaluator(ImageView imageView, Drawable[] drawables) {
        this.imageView = imageView;
        this.drawables = drawables;
    }

    @Override
    public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
        int index = (int) (fraction * (endValue - startValue) + startValue);
        imageView.setImageDrawable(drawables[index]);
        return index;
    }
}