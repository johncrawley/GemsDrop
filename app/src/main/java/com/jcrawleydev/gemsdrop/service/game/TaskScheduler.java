package com.jcrawleydev.gemsdrop.service.game;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> future;


    public void cancelTask(){
        if(future != null && !future.isCancelled()){
            future.cancel(true);
        }
    }


    public void schedule(Runnable runnable, long delay ){
        schedule(runnable, 0, delay);
    }


    public void schedule(Runnable runnable, long initialDelay, long delay ){
        future = executor.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }


}
