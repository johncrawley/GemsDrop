package com.jcrawleydev.gemsdrop.instructions;

public class InstructionState {

    private int counter;
    private final int counterLimit;
    private final Runnable onStartRunnable, onClickRunnable;
    private final GameInstructions gameInstructions;


    public InstructionState(int counterLimit, Runnable onStart, Runnable onClick, GameInstructions gameInstructions){
        this.counterLimit = counterLimit;
        this.onStartRunnable = onStart;
        this.onClickRunnable = onClick;
        this.gameInstructions = gameInstructions;
    }


    public void setOnStart(){
        onStartRunnable.run();
    }


    public void onClick(){
        counter++;
        if(counter >= counterLimit){
            counter = 0;
            gameInstructions.moveToNextInstruction();
            return;
        }
        onClickRunnable.run();
    }

}
