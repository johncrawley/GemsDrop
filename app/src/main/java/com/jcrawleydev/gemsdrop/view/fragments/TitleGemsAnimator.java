package com.jcrawleydev.gemsdrop.view.fragments;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcrawleydev.gemsdrop.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class TitleGemsAnimator {


    private List<ImageView> titleGems;
    private final Animation rotateAnimation;
    private final View parentView;
    private Random random;


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
        var randomGemResources = getRandomGemColors();
        addGemView(R.id.titleJewel4, randomGemResources.get(0));
        addGemView(R.id.titleJewel5, randomGemResources.get(1));
        addGemView(R.id.titleJewel6, randomGemResources.get(2));
    }


    private void addGemView(int id, int resId){
        var gemView = (ImageView)parentView.findViewById(id);
        gemView.setImageResource(resId);
        titleGems.add(parentView.findViewById(id));
    }


    private List<Integer> getRandomGemColors(){
        var colors = List.of(R.drawable.jewel_blue,
                R.drawable.jewel_red_2,
                R.drawable.jewel_yellow,
                R.drawable.jewel_green,
                R.drawable.jewel_orange,
                R.drawable.jewel_purple,
                R.drawable.jewel_deep_blue,
                R.drawable.jewel_turquoise);
        var list = new ArrayList<>(colors);
        Collections.shuffle(list);
        return list;
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
