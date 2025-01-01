package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import java.util.function.Consumer;

public class GemAnimator {

    public static void animateRemovalOf(ViewGroup gemLayout, Consumer<ViewGroup> cleanupGem){
        View gemView = gemLayout.getChildAt(0);

        Animation reductionAnimation = createCentredScaleAnimation(1.5f, 0f, 400);
        onConclusionOf(reductionAnimation, ()-> cleanupGem.accept(gemLayout));

        Animation enlargementAnimation = createCentredScaleAnimation(1f, 1.5f, 270);
        onConclusionOf(enlargementAnimation, ()-> {
            gemView.clearAnimation();
            gemView.startAnimation(reductionAnimation);
        });

        gemView.startAnimation(enlargementAnimation);
    }


    public static void animateAppearanceOf(ViewGroup gemLayout) {
        gemLayout.setVisibility(View.VISIBLE);
        View gemView = gemLayout.getChildAt(0);
        Animation animation = createCentredScaleAnimation(0f, 1f, 750);
        gemView.startAnimation(animation);
    }


    private static Animation createCentredScaleAnimation(float startingScale, float endingScale, int duration){
        Animation animation = new ScaleAnimation(
                startingScale, endingScale,
                startingScale, endingScale,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setFillAfter(true); // Needed to keep the result of the animation
        animation.setDuration(duration);
        return animation;
    }


    private static void onConclusionOf(Animation animation, Runnable runnable){
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                runnable.run();
            }
        });
    }
}
