package com.jcrawleydev.gemsdrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import com.jcrawleydev.gemsdrop.control.ClickHandler;
import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.tasks.AnimateTask;
import com.jcrawleydev.gemsdrop.tasks.CancelFutureTask;
import com.jcrawleydev.gemsdrop.tasks.FlickerMarkedGemsTask;
import com.jcrawleydev.gemsdrop.tasks.GemDropTask;
import com.jcrawleydev.gemsdrop.tasks.GemGridGravityTask;
import com.jcrawleydev.gemsdrop.tasks.QuickDropTask;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private GemGroupFactory gemGroupFactory;
    private GemGroupView gemGroupView;
    int height, width;
    private boolean hasGemDropStarted = false;
    private int floorY = 0;
    private GemGrid gemGrid;
    private GemGridView gemGridView;
    private ClickHandler clickHandler;
    private GemControls gemControls;
    private int gemWidth = 150;
    private ScheduledFuture<?> gemDropFuture, animateFuture, quickDropFuture, gemsFlickerFuture, gemGridGravityFuture;
    private Evaluator evaluator;
    private GemGroup gemGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        assignScreenDimensions();


        int initialY = gemWidth * -2;

        gemGroupFactory = new GemGroupFactory.Builder()
                    .withInitialY(initialY)
                    .withGemWidth(gemWidth)
                    .withNumerOfGems(3)
                    .withInitialPosition(4)
                    .withFloorAt(floorY)
                .build();


        TransparentView gemGroupTransparentView = findViewById(R.id.gemGroupView);
        TransparentView gemGridTransparentView = findViewById(R.id.gemGridView);
        gemGroupTransparentView.setDimensions(width, height);
        gemGroupTransparentView.translateXToMiddle();
        gemGridTransparentView.setDimensions(width, height);
        gemGroupTransparentView.setOnTouchListener(this);
        gemGridTransparentView.setOnTouchListener(this);
        gemGroupView = new GemGroupView(gemGroupTransparentView, MainActivity.this, gemGroupFactory.createGemGroup());
        gemGrid = new GemGrid(7,12);
        gemGrid.setDropIncrement(gemWidth / 5);
        gemGridView = new GemGridView(gemGridTransparentView, gemGrid, 150, floorY);
        evaluator = new Evaluator(gemGrid, 3);
        gemControls = new GemControls(gemGrid);
        clickHandler = new ClickHandler(gemControls, width);
    }


    public boolean onTouch(View v, MotionEvent e){
        if(e.getAction() != MotionEvent.ACTION_DOWN){
            return true;
        }
        clickHandler.click((int)e.getX());
        startGemDrop();
        return true;
    }


    private void assignScreenDimensions(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
        floorY = height - (height /10);
    }


    private void startGemDrop(){
        if(hasGemDropStarted){
            return;
        }
        hasGemDropStarted = true;
        gemGroup = gemGroupFactory.createGemGroup();
        gemControls.setGemGroup(gemGroup);
        gemGroupView.setGemGroup(gemGroup);
        GemDropTask gemDropTask = new GemDropTask(gemGroup, gemGrid, gemGridView, this);
        AnimateTask animateTask = new AnimateTask(gemGroupView);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        gemDropFuture = executor.scheduleWithFixedDelay(gemDropTask, 0, 250, TimeUnit.MILLISECONDS);
        animateFuture = executor.scheduleWithFixedDelay(animateTask, 0, 20, TimeUnit.MILLISECONDS);
    }


    private int quickDropAnimationInterval = 70;

    public void quickDropRemainingGems(){

        QuickDropTask quickDropTask = new QuickDropTask(this, gemGroup, gemGrid, gemGridView);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        gemControls.deactivate();
        quickDropFuture = executor.scheduleWithFixedDelay(quickDropTask, 0, quickDropAnimationInterval, TimeUnit.MILLISECONDS);

    }


    public void cancelFuture(){
        gemDropFuture.cancel(false);
    }

    public void finishQuickDrop(){
        quickDropFuture.cancel(false);
        evaluateStep();
    }

    public void evaluateStep(){
        evaluator.evaluate();

        if(evaluator.hasMarkedGems()){
            startMarkedGemsFlicker();
            return;
        }
        resetDrop();
    }


    private void log(String msg){
        System.out.println("MainActivity: " + msg);
        System.out.flush();
    }


    private void resetDrop(){
        hasGemDropStarted = false;
        gemControls.reactivate();
    }


    private void startMarkedGemsFlicker(){
        Runnable markedGemsFlickerTask = new FlickerMarkedGemsTask(gemGrid, gemGridView);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemsFlickerFuture = executor.scheduleWithFixedDelay(markedGemsFlickerTask, 0, 100, TimeUnit.MILLISECONDS);

        Runnable flickerTimeoutTask = new CancelFutureTask(gemsFlickerFuture, this);
        executor.schedule(flickerTimeoutTask, 1000, TimeUnit.MILLISECONDS);
    }


    public void deleteMarkedGems(){
        evaluator.deleteMarkedGems();
        gemGridView.draw();
        startGemGridGravityDrop();
    }


    public void stopGravity(){
        gemGridGravityFuture.cancel(false);
        resetDrop();
    }


    public void startGemGridGravityDrop(){
        Runnable gemGridGravityTask = new GemGridGravityTask(gemGrid, gemGridView, this);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemGridGravityFuture = executor.scheduleWithFixedDelay(gemGridGravityTask, 0, quickDropAnimationInterval, TimeUnit.MILLISECONDS);
    }



}
