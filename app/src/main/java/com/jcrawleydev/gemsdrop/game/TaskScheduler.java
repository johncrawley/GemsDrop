package com.jcrawleydev.gemsdrop.game;

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


    public void scheduleWithRepeats(Runnable runnable, long delay ){
        scheduleWithRepeats(runnable, 0, delay);
    }


    public void scheduleWithRepeats(Runnable runnable, long initialDelay, long delay ){
        future = executor.scheduleWithFixedDelay(runnable, initialDelay, delay, TimeUnit.MILLISECONDS);
    }


    public void scheduleOnce(Runnable runnable, long delay){
        executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }


}
