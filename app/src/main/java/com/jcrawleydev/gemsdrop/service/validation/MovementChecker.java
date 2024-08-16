package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.service.DroppingGems;
import com.jcrawleydev.gemsdrop.service.GridProps;

public class MovementChecker {

    private final GridProps gridProps;

    public MovementChecker(GridProps gridProps){
        this.gridProps = gridProps;
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
