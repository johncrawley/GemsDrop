package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.SpeedController;
import com.jcrawleydev.gemsdrop.score.Score;
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
    private ScheduledFuture<?> gemDropFuture, animateFuture;
    private final GemGroupFactory gemGroupFactory;
    private final Score score;
    private final SpeedController speedController;
    private final GemGridLayer gemGridLayer;
    private final ActionMediator actionMediator;

    public GemDropAction(SpeedController speedController,
                         ActionMediator actionMediator,
                         GemControls controls,
                         GemGroupLayer gemGroupLayer,
                         GemGridLayer gemGridLayer,
                         GemGroupFactory gemGroupFactory,
                         Score score){
        this.speedController = speedController;
        this.controls = controls;
        this.gemGroupLayer = gemGroupLayer;
        this.gemGroupFactory = gemGroupFactory;
        this.score = score;

        this.gemGridLayer = gemGridLayer;
        this.actionMediator = actionMediator;
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
            int dropInterval = 70 - speedController.getCurrentSpeed();
            int redrawInterval = 20;
            speedController.update();
            hasGemDropStarted = true;
            score.resetMultiplier();
            gemGroup = gemGroupFactory.createGemGroup();
            controls.activateAndSet(gemGroup);
            gemGroupLayer.setGemGroup(gemGroup);
            executeDropAndAnimateTasks(dropInterval, redrawInterval);
    }


    private void executeDropAndAnimateTasks(int dropInterval, int redrawInterval){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        gemDropFuture = executor.scheduleWithFixedDelay(this::drop, 0, dropInterval, TimeUnit.MILLISECONDS);
        animateFuture = executor.scheduleWithFixedDelay(gemGroupLayer::drawIfUpdated, 0, redrawInterval, TimeUnit.MILLISECONDS);
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


    public void drop(){
        GemGrid gemGrid = gemGridLayer.getGemGrid();
        if(gemGrid.shouldAdd(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            actionMediator.onAllGemsAdded();
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridLayer.draw();
            actionMediator.onAnyGemsAdded();
        }
        else {
            gemGroup.drop();
        }
    }

}
