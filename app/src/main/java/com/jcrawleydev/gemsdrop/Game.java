package com.jcrawleydev.gemsdrop;

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
import com.jcrawleydev.gemsdrop.view.BitmapLoader;
import com.jcrawleydev.gemsdrop.view.GemGridView;
import com.jcrawleydev.gemsdrop.view.GemGroupView;
import com.jcrawleydev.gemsdrop.view.TransparentView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Game {


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
    private final int FLICKER_SPEED = 80;
    private final int GEM_GRID_GRAVITY_INTERVAL = 30;
    private int quickDropAnimationInterval = 70;



    public Game(int screenWidth, int screenHeight){
        this.width = screenWidth;
        this.height = screenHeight;
        int initialY = gemWidth * -2;

        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .withNumerOfGems(3)
                .withInitialPosition(4)
                .withFloorAt(floorY)
                .build();
    }


    void initGemGridView(TransparentView v){
        gemGrid = new GemGrid(7,12);
        gemGrid.setDropIncrement(gemWidth / 5);
        gemGridView = new GemGridView(v, gemGrid, 150, floorY);
        evaluator = new Evaluator(gemGrid, 3);
        gemControls = new GemControls(gemGrid);
        clickHandler = new ClickHandler(gemControls, width, height);
    }

    void initGemGroupView(TransparentView v, BitmapLoader bitmapLoader){
        gemGroupView = new GemGroupView(v, bitmapLoader, gemGroupFactory.createGemGroup());
    }


    /*



    private void startGemDrop(){

        final int GEM_DROP_TASK_INTERVAL = 40;

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
        gemDropFuture = executor.scheduleWithFixedDelay(gemDropTask, 0, GEM_DROP_TASK_INTERVAL, TimeUnit.MILLISECONDS);
        animateFuture = executor.scheduleWithFixedDelay(animateTask, 0, 20, TimeUnit.MILLISECONDS);
    }


    public void quickDropRemainingGems(){
        gemGroup.enableQuickDrop();
        QuickDropTask quickDropTask = new QuickDropTask(this, gemGroup, gemGroupView, gemGrid, gemGridView);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        gemControls.deactivate();
        quickDropFuture = executor.scheduleWithFixedDelay(quickDropTask, 0, quickDropAnimationInterval, TimeUnit.MILLISECONDS);
    }


    public void cancelDropAndAnimateFutures(){
        gemDropFuture.cancel(false);
        animateFuture.cancel(false);
        gemGroupView.drawIfUpdated();
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
        gemsFlickerFuture = executor.scheduleWithFixedDelay(markedGemsFlickerTask, 0, FLICKER_SPEED, TimeUnit.MILLISECONDS);

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
        evaluateStep();
    }


    public void startGemGridGravityDrop(){
        Runnable gemGridGravityTask = new GemGridGravityTask(gemGrid, gemGridView, this);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        gemGridGravityFuture = executor.scheduleWithFixedDelay(gemGridGravityTask, 0, GEM_GRID_GRAVITY_INTERVAL, TimeUnit.MILLISECONDS);
    }

*/


}
