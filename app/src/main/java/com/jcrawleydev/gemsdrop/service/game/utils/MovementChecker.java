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
        log("canMoveLeft() ColumnIndex: " + nextColumnIndex);
        return nextColumnIndex >= 0 && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }


    public boolean canMoveDown(DroppingGems droppingGems){
       return droppingGems.getBottomGem().getContainerPosition() > 0;
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
