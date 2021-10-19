package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.score.Score;
import com.jcrawleydev.gemsdrop.tasks.AnimateTask;
import com.jcrawleydev.gemsdrop.tasks.GemDropTask;
import com.jcrawleydev.gemsdrop.view.gemgrid.GemGridLayer;
import com.jcrawleydev.gemsdrop.view.GemGroupLayer;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GemDropAction {

    private boolean hasGemDropStarted = false;
    private GemGroup gemGroup;
    private final GemControls controls;
    private final GemGroupLayer gemGroupLayer;
    private final GemGridLayer gemGridView;
    private ScheduledFuture<?> gemDropFuture, animateFuture;
    private final ActionMediator actionMediator;
    private final GemGroupFactory gemGroupFactory;
    private final Score score;
    private final SpeedController speedController;


    public GemDropAction(SpeedController speedController,
                         ActionMediator actionMediator,
                         GemControls controls,
                         GemGroupLayer gemGroupLayer,
                         GemGridLayer gemGridView,
                         GemGroupFactory gemGroupFactory,
                         Score score){
        this.speedController = speedController;
        this.controls = controls;
        this.gemGroupLayer = gemGroupLayer;
        this.gemGridView = gemGridView;
        this.actionMediator = actionMediator;
        this.gemGroupFactory = gemGroupFactory;
        this.score = score;
    }


    public void setGemGroup(GemGroup gemGroup) {
        this.gemGroup = gemGroup;
    }


    public void start(){
            if(hasGemDropStarted){
                return;
            }
            if(animateFuture != null){
                animateFuture.cancel(false);
            }
            final int GEM_DROP_TASK_INTERVAL = 70 - speedController.getCurrentSpeed();
            final int REDRAW_INTERVAL = 20;
            speedController.update();

            hasGemDropStarted = true;
            score.resetMultiplier();
            gemGroup = gemGroupFactory.createGemGroup();
            controls.activateAndSet(gemGroup);
            gemGroupLayer.setGemGroup(gemGroup);
            GemDropTask gemDropTask = new GemDropTask(gemGroup, gemGridView, actionMediator);
            AnimateTask animateTask = new AnimateTask(gemGroupLayer);

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
            gemDropFuture = executor.scheduleWithFixedDelay(gemDropTask, 0, GEM_DROP_TASK_INTERVAL, TimeUnit.MILLISECONDS);
            animateFuture = executor.scheduleWithFixedDelay(animateTask, 0, REDRAW_INTERVAL, TimeUnit.MILLISECONDS);
    }


    public void cancelFutures(){
        gemDropFuture.cancel(false);
        animateFuture.cancel(false);
        gemGroupLayer.drawIfUpdated();
    }


    public void reset(){
        hasGemDropStarted = false;
        gemGroupLayer.wipe();
        controls.reactivate();
    }

}
