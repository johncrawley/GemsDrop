package com.jcrawleydev.gemsdrop.service;

import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

public class RotationChecker {


    private GemGrid gemGrid;

    public RotationChecker(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }


    public boolean canRotate(DroppingGems droppingGems){

        if(droppingGems.isOrientationVertical() && (droppingGems.getLeftmostColumn() == 0
            ||  droppingGems.getRightmostColumn() >= gemGrid.getNumberOfColumns() -1)){
            return false;
        }
        return true;
    }
}
