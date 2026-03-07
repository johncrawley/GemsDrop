package com.jcrawleydev.gemsdrop.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.score.ScoreRecords;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;


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
        var scoreRecords = new ScoreRecords(getContext());
        ViewGroup highScoresLayout = parentView.findViewById(R.id.highScoresLayout);

        for(int i = 0; i < scoreRecords.getOrderedHighScores().size(); i++){
            highlightUserHighScore(highScoresLayout, i, scoreRecords);
        }
    }


    private void highlightUserHighScore(ViewGroup parent, int index, ScoreRecords scoreRecords){
        var highScores = scoreRecords.getOrderedHighScores();
        var mostRecentScore = scoreRecords.getMostRecentScore();

        if(index >= parent.getChildCount()){
            return;
        }
        TextView textView = (TextView) parent.getChildAt(index);
        int highScore = highScores.get(index);
        textView.setText(String.valueOf(highScore));
        if(highScore == mostRecentScore){
            textView.setTextColor(Color.YELLOW);
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        //titleGemsAnimator.stop();
    }


}