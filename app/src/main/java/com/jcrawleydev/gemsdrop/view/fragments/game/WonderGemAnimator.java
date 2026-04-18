package com.jcrawleydev.gemsdrop.view.fragments.game;

import android.graphics.drawable.AnimationDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jcrawleydev.gemsdrop.R;

public class WonderGemAnimator {

    private AnimationDrawable wonderGemAnimation;


    public void stopAnimation(){
        if(wonderGemAnimation != null && wonderGemAnimation.isRunning()){
            wonderGemAnimation.stop();
        }
    }


    public void startAnimation(ViewGroup gemLayout){
        ImageView wonderGemView = (ImageView) gemLayout.getChildAt(0);
        wonderGemView.setBackgroundResource(R.drawable.wonder_gem_animation);
        wonderGemAnimation = (AnimationDrawable) wonderGemView.getBackground();
        wonderGemAnimation.start();
    }

}
