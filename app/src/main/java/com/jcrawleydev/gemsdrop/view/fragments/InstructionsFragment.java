package com.jcrawleydev.gemsdrop.view.fragments;

import static android.view.View.VISIBLE;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.MainViewModel;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.instructions.InstructionsView;

import java.util.List;

public class InstructionsFragment extends Fragment implements InstructionsView {


    private View dot1,dot2,dot3,dot4;
    private View text1,text2,text3,text4;
    private MainViewModel viewModel;
    private Group group1, group2, group3, group4;
    private int containerWidth, containerHeight;
    private RectF boundsMoveLeft, boundsMoveRight, boundsRotate, boundsDrop, currentBounds;
    private float gemWidth;


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
        assignLayoutDimensions(parent);
        assignOnClick(parent);
        return parent;
    }


    private void startInstructions(){
        int index = viewModel.gameInstructions.getCurrentIndex();
       var currentGroup = switch(index){
           case 1 -> group1;
           case 2 -> group2;
           case 3 -> group3;
           default -> group4;
        };
        currentGroup.setVisibility(VISIBLE);
        viewModel.gameInstructions.initCurrentInstruction();
        var dot = group1.findViewWithTag("dot");
        startPulse(dot);

       currentBounds = switch(index){
           case 1 -> boundsMoveLeft;
           case 2 -> boundsMoveRight;
           case 3 -> boundsRotate;
           default -> boundsDrop;
       };
    }


    @SuppressLint("ClickableViewAccessibility")
    private void assignOnClick(View parent){
        parent.setOnTouchListener((view, motionEvent) ->
        {
            handleClick(motionEvent.getX(), motionEvent.getY());
            return false;
        });
    }


    private void handleClick(float x, float y){
        if(currentBounds != null && currentBounds.contains(x, y)){
            viewModel.gameInstructions.onClick(x,y);
        }
    }


    private void assignLayoutDimensions(View parent){
        var listener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                parent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                containerWidth = parent.getMeasuredWidth();
                containerHeight = parent.getMeasuredHeight();
                assignGemDimensions();
                initClickBounds();
                startInstructions();
            }
        };
        parent.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    private void initClickBounds(){
        float xStart = 0;
        float xRotateStart = 0.3f / containerWidth;
        float xRotateEnd = 0.6f / containerWidth;
        float xEnd = containerWidth;

        float yStart = 0;
        float yDropStart = 0.8f / containerHeight;
        float yEnd = containerHeight;

        boundsMoveLeft = new RectF(xRotateEnd, yStart, xEnd, yDropStart);
        boundsMoveRight = new RectF(xStart, yStart, xRotateStart, yDropStart);
        boundsRotate = new RectF(xRotateStart, yStart, xRotateEnd, yDropStart);
        boundsDrop = new RectF(xStart, yDropStart, xEnd, yEnd);


    }


    private void assignGemDimensions(){
        var shortestDimension = Math.min(containerHeight, containerWidth);
        gemWidth = shortestDimension / 9f;
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
        group1 = parent.findViewById(R.id.instructionGroup1);
        group2 = parent.findViewById(R.id.instructionGroup2);
        group3 = parent.findViewById(R.id.instructionGroup3);
        group4 = parent.findViewById(R.id.instructionGroup4);

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



    @Override
    public void setClickBounds(float xStart, float yStart, float xEnd, float yEnd) {

    }

    @Override
    public void createGems(List<Gem> gems) {

    }

    @Override
    public void updateGems() {

    }
}