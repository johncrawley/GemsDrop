package com.jcrawleydev.gemsdrop.view.fragments;

import static android.view.View.INVISIBLE;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import com.jcrawleydev.gemsdrop.MainActivity;
import com.jcrawleydev.gemsdrop.MainViewModel;
import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.instructions.InstructionsView;

import java.util.List;

public class InstructionsFragment extends Fragment implements InstructionsView {


    private View dot1,dot2,dot3,dot4, currentDot;
    private MainViewModel viewModel;
    private Group group1, group2, group3, group4, currentGroup;
    private int containerWidth, containerHeight;
    private RectF boundsMoveRight, boundsMoveLeft, boundsRotate, boundsDrop, currentBounds;
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


    private void log(String msg){
        System.out.println("^^^ InstructionFragment: " + msg);
    }


    private void startInstructions(){
        int index = viewModel.gameInstructions.getCurrentIndex();
        log("Entered startInstructions() currentIndex: " + index);
        setCurrentGroup(index);
        viewModel.gameInstructions.initCurrentInstruction();
        stopPulse();
        setCurrentDot(index);
        if(currentDot != null){
            startPulse(currentDot);
        }

       currentBounds = switch(index){
           case 0 -> boundsMoveRight;
           case 1 -> boundsMoveLeft;
           case 2 -> boundsRotate;
           default -> boundsDrop;
       };
    }

    private void setCurrentGroup(int index){
        if(currentGroup != null){
            currentGroup.setVisibility(INVISIBLE);
        }
        currentGroup = switch(index){
            case 0 -> group1;
            case 1 -> group2;
            case 2 -> group3;
            default -> group4;
        };
        currentGroup.setVisibility(VISIBLE);
    }

    private void setCurrentDot(int index){
        currentDot = switch(index){
            case 0 -> dot1;
            case 1 -> dot2;
            case 2 -> dot3;
            default -> dot4;
        };

    }


    public void start(int index){
        if(currentGroup != null){
            currentGroup.setVisibility(INVISIBLE);
        }
        currentGroup = switch(index){
            case 0 -> group1;
            case 1 -> group2;
            case 2 -> group3;
            default -> group4;
        };
        currentGroup.setVisibility(VISIBLE);
        viewModel.gameInstructions.initCurrentInstruction();
        var dot = currentGroup.findViewWithTag("dot");

        startPulse(dot);

        currentBounds = switch(index){
            case 0 -> boundsMoveLeft;
            case 1 -> boundsMoveRight;
            case 2 -> boundsRotate;
            default -> boundsDrop;
        };
    }


    private void fadeOut(View group, View nextGroup){
        var fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_out);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                group.setVisibility(INVISIBLE);
                fadeIn(nextGroup);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        });
        group.startAnimation(fadeOut);
    }


    private void fadeIn(View group){
        var fadeOut = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                group.setVisibility(VISIBLE);
                var dot = group.findViewWithTag("dot");
                startPulse(dot);
            }

            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationStart(Animation animation) {}
        });
        group.startAnimation(fadeOut);
    }



    @SuppressLint("ClickableViewAccessibility")
    private void assignOnClick(View parent){
        parent.setOnTouchListener((view, motionEvent) ->
        {
            log("motion event detected!");
            handleClick(motionEvent.getX(), motionEvent.getY());
            return false;
        });
    }


    private void handleClick(float x, float y){
        if(currentBounds == null){
            log("entered handleClick, currentBounds are null!");
        }
        else if(!currentBounds.contains(x,y)){
            log("current bounds: " + currentBounds.right + "," + currentBounds.top + "," + currentBounds.left + "," + currentBounds.bottom + " x,y: " + x + "," + y);
        }
        if(currentBounds != null && currentBounds.contains(x, y)){
            log("entered handleClick() about to call gameInstructions onclick()");
            viewModel.gameInstructions.onClick();
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
                viewModel.gameInstructions.setView(InstructionsFragment.this);
            }
        };
        parent.getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }


    private void initClickBounds(){
        float xStart = 0;
        float xRotateStart = containerWidth / 3f;
        float xRotateEnd = xRotateStart * 2;
        float xEnd = containerWidth;

        float yStart = 0;
        float yDropStart = (containerHeight / 8f) * 7f;
        float yEnd = containerHeight;

        boundsMoveLeft = new RectF(xStart, yStart, xRotateStart, yDropStart);
        boundsRotate = new RectF(xRotateStart, yStart, xRotateEnd, yDropStart);
        boundsMoveRight = new RectF(xRotateEnd, yStart, xEnd, yDropStart);
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

        setInvisible(group1, group2, group3, group4);

        dot1 = parent.findViewById(R.id.instructionDot1);
        dot2 = parent.findViewById(R.id.instructionDot2);
        dot3 = parent.findViewById(R.id.instructionDot3);
        dot4 = parent.findViewById(R.id.instructionDot4);
    }


    private void setInvisible(View ...views){
        for(var view : views ){
            view.setVisibility(INVISIBLE);
        }
    }

    AnimatorSet animatorSet;

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

        animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(800);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

    }

    private void stopPulse(){
        if(animatorSet != null && animatorSet.isRunning()){
            animatorSet.cancel();
        }
    }


    @Override
    public void createGems(List<Gem> gems) {

    }

    @Override
    public void updateGems() {

    }
}