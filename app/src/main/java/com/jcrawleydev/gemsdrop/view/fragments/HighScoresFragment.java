package com.jcrawleydev.gemsdrop.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;
import com.jcrawleydev.gemsdrop.view.fragments.utils.GameUtils;


public class HighScoresFragment extends Fragment {

    public HighScoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View parent = inflater.inflate(R.layout.fragment_high_scores, container, false);

       parent.setOnClickListener((v)-> FragmentUtils.loadMainMenu(this));
        setupHighScores((ViewGroup)parent);
        FragmentUtils.loadMainMenuOnBackButtonPressed(this);
       return parent;

    }


    private void setupHighScores(ViewGroup parent){
        var highScores = GameUtils.getHighScores(this);
        var finalScore = GameUtils.getFinalScoreString(this);

        for(int i = 0; i <= parent.getChildCount() && i < highScores.size(); i++){
            if(i == 1){
                assignTempHighScoreTo(getTextViewFrom(parent, i), highScores.get(i));
            }else{
                assignScoreTo(getTextViewFrom(parent, i), highScores.get(i), finalScore);
            }
        }
    }


    private TextView getTextViewFrom(ViewGroup parent, int index ){
        int textViewIndex = index + 2; //NB starting from second child because first child is the guideline and second is the title
        return (TextView) parent.getChildAt(textViewIndex);
    }


    private void  assignTempHighScoreTo(TextView textView, String highScore){
        textView.setText(highScore);
        textView.setBackgroundResource(R.drawable.background_recent_high_score);
    }


    private void assignScoreTo(TextView textView, String highScore, String finalScore){
        if(textView != null){
            textView.setText(highScore);
            if(highScore.equals(finalScore)){
                int color = getResources().getColor(R.color.high_score_recent_item_text, null);
                textView.setTextColor(color);
                textView.setBackgroundResource(R.drawable.background_recent_high_score_text);
            }
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        //titleGemsAnimator.stop();
    }


}