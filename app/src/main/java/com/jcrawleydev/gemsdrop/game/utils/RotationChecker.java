package com.jcrawleydev.gemsdrop.game.utils;
import com.jcrawleydev.gemsdrop.game.gem.dropping.DroppingGems;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;

public class RotationChecker {

    private final GemGrid gemGrid;
    private final GridProps gridProps;

    public RotationChecker(GemGrid gemGrid, GridProps gridProps){
        this.gemGrid = gemGrid;
        this.gridProps = gridProps;
    }


    public boolean canRotate(DroppingGems droppingGems){
        if(droppingGems.areAnyAddedToGrid()){
            return false;
        }
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
        return gem.getContainerPosition() >= columnHeight + gridProps.depthPerDrop();
    }


    private int getColumnHeightUnder(Gem gem){
        return gemGrid.getColumnHeightAt(gem.getColumn());
    }


    private boolean areDroppingGemsObstructedByColumns(DroppingGems droppingGems){
        return isGemAdjacentToColumn(droppingGems.getBottomGem(), -1)
               || isGemAdjacentToColumn(droppingGems.getCentreGem(), 1);
    }


    private boolean isGemAdjacentToColumn(Gem gem, int columnOffset){
        int gemColumnHeight = gemGrid.getColumnHeightAt(gem.getColumn() + columnOffset);
        return gem.getContainerPosition() < gemColumnHeight;
    }


    public boolean isAtBoundary(DroppingGems droppingGems){
       return droppingGems.getLeftmostColumn() == 0
               ||  droppingGems.getRightmostColumn() >= gridProps.numberOfColumns() -1;
    }
}
