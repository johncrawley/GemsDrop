package com.jcrawleydev.gemsdrop.service.game.utils;
import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.gem.GemUtils;
import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;

public class RotationChecker {


    private final GemGrid gemGrid;
    private final GridProps gridProps;

    public RotationChecker(GemGrid gemGrid, GridProps gridProps){
        this.gemGrid = gemGrid;
        this.gridProps = gridProps;
    }


    public boolean canRotate(DroppingGems droppingGems){
        if(!droppingGems.isOrientationVertical()){
            if(droppingGems.getCentreGem().getContainerPosition() < 2){
                return false;
            }
        }
        if(droppingGems.isOrientationVertical()){
            return !isAtBoundary(droppingGems) && !areDroppingGemsObstructedByColumns(droppingGems);
        }
        return isEnoughSpaceUnderneath(droppingGems.getCentreGem())
                && isEnoughSpaceUnderneath(droppingGems.getRightmostGem());
    }


    public boolean isEnoughSpaceUnderneath(Gem gem){
        int columnHeight = getColumnHeightUnder(gem);
        int gemBottomHeight = GemUtils.getBottomHeightOf(gem, gridProps);
        return gemBottomHeight >= columnHeight + gridProps.depthPerDrop();
    }


    private int getColumnHeightUnder(Gem gem){
        return gemGrid.getHeightOfColumn(gem.getColumn());
    }

    private boolean areDroppingGemsObstructedByColumns(DroppingGems droppingGems){
        return isGemAdjacentToColumn(droppingGems.getBottomGem(), -1)
               || isGemAdjacentToColumn(droppingGems.getCentreGem(), 1);
    }


    private void log(String msg){
        System.out.println("^^^ RotationChecker: " + msg);
    }


    private boolean isGemAdjacentToColumn(Gem gem, int columnOffset){
        if(columnOffset == 1){
            log("Entered isGemAdjacentToColumn() checking centre gem against right column");
            log("centreGem container position: "+  gem.getContainerPosition());
        }
        int gemColumnHeight = gemGrid.getHeightOfColumn(gem.getColumn() + columnOffset);
        return GemUtils.isGemAdjacentToColumn(gem, gemColumnHeight, gridProps);
    }


    public boolean isAtBoundary(DroppingGems droppingGems){
       return droppingGems.getLeftmostColumn() == 0
               ||  droppingGems.getRightmostColumn() >= gemGrid.getNumberOfColumns() -1;
    }
}
