package com.jcrawleydev.gemsdrop.instructions;

import java.util.concurrent.atomic.AtomicBoolean;

public class Instruction {

    int counter;
    private final int counterLimit;
    private final Runnable onClickRunnable;
    final GameInstructions gameInstructions;
    final AtomicBoolean isActive = new AtomicBoolean();


    public Instruction(int counterLimit, Runnable onClick, GameInstructions gameInstructions){
        this.counterLimit = counterLimit;
        this.onClickRunnable = onClick;
        this.gameInstructions = gameInstructions;
    }


    public void init(){
        isActive.set(true);
    }


    public void onClick(){
        if(!isActive.get()){
            return;
        }
        if(counter < counterLimit){
            counter++;
            onClickRunnable.run();
        }
        if(counter >= counterLimit -1){
            counter = 0;
            gameInstructions.moveToNextInstruction();
            isActive.set(false);
        }
    }


    public void reset(){
        counter = 0;
    }
}
