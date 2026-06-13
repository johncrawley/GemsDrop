package com.jcrawleydev.gemsdrop.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;
import com.jcrawleydev.gemsdrop.view.fragments.utils.GameUtils;


public class HighScoresFragment extends Fragment {

    private OnBackPressedCallback backPressedCallback;

    public HighScoresFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        var parent = inflater.inflate(R.layout.fragment_high_scores, container, false);
        loadMainMenuOnClick(parent);
        setupHighScores((ViewGroup) parent);
        setupBackButton();
        return parent;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (backPressedCallback != null) {
            backPressedCallback.remove();
        }
    }


    private void loadMainMenuOnClick(View parent) {
        parent.setOnClickListener((v) -> FragmentUtils.loadMainMenu(this));
    }


    private void setupBackButton() {
        backPressedCallback = FragmentUtils.loadMainMenuOnBackButtonPressed(this);
    }


    private void setupHighScores(ViewGroup parent) {
        var highScores = GameUtils.getHighScores(this);
        var finalScore = GameUtils.getFinalScoreString(this);

        for (int i = 0; i <= parent.getChildCount() && i < highScores.size(); i++) {
            if (i == 1) {
                assignTempHighScoreTo(getTextViewFrom(parent, i), highScores.get(i));
                continue;
            }
            assignScoreTo(getTextViewFrom(parent, i), highScores.get(i), finalScore);
        }
    }


    private TextView getTextViewFrom(ViewGroup parent, int index) {
        int textViewIndex = index + 2; //NB starting from second child because first child is the guideline and second is the title
        return (TextView) parent.getChildAt(textViewIndex);
    }


    private void assignTempHighScoreTo(TextView textView, String highScore) {
        textView.setText(highScore);
        textView.setBackgroundResource(R.drawable.background_recent_high_score);
    }


    private void assignScoreTo(TextView textView, String highScore, String finalScore) {
        if (textView == null) {
            return;
        }
        textView.setText(highScore);
        setHighScoreStyle(textView, highScore, finalScore);
    }


    private void setHighScoreStyle(TextView textView, String highScore, String finalScore){
        if (highScore.equals(finalScore)) {
            int color = getResources().getColor(R.color.high_score_recent_item_text, null);
            textView.setTextColor(color);
            textView.setBackgroundResource(R.drawable.background_recent_high_score_text);
        }
    }

}