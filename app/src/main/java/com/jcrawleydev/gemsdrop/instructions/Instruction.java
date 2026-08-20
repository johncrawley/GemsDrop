package com.jcrawleydev.gemsdrop.instructions;

public class Instruction {

    int counter;
    private final int counterLimit;
    private final Runnable onStartRunnable, onClickRunnable;
    final GameInstructions gameInstructions;


    public Instruction(int counterLimit, Runnable onStart, Runnable onClick, GameInstructions gameInstructions){
        this.counterLimit = counterLimit;
        this.onStartRunnable = onStart;
        this.onClickRunnable = onClick;
        this.gameInstructions = gameInstructions;
    }


    public void setOnStart(){
        onStartRunnable.run();
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
