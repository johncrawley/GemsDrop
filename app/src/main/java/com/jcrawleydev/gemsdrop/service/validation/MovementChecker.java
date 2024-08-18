package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.service.DroppingGems;
import com.jcrawleydev.gemsdrop.service.GridProps;
import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

public class MovementChecker {

    private final GridProps gridProps;
    private final GemGrid gemGrid;

    public MovementChecker(GridProps gridProps, GemGrid gemGrid){
        this.gridProps = gridProps;
        this.gemGrid = gemGrid;
    }


    public boolean canMoveLeft(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getLeftmostColumn() - 1;
        return nextColumnIndex >= 0 && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }



    public boolean canMoveRight(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getRightmostColumn() + 1;
        return nextColumnIndex < gridProps.numberOfColumns()
         && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }


    private boolean isUnobstructedByColumn(DroppingGems droppingGems, int adjacentColumnIndex){
        int columnHeight = gemGrid.getHeightOfColumn(adjacentColumnIndex);
        return columnHeight <= droppingGems.getBottomHeight();
    }

}
