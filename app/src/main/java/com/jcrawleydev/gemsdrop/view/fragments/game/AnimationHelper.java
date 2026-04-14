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


    public AnimationHelper(ViewGroup layout){
        this.previewLayout = layout;
        fadeInAnimation.setDuration(250);
        fadeOutAndInAnimation.setDuration(250);

        fadeOutAndInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                previewLayout.setVisibility(View.INVISIBLE);
                for(var update : gemPreviewUpdates){
                    update.imageView().setImageDrawable(update.drawable());
                }
                previewLayout.startAnimation(fadeInAnimation);
            }
            @Override public void onAnimationStart(Animation animation){/*do nothing */}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });


        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) { /*do nothing */}
            @Override public void onAnimationStart(Animation animation){ previewLayout.setVisibility(View.VISIBLE);}
            @Override public void onAnimationRepeat(Animation animation) { /* do nothing */}
        });
    }


    public Animation getFadeInAnimation(){
        return fadeInAnimation;
    }


    public void animatePreviewChangeFor(List<GemPreviewUpdate> gemPreviewUpdates){
        this.gemPreviewUpdates = gemPreviewUpdates;
        previewLayout.startAnimation(fadeOutAndInAnimation);
    }

}