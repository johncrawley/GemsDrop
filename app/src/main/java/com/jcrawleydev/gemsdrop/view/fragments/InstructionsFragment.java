package com.jcrawleydev.gemsdrop.view.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.R;

public class InstructionsFragment extends Fragment {




    public InstructionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_instructions, container, false);

        setupViews(parent);
        return parent;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void setupViews(View parent){
        View dot1 = parent.findViewById(R.id.instructionDot1);
        startPulse(dot1);
    }


    private void startPulse(View dot) {
        var scaleX = ObjectAnimator.ofFloat(dot, "scaleX", 1f, 1.4f);
        var scaleY = ObjectAnimator.ofFloat(dot, "scaleY", 1f, 1.4f);
        var alpha  = ObjectAnimator.ofFloat(dot, "alpha", 1f, 0.1f);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        alpha.setRepeatCount(ValueAnimator.INFINITE);

        scaleX.setRepeatMode(ValueAnimator.REVERSE);
        scaleY.setRepeatMode(ValueAnimator.REVERSE);
        alpha.setRepeatMode(ValueAnimator.REVERSE);

        var set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(800);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }
}