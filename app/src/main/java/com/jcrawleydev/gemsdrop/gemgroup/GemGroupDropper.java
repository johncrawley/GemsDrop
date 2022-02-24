package com.jcrawleydev.gemsdrop.gemgroup;


public class GemGroupDropper {

    private final GemGroup gemGroup;
    private int dropIncrement;
    private float dropFactor = 0f;
    private float currentDropIncrement = 0f;
    private final int gemWidth;
    private int currentCount;
    private boolean isQuickDropEnabled = false;


    public GemGroupDropper(GemGroup gemGroup, int gemWidth){
        this.gemGroup = gemGroup;
        this.gemWidth = gemWidth;
        setDropFactor(0.5f);
    }


    public void enableQuickDrop(){
        isQuickDropEnabled = true;
        setDropFactor(1f);
    }


    public void drop(){
        log("Entered drop()");
        if(!shouldAnimate()){
            return;
        }
        currentDropIncrement += dropFactor;
        while(currentDropIncrement >= 1){
            log("While loop iteration!");
            currentDropIncrement -= 1;
            gemGroup.decrementMiddleYPosition();
        }
        gemGroup.dropBy(dropIncrement);
        log("Exiting drop()");
    }

    private void log(String msg){
        System.out.println("^^^ GemGroupDropper: " + msg);
    }


    private boolean shouldAnimate(){
        return isQuickDropEnabled || isUpdateScheduled();
    }


    private boolean isUpdateScheduled(){
        final int UPDATE_FACTOR = 5;
        currentCount++;
        if(currentCount < UPDATE_FACTOR){
            return false;
        }
        currentCount = 0;
        return true;
    }


    private void setDropFactor(float dropFactor){
        this.dropFactor = dropFactor;
        this.dropIncrement = (int)(gemWidth * dropFactor);
    }

}
