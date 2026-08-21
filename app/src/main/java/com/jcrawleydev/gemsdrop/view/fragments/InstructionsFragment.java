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

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.MainViewModel;
import com.jcrawleydev.gemsdrop.R;

public class InstructionsFragment extends Fragment {


    private View dot1,dot2,dot3,dot4;
    private View text1,text2,text3,text4;
    private MainViewModel viewModel;

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

        assignViewModel();
        setupViews(parent);
        return parent;
    }


    private void startInstructions(){


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }


    private void assignViewModel(){
        var mainActivity = (MainActivity)getActivity();
        if(mainActivity != null){
            viewModel = mainActivity.getViewModel();
        }
    }



    private void setupViews(View parent){
         dot1 = parent.findViewById(R.id.instructionDot1);
        dot2 = parent.findViewById(R.id.instructionDot2);
        dot3 = parent.findViewById(R.id.instructionDot3);
        dot4 = parent.findViewById(R.id.instructionDot4);

        text1 = parent.findViewById(R.id.instructionText1);
        text2 = parent.findViewById(R.id.instructionText2);
        text3 = parent.findViewById(R.id.instructionText3);
        text4 = parent.findViewById(R.id.instructionText4);

        startPulse(dot1);
    }


    private void startPulse(View dot) {
        var scaleX = ObjectAnimator.ofFloat(dot, "scaleX", 1f, 1.4f);
        var scaleY = ObjectAnimator.ofFloat(dot, "scaleY", 1f, 1.4f);
        var alpha  = ObjectAnimator.ofFloat(dot, "alpha", 1f, 0.1f);

        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        alpha.setRepeatCount(ValueAnimator.INFINITE);

        scaleX.setRepeatMode(ValueAnimator.RESTART);
        scaleY.setRepeatMode(ValueAnimator.RESTART);
        alpha.setRepeatMode(ValueAnimator.RESTART);

        var set = new AnimatorSet();
        set.playTogether(scaleX, scaleY, alpha);
        set.setDuration(800);
        set.setInterpolator(new AccelerateDecelerateInterpolator());
        set.start();
    }
}