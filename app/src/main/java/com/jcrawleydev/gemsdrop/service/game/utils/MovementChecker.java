package com.jcrawleydev.gemsdrop.service.game.utils;

import com.jcrawleydev.gemsdrop.service.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;

public class MovementChecker {

    private final GridProps gridProps;
    private final GemGrid gemGrid;

    public MovementChecker(GemGrid gemGrid, GridProps gridProps){
        this.gridProps = gridProps;
        this.gemGrid = gemGrid;
    }


    public boolean canMoveLeft(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getLeftmostColumn() - 1;
        return !droppingGems.areAnyAddedToGrid()
                && nextColumnIndex >= 0
                && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }


    public boolean canMoveRight(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getRightmostColumn() + 1;

        return !droppingGems.areAnyAddedToGrid()
            && nextColumnIndex < gridProps.numberOfColumns()
            && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }


    private boolean isUnobstructedByColumn(DroppingGems droppingGems, int adjacentColumnIndex){
        int columnHeight = gemGrid.getColumnHeightAt(adjacentColumnIndex);
        return columnHeight <= droppingGems.getLowestGemPosition();
    }

}
