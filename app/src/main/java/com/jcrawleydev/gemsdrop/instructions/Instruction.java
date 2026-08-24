package com.jcrawleydev.gemsdrop.instructions;

import android.graphics.RectF;

public class Instruction {

    int counter;
    private final int counterLimit;
    private final Runnable onClickRunnable;
    final GameInstructions gameInstructions;


    public Instruction(int counterLimit, Runnable onClick, GameInstructions gameInstructions){
        this.counterLimit = counterLimit;
        this.onClickRunnable = onClick;
        this.gameInstructions = gameInstructions;
    }


    public void init(){
        //do nothing for now
    }


    public void onClick(){
        if(counter == counterLimit -1){
            counter = 0;
            gameInstructions.moveToNextInstruction();
        }
        else if(counter < counterLimit){
            counter++;
            onClickRunnable.run();
        }
    }


    public void reset(){
        counter = 0;
    }
}
