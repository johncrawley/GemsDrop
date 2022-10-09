package com.jcrawleydev.gemsdrop.gemgrid;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.List;

public class HeightExceededAnimator {

    private int currentLevel;
    private final GemGrid gemGrid;

    public HeightExceededAnimator(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }

    public void resetLevel(){
        currentLevel = 0;
    }


    public boolean haveAllLevelsBeenChanged(){
        return currentLevel > gemGrid.getHighestColumnIndex();
    }


    public void turnNextGridLevelGrey(){
        log("Entered turnNextGridLevelGrey() current level: " + currentLevel + " number of columns: " + gemGrid.getGemColumns().size());
        for(List<Gem> gemColumn : gemGrid.getGemColumns()){
            setCurrentLevelGrey(gemColumn);
        }
        currentLevel++;
    }


    private void setCurrentLevelGrey(List<Gem> gemColumn){
        if(gemColumn == null){
            return;
        }
        if(gemColumn.size() > currentLevel) {
            gemColumn.get(currentLevel).setGrey();
        }
    }


    private void log(String msg){
        System.out.println("HeightExceededAnimator: " + msg);
    }
}
