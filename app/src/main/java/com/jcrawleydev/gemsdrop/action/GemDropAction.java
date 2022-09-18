package com.jcrawleydev.gemsdrop.action;

import com.jcrawleydev.gemsdrop.control.GemControls;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.speed.SpeedControllerImpl;
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
    private final SpeedControllerImpl speedController;
    private final GemGridLayer gemGridLayer;
    private final ActionMediator actionMediator;
    private int evalCount;
    private int dropCount;
    boolean isQuickDropCancelled = false;
    private boolean isFirstDrop = true;


    public GemDropAction(SpeedControllerImpl speedController,
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
            if(hasGemDropStarted){
                return;
            }
            cancelFutures();
            int dropInterval = 70 - speedController.getCurrentSpeed();
            int redrawInterval = 20;
            isFirstDrop = true;
            speedController.update();
            hasGemDropStarted = true;
            isQuickDropCancelled = false;
            evalCount = 0;
            dropCount = 0;
            score.resetMultiplier();
            gemGroup = gemGroupFactory.createGemGroup();
            controls.activateAndSet(gemGroup);
            gemGroupLayer.setGemGroup(gemGroup);
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
    }


    public void dropOnInterval(){
        if(gemGroup.isQuickDropEnabled() && !isFirstDrop){
            dropQuick();
        }
        dropCount++;
        if(dropCount % 5 == 0){
            drop();
        }
        isFirstDrop = false;
    }


    public void dropQuick(){
        gemGroup.dropBy();
        gemGroup.decrementMiddleYPosition();
        removeAtLeastSomeGems();
        if(isQuickDropCancelled){
            return;
        }
        gemGroup.dropNoUpdate();
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
        if(gemGrid.shouldAddAll(gemGroup)) {
            gemGrid.add(gemGroup);
            gemGridLayer.draw();
            gemGroup.setGemsInvisible();
            isQuickDropCancelled = true;
            actionMediator.onAllGemsAdded();
        }
        else if(gemGrid.addAnyFrom(gemGroup)){
            gemGridLayer.draw();
            isQuickDropCancelled = true;
            actionMediator.onAnyGemsAdded();
        }
    }

}
