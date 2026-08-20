package com.jcrawleydev.gemsdrop.instructions;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DropInstruction extends Instruction {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> dropFuture;
    private int dropCount = 0;


    public DropInstruction(){
        super(1,null,null,null);
    }

    private void startDrop(){
        dropFuture = executorService.schedule(this::drop, 150, TimeUnit.MILLISECONDS);
    }


    private void drop(){
        gameInstructions.drop();
        updateDropCount();
    }


    private void updateDropCount(){
        final int dropLimit = 20;

        if(++dropCount > dropLimit){
            dropFuture.cancel(false);
        }
    }



}
