package com.jcrawleydev.gemsdrop.gemgroup;


public class GemGroupDropper {

    private final GemGroup gemGroup;
    private float dropIncrement;
    private float dropFactor = 0.5f;
    private float currentDropIncrement = 0f;
    private final float gemWidth;
    private int currentCount;
    private boolean isQuickDropEnabled = false;


    public GemGroupDropper(GemGroup gemGroup, float gemWidth){
        this.gemGroup = gemGroup;
        this.gemWidth = gemWidth;
        setDropFactor(0.5f);
    }


    public void enableQuickDrop(){
        isQuickDropEnabled = true;
        //setDropFactor(1f);
    }

    public boolean isQuickDropEnabled(){
        return isQuickDropEnabled;
    }


    public void drop(){
        /*
        if(!shouldAnimate()){
            return;
        }
        currentDropIncrement += dropFactor;
        if(currentDropIncrement >= 1){
            currentDropIncrement = 0;
            gemGroup.decrementMiddleYPosition();
        }

         */
        gemGroup.decrementMiddleYPosition();
        gemGroup.dropBy(dropIncrement);
    }


    private boolean shouldAnimate(){
        return isQuickDropEnabled || isUpdateScheduled();
    }


    private boolean isUpdateScheduled(){
        int updateFactor = 5;
        currentCount++;
        if(currentCount < updateFactor){
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
