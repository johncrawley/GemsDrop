package com.jcrawleydev.gemsdrop.tasks;

import com.jcrawleydev.gemsdrop.MainActivity;

import java.util.concurrent.ScheduledFuture;

public class CancelFutureTask implements Runnable {
    private ScheduledFuture future;
    private MainActivity mainActivity;

    public CancelFutureTask(ScheduledFuture future, MainActivity mainActivity){
        this.future = future;
        this.mainActivity = mainActivity;
    }

    public void run(){
        future.cancel(false);
        mainActivity.deleteMarkedGems();
    }
}
