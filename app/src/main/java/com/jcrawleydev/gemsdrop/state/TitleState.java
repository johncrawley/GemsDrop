package com.jcrawleydev.gemsdrop.state;

import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
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
    private final MainActivity activity;

    public TitleState(MainActivity activity, Game game, View titleView, int screenHeight){
        this.activity = activity;
        this.game = game;
        this.titleView = titleView;
        this.screenHeight = screenHeight;
        textAnimation =  AnimationUtils.loadAnimation(activity, R.anim.text_bounce);
        rotateAnimation =  AnimationUtils.loadAnimation(activity, R.anim.rotate_infinite);
        textAnimation.setRepeatCount(Animation.INFINITE);
        tapToPlayText = activity.findViewById(R.id.tapToPlayTextView);
        TextView titleTextView = activity.findViewById(R.id.titleTextView);
        Typeface customTypeface = ResourcesCompat.getFont(activity, R.font.pcalc_font);
        tapToPlayText.setTypeface(customTypeface);
        int topColor = activity.getColor(R.color.score_text_top);
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
        titleGems.add(activity.findViewById(id));
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
    }


    public void start(){
        hasClicked = false;
        titleView.clearAnimation();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()-> tapToPlayText.startAnimation(textAnimation), 1);
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
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(()->{
            titleView.startAnimation(titleViewDisappearAnimation);
            textAnimation.cancel();
            tapToPlayText.clearAnimation();
            game.loadInGameState();
        }, 200);

    }
}
