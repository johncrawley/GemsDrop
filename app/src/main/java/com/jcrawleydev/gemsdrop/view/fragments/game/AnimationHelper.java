package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import java.util.List;

public class AnimationHelper {

    private final Animation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
    private final Animation fadeOutAndInAnimation = new AlphaAnimation(1.0f, 0.0f);
    private final ViewGroup previewLayout;
    private List<GemPreviewUpdate> gemPreviewUpdates;
    private final int normalBackgroundColor, wonderGemBackgroundColor;
    private boolean containsWonder;

    public AnimationHelper(ViewGroup layout, int normalBackgroundColor, int wonderGemBackgroundColor){
        this.previewLayout = layout;
        fadeOutAndInAnimation.setDuration(250);
        this.normalBackgroundColor = normalBackgroundColor;
        this.wonderGemBackgroundColor = wonderGemBackgroundColor;

        setupFadeOutAnimation();
        setupFadeInAnimation();
    }


    private void setupFadeOutAnimation(){
        fadeOutAndInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
             onFadeOutCompleted();
            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
    }


    private void setupFadeInAnimation(){
        fadeInAnimation.setDuration(250);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation){ previewLayout.setVisibility(View.VISIBLE);}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
            @Override public void onAnimationEnd(Animation animation) { /*do nothing */}
        });
    }


    private void onFadeOutCompleted(){
        previewLayout.setVisibility(View.INVISIBLE);
        setBackgroundColor();
        for(var update : gemPreviewUpdates){
            update.imageView().setImageDrawable(update.drawable());
        }
        previewLayout.startAnimation(fadeInAnimation);
    }


    private void setBackgroundColor(){
        log("entered setBackgroundColor number of previewUpdates: " + gemPreviewUpdates.size());
        int backgroundColor = containsWonder ? wonderGemBackgroundColor : normalBackgroundColor;
        previewLayout.setBackgroundColor(backgroundColor);
    }


    private void log(String msg){
        System.out.println("^^^ AnimationHelper: " + msg);
    }


    public void animatePreviewChangeFor(List<GemPreviewUpdate> gemPreviewUpdates, boolean containsWonder){
        this.gemPreviewUpdates = gemPreviewUpdates;
        this.containsWonder = containsWonder;
        previewLayout.startAnimation(fadeOutAndInAnimation);
    }

}