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
        for(List<Gem> gemColumn : gemGrid.getGemColumns()){
            setCurrentLevelGrey(gemColumn);
        }
        currentLevel++;
    }


    private void setCurrentLevelGrey(List<Gem> gemColumn){
        if(gemColumn != null && gemColumn.size() > currentLevel){
            gemColumn.get(currentLevel).setGrey();
        }
    }

}
