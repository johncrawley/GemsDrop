package com.jcrawleydev.gemsdrop.service;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

public class RotationChecker {


    private final GemGrid gemGrid;
    private final int numberOfRows;

    public RotationChecker(GemGrid gemGrid, int numberOfRows){
        this.gemGrid = gemGrid;
        this.numberOfRows = numberOfRows;
    }


    public boolean canRotate(DroppingGems droppingGems){
        if(droppingGems.isOrientationVertical()){
            if(isAtBoundary(droppingGems) || areDroppingGemsObstructedByColumns(droppingGems)){
                return false;
            }
        }
        return true;
    }


    private boolean areDroppingGemsObstructedByColumns(DroppingGems droppingGems){
        return isGemAdjacentToColumn(droppingGems.getBottomGem(), -1);
             //   || isGemAdjacentToColumn(droppingGems.getCentreGem(), 1);
    }


    private boolean isGemAdjacentToColumn(Gem gem, int columnOffset){

        int gemColumnHeight = gemGrid.getHeightAtColumn(gem.getColumn() + columnOffset);
        log(" isGemAdjacentToColumn() gemBottomDepth: " + gem.getBottomDepth() + " columnHeight: " + gemColumnHeight);
        return GemUtils.isGemAdjacentToColumn(gem, gemColumnHeight, numberOfRows);
    }


    private void log(String msg){
        System.out.println("^^^ RotationChecker: " + msg);
    }


    public boolean isAtBoundary(DroppingGems droppingGems){
       return droppingGems.getLeftmostColumn() == 0
               ||  droppingGems.getRightmostColumn() >= gemGrid.getNumberOfColumns() -1;
    }
}
