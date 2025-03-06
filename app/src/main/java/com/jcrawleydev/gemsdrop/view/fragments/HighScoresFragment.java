package com.jcrawleydev.gemsdrop.view.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.records.ScoreRecords;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;


public class HighScoresFragment extends Fragment {

    private TitleGemsAnimator titleGemsAnimator;


    public HighScoresFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View parentView = getView();
        if(parentView == null){
            return;
        }
        setupHighScores(parentView);
        parentView.setOnClickListener((v)->{
            FragmentUtils.loadMainMenu(this);
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_high_scores, container, false);
        titleGemsAnimator = new TitleGemsAnimator(parent, getContext());
        titleGemsAnimator.start();
        return parent;
    }


    private void setupHighScores(View parentView){
        var scoreRecords = new ScoreRecords(getContext());
        var highScores = scoreRecords.getOrderedHighScores();
        var mostRecentScore = scoreRecords.getMostRecentScore();

        ViewGroup highScoresLayout = parentView.findViewById(R.id.highScoresLayout);

        for(int i = 0; i < highScores.size(); i++){
            if(i >= highScoresLayout.getChildCount()){
                return;
            }
            TextView textView = (TextView) highScoresLayout.getChildAt(i);
            int highScore = highScores.get(i);
            textView.setText(highScore);
            if(highScore == mostRecentScore){
                textView.setTextColor(Color.YELLOW);
            }
        }
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        titleGemsAnimator.stop();
    }


}