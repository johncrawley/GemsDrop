package com.jcrawleydev.gemsdrop.instructions;

import android.graphics.RectF;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DropInstruction extends Instruction {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> dropFuture;
    private int dropCount = 0;


    public DropInstruction(GameInstructions gameInstructions){
        super(1, null ,gameInstructions);
    }


    @Override
    public void onClick(){
        if(counter == 0){
            counter++;
            dropFuture = executorService.schedule(this::drop, 150, TimeUnit.MILLISECONDS);
        }
    }


    private void drop(){
        gameInstructions.drop();
        cancelDropOnLimitReached();
    }


    private void cancelDropOnLimitReached(){
        final int dropLimit = 20;

        if(++dropCount > dropLimit){
            dropFuture.cancel(false);
        }
    }



}
