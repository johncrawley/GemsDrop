package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.service.DroppingGems;
import com.jcrawleydev.gemsdrop.service.GridProps;
import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

public class MovementChecker {

    private final GridProps gridProps;
    private final GemGrid gemGrid;

    public MovementChecker(GemGrid gemGrid, GridProps gridProps){
        this.gridProps = gridProps;
        this.gemGrid = gemGrid;
    }


    public boolean canMoveLeft(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getLeftmostColumn() - 1;
        log("canMoveLeft() ColumnIndex: " + nextColumnIndex);
        return nextColumnIndex >= 0 && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }



    public boolean canMoveRight(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getRightmostColumn() + 1;
        log("canMoveRight() nextColumnIndex: " + nextColumnIndex);

        return nextColumnIndex < gridProps.numberOfColumns()
         && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }



    private void log(String msg){
        System.out.println("^^^ MovementChecker: " + msg);
    }


    private boolean isUnobstructedByColumn(DroppingGems droppingGems, int adjacentColumnIndex){
        int columnHeight = gemGrid.getHeightOfColumn(adjacentColumnIndex);
        log("isUnobstructedByColumn() col Height: " + columnHeight + " droppingGems Bottom height: " + droppingGems.getBottomHeight());
        return columnHeight <= droppingGems.getBottomHeight();
    }

}
