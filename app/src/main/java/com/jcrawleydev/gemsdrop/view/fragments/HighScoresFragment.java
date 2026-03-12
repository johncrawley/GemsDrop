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
        setupHighScores(parent);
        FragmentUtils.loadMainMenuOnBackButtonPressed(this);
       return parent;

    }


    private void setupHighScores(View parentView){
        var highScores = GameUtils.getHighScores(this);

        ViewGroup highScoresLayout = parentView.findViewById(R.id.highScoresLayout);
        var finalScore = GameUtils.getFinalScoreString(this);

        log("setupHighScore() number of high scores: " + highScores.size());
        for(int i = 0; i < highScores.size(); i++){
            var textView = (TextView) highScoresLayout.getChildAt(i);
            assignScoreTo(textView, highScores.get(i), finalScore);
        }
    }


    private void assignScoreTo(TextView textView, String highScore, String finalScore){
        if(textView == null){
            log("assignScoreTo() text view is null");
            return;
        }
        textView.setText(finalScore);
        if(highScore.equals(finalScore)){
            textView.setTextColor(Color.YELLOW);
        }
    }

    private void log(String msg){
        System.out.println("^^^ HighScoresFragment: " + msg);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //titleGemsAnimator.stop();
    }


}