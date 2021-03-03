package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.action.ActionMediator;

import java.util.concurrent.ScheduledFuture;

public class CancelFutureTask implements Runnable {
    private ScheduledFuture future;
    private ActionMediator actionManager;

    public CancelFutureTask(ScheduledFuture future, ActionMediator actionManager){
        this.future = future;
        this.actionManager = actionManager;
    }

    public void run(){
        future.cancel(false);
        actionManager.deleteMarkedGems();
    }
}
