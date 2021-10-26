package com.jcrawleydev.gemsdrop.state;

import android.content.Context;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.jcrawleydev.gemsdrop.Game;
import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.R;

import androidx.core.content.res.ResourcesCompat;

public class TitleState implements GameState {

    private final Game game;
    private final View titleView;
    private final int screenHeight;
    private final Animation textAnimation;
    private final TextView tapToPlayText;


    public TitleState(MainActivity activity, Game game, View titleView, int screenHeight){
        this.game = game;
        this.titleView = titleView;
        this.screenHeight = screenHeight;
        textAnimation =  AnimationUtils.loadAnimation(activity, R.anim.text_bounce);
        textAnimation.setRepeatCount(Animation.INFINITE);
        tapToPlayText = activity.findViewById(R.id.tapToPlayTextView);

        TextView titleTextView = activity.findViewById(R.id.titleTextView);
        Typeface customTypeface = ResourcesCompat.getFont(activity, R.font.pcalc_font);
        tapToPlayText.setTypeface(customTypeface);
      //  titleTextView.setTypeface(customTypeface);
        int topColor = activity.getColor(R.color.score_text_top);
        int bottomColor = activity.getColor(R.color.score_text_bottom);
       /* tapToPlayText.setShader(new LinearGradient( 0,
                0,
                0,
                100,
                topColor,
                bottomColor,
                Shader.TileMode.CLAMP));
        */
        titleTextView.setTextColor(topColor);
        Shader textShader = new LinearGradient(0, 0, titleTextView.getPaint().measureText(titleTextView.getText().toString()), titleTextView.getTextSize(),
                new int[]{topColor, bottomColor},
                new float[]{0, 1}, Shader.TileMode.CLAMP);


      //  titleTextView.setShadowLayer(70, 10,10, Color.DKGRAY);
    }


    public void start(){
        tapToPlayText.startAnimation(textAnimation);
    }


    public void stop(){

    }

    @Override
    public void click(int x, int y) {

        TranslateAnimation animate = new TranslateAnimation(
                0,
                0,
                0,
                screenHeight);
        animate.setDuration(500);
        animate.setFillAfter(true);
        titleView.startAnimation(animate);
        textAnimation.cancel();
        tapToPlayText.clearAnimation();
        game.loadInGameState();
    }
}
