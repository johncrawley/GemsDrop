package com.jcrawleydev.gemsdrop.view.fragments;

import static com.jcrawleydev.gemsdrop.view.fragments.utils.GameUtils.getFinalScoreString;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.view.fragments.utils.FragmentUtils;

public class GameOverFragment extends Fragment {


    public GameOverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.game_over, container, false);
       // playTrack(this, R.raw.music_title_1);
        parent.setOnClickListener(this::loadHighScores);
        FragmentUtils.loadMainMenuOnBackButtonPressed(this);
        setupScoreView(parent);
        return parent;
    }


    private void setupScoreView(View parent){
        TextView scoreView = parent.findViewById(R.id.scoreText);
        var finalScoreStr = getFinalScoreString(this);
        scoreView.setText(finalScoreStr);
    }


    private void loadHighScores(View parentView){
        FragmentUtils.loadHighScores(this);
    }

}
