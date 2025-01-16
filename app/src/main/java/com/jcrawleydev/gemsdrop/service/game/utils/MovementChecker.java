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
        return !droppingGems.areAnyAddedToGrid()
                && nextColumnIndex >= 0
                && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }


    public boolean canMoveDown(DroppingGems droppingGems){
        int position = droppingGems.getBottomGem().getContainerPosition();
        boolean areAllGemsAboveColumns = droppingGems.get().stream().allMatch( gem -> gem.getContainerPosition() > gemGrid.getColumnHeightAt(gem.getColumn()));
        return position > 0; // && areAllGemsAboveColumns;
    }


    public boolean canMoveRight(DroppingGems droppingGems){
        int nextColumnIndex = droppingGems.getRightmostColumn() + 1;
        log("canMoveRight() nextColumnIndex: " + nextColumnIndex);

        return !droppingGems.areAnyAddedToGrid()
            && nextColumnIndex < gridProps.numberOfColumns()
            && isUnobstructedByColumn(droppingGems, nextColumnIndex);
    }


    private void log(String msg){
        System.out.println("^^^ MovementChecker: " + msg);
    }


    private boolean isUnobstructedByColumn(DroppingGems droppingGems, int adjacentColumnIndex){
        int columnHeight = gemGrid.getColumnHeightAt(adjacentColumnIndex);
        log("isUnobstructedByColumn() col Height: " + columnHeight + " droppingGems Bottom height: " + droppingGems.getLowestGemPosition());
        return columnHeight <= droppingGems.getLowestGemPosition();
    }

}
