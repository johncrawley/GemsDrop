package com.jcrawleydev.gemsdrop.view.fragments;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcrawleydev.gemsdrop.R;

import java.util.ArrayList;
import java.util.List;

public class TitleGemsAnimator {


    private List<ImageView> titleGems;
    private final Animation rotateAnimation;
    private final View parentView;


    public TitleGemsAnimator(View parentView, Context context){
        this.parentView = parentView;
        rotateAnimation =  AnimationUtils.loadAnimation(context, R.anim.rotate_infinite);
        TextView titleTextView = parentView.findViewById(R.id.titleTextView);
        int topColor = context.getColor(R.color.score_text_top);
        titleTextView.setTextColor(topColor);
        setupTitleGems();
    }


    private void setupTitleGems(){
        titleGems = new ArrayList<>();
        addGemView(R.id.titleJewel4);
        addGemView(R.id.titleJewel5);
        addGemView(R.id.titleJewel6);
    }


    private void addGemView(int id){
        titleGems.add(parentView.findViewById(id));
    }


    private void startGemAnimation(){
        for(View view: titleGems){
            view.startAnimation(rotateAnimation);
        }
    }


    public void start(){
        startGemAnimation();
    }


    public void stop(){
        for(View view: titleGems){
            view.clearAnimation();
        }
    }

}
