package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.tasks.AnimateTask;
import com.jcrawleydev.gemsdrop.tasks.GemDropTask;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupView;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GemDropAction {

    private boolean hasGemDropStarted = false;
    private GemGroup gemGroup;
    private final GemControls controls;
    private final GemGroupView gemGroupView;
    private final GemGridLayer gemGridView;
    private ScheduledFuture<?> gemDropFuture, animateFuture;
    private final ActionMediator actionMediator;
    private final GemGroupFactory gemGroupFactory;
    private final Score score;
    private final SpeedController speedController;


    public GemDropAction(SpeedController speedController,
                         ActionMediator actionMediator,
                         GemControls controls,
                         GemGroupView gemGroupView,
                         GemGridLayer gemGridView,
                         GemGroupFactory gemGroupFactory,
                         Score score){
        this.speedController = speedController;
        this.controls = controls;
        this.gemGroupView = gemGroupView;
        this.gemGridView = gemGridView;
        this.actionMediator = actionMediator;
        this.gemGroupFactory = gemGroupFactory;
        this.score = score;
    }


    public void setGemGroup(GemGroup gemGroup) {
        this.gemGroup = gemGroup;
    }

    private void log(String msg){
        System.out.println("GemDropAction: " + msg);
    }


    public void start(){

            if(hasGemDropStarted){
                return;
            }
            final int GEM_DROP_TASK_INTERVAL = 70 - speedController.getCurrentSpeed();
            log("start() gem_drop_task_interval: "+  GEM_DROP_TASK_INTERVAL);
            final int REDRAW_INTERVAL = 20;
            speedController.update();

            hasGemDropStarted = true;
            score.resetMultiplier();
            gemGroup = gemGroupFactory.createGemGroup();
            controls.activateAndSet(gemGroup);
            gemGroupView.setGemGroup(gemGroup);
            GemDropTask gemDropTask = new GemDropTask(gemGroup, gemGridView, actionMediator);
            AnimateTask animateTask = new AnimateTask(gemGroupView);

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
            gemDropFuture = executor.scheduleWithFixedDelay(gemDropTask, 0, GEM_DROP_TASK_INTERVAL, TimeUnit.MILLISECONDS);
            animateFuture = executor.scheduleWithFixedDelay(animateTask, 0, REDRAW_INTERVAL, TimeUnit.MILLISECONDS);
    }


    public void cancelFutures(){
        gemDropFuture.cancel(false);
        animateFuture.cancel(false);
        gemGroupView.drawIfUpdated();
    }


    public void reset(){
        hasGemDropStarted = false;
        controls.reactivate();
    }

}
