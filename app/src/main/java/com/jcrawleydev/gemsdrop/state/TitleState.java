package com.jcrawleydev.gemsdrop.state;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.res.ResourcesCompat;

public class TitleState implements GameState {

    private final Game game;
    private final View titleView;
    private final int screenHeight;
    private final Animation textAnimation;
    private final TextView tapToPlayText;
    private TranslateAnimation titleViewDisappearAnimation;
    private boolean hasClicked = false;
    private List<ImageView> titleGems;
    private final Animation rotateAnimation;
    private final View parentView;

    public TitleState(View parentView, Context context, Game game, View titleView, int screenHeight){
        this.parentView = parentView;
        this.game = game;
        this.titleView = titleView;
        this.screenHeight = screenHeight;
        textAnimation =  AnimationUtils.loadAnimation(context, R.anim.text_bounce);
        rotateAnimation =  AnimationUtils.loadAnimation(context, R.anim.rotate_infinite);
        textAnimation.setRepeatCount(Animation.INFINITE);
        tapToPlayText = parentView.findViewById(R.id.tapToPlayTextView);
        TextView titleTextView = parentView.findViewById(R.id.titleTextView);
        Typeface customTypeface = ResourcesCompat.getFont(context, R.font.pcalc_font);
        tapToPlayText.setTypeface(customTypeface);
        int topColor = context.getColor(R.color.score_text_top);
        titleTextView.setTextColor(topColor);
        setupTitleViewAnimation();
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


    private void stopAnimationsOnGems(){
        for(View view: titleGems){
            view.clearAnimation();
        }
    }


    private void setupTitleViewAnimation(){
        titleViewDisappearAnimation = new TranslateAnimation(
                0,
                0,
                0,
                screenHeight);
        titleViewDisappearAnimation.setDuration(500);
        titleViewDisappearAnimation.setFillAfter(true);

        titleViewDisappearAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation arg0) {}
            @Override public void onAnimationRepeat(Animation arg0) {}
            @Override
            public void onAnimationEnd(Animation arg0) {
                titleView.setVisibility(View.GONE);
            }
        });
    }


    public void start(){
        hasClicked = false;
        titleView.clearAnimation();
        startTextAnimation();
        startGemAnimation();
    }


    public void stop(){
        stopAnimationsOnGems();
    }


    @Override
    public void click(int x, int y) {
        if(hasClicked){
            return;
        }
        hasClicked = true;
        new Handler(Looper.getMainLooper()).postDelayed(this::cancelAnimationsAndLoadInGameState, 200);
    }


    private void cancelAnimationsAndLoadInGameState(){
        titleView.startAnimation(titleViewDisappearAnimation);
        textAnimation.cancel();
        tapToPlayText.clearAnimation();
        game.loadInGameState();
    }


    private void startTextAnimation(){
        new Handler(Looper.getMainLooper()).postDelayed(()-> tapToPlayText.startAnimation(textAnimation), 1);
    }
}
