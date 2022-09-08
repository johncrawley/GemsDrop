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
    private int evalCount;
    private int dropCount;

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


    public void start(){
        log("Entered start()");
            if(hasGemDropStarted){
                return;
            }
            cancelFutures();
            int dropInterval = 70 - speedController.getCurrentSpeed();
            int redrawInterval = 20;
            speedController.update();
            hasGemDropStarted = true;
            isQuickDropCancelled = false;
            evalCount = 0;
            dropCount = 0;
            score.resetMultiplier();
            log("About to createGemGroup");
            gemGroup = gemGroupFactory.createGemGroup();
            controls.activateAndSet(gemGroup);
            gemGroupLayer.setGemGroup(gemGroup);
            log("About to executeDropAndAnimateTasks");
            executeDropAndAnimateTasks(dropInterval, redrawInterval);
    }


    private void executeDropAndAnimateTasks(int dropInterval, int redrawInterval){
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(3);
        gemDropFuture = executor.scheduleWithFixedDelay(this::dropOnInterval, 0, dropInterval, TimeUnit.MILLISECONDS);
        animateFuture = executor.scheduleWithFixedDelay(gemGroupLayer::drawIfUpdated, 0, redrawInterval, TimeUnit.MILLISECONDS);
    }


    public void cancelFutures(){
        cancelFuture(gemDropFuture);
        cancelFuture(animateFuture);
        gemGroupLayer.drawIfUpdated();
    }

    public void cancelFuture(ScheduledFuture<?> future){
        if(future !=null){
            future.cancel(false);
        }
    }


    public void reset(){
        hasGemDropStarted = false;
        gemGroupLayer.wipe();
        //controls.reactivate();
    }


    public void dropOnInterval(){
        log("entered dropOnInterval()");
        if(gemGroup.isQuickDropEnabled()){
            log("dropOnInterval() - quickDrop is enabled, invoking dropQuick()");
            dropQuick();
        }
        dropCount++;
        if(dropCount % 5 == 0){
            drop();
        }
    }


    public void dropQuick(){
        gemGroup.dropBy();
        gemGroup.decrementMiddleYPosition();
        log("entered dropQuick, calling removeAtLeastSomeGems()");
        //removeAtLeastSomeGems();
        if(isQuickDropCancelled){
            return;
        }
        gemGroup.dropNoUpdate();
        log("entered dropQuick, calling removeAtLeastSomeGems() again");
        removeAtLeastSomeGems();
    }


    public void drop(){
        evalCount++;
        gemGroup.dropBy();
        if(evalCount %2 == 1){
            gemGroup.decrementMiddleYPosition();
            removeAtLeastSomeGems();
        }
    }


    private void removeAtLeastSomeGems(){
        GemGrid gemGrid = gemGridLayer.getGemGrid();
        if(gemGrid.shouldAdd(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            isQuickDropCancelled = true;
            actionMediator.onAllGemsAdded();
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            log("removeAtLeastSomeGems() addAnyFrom() was true");
            gemGridLayer.draw();
            isQuickDropCancelled = true;
            log("removeAtLeastSomeGems() about to invoke actionMediator.onAnyGemsAdded()");
            actionMediator.onAnyGemsAdded();
        }
    }

    private void log(String msg){
        System.out.println("GemDropAction: " + msg);
    }

    boolean isQuickDropCancelled = false;

}
