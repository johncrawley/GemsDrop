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
import java.util.Random;

public class TitleGemsAnimator {


    private List<ImageView> titleGems;
    private final Animation rotateAnimation;
    private final View parentView;


    public TitleGemsAnimator(View parentView, Context context){
        this.parentView = parentView;
        random = new Random(System.nanoTime());
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

    private Random random;


    private void setRandomGemColor(ImageView gemView){
        var colors = List.of(R.drawable.jewel_blue,
                R.drawable.jewel_red_2,
                R.drawable.jewel_yellow,
                R.drawable.jewel_green,
                R.drawable.jewel_orange,
                R.drawable.jewel_purple,
                R.drawable.jewel_deep_blue,
                R.drawable.jewel_turquoise);


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
