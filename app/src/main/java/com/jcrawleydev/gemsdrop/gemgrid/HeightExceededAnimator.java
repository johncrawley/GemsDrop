package com.jcrawleydev.gemsdrop.gemgrid;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.List;

public class HeightExceededAnimator {

    private int currentLevel;
    private final GemGrid gemGrid;

    public HeightExceededAnimator(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }

    public void turnLevelGrey(){
        for(List<Gem> gemColumn : gemGrid.getGemColumns()){
            gemColumn.get(currentLevel).setGrey();
        }
        currentLevel++;
    }
}
