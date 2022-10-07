package com.jcrawleydev.gemsdrop.gameState.dropcounter;

public class DropCounter {

    private int dropCount;

    public int get(){
        return dropCount;
    }

    public void reset(){
        dropCount = 0;
    }

    public void increment(){
        dropCount++;
    }
}
