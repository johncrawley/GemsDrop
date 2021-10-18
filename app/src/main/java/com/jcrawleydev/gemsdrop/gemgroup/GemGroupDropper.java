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
        if(!shouldAnimate()){
            return;
        }
        currentDropIncrement += dropFactor;
        while(currentDropIncrement >= 1){
            currentDropIncrement -= 1;
            gemGroup.decrementMiddleYPosition();
        }
        gemGroup.dropBy(dropIncrement);
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
