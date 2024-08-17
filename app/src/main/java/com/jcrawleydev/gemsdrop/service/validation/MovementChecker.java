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
        if(droppingGems.getLeftmostColumn() == 0){
            return false;
        }
        return true;
    }


    public boolean canMoveRight(DroppingGems droppingGems){
        if(droppingGems.getRightmostColumn() >= gridProps.numberOfColumns() -1){
            return false;
        }
        return true;
    }
}
