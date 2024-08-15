package com.jcrawleydev.gemsdrop.service;

public class MovementChecker {

    private final GridProps gridProps;

    public MovementChecker(GridProps gridProps){
        this.gridProps = gridProps;
    }

    public boolean canMoveLeft(DroppingGems droppingGems){
        return false;
    }


    public boolean canMoveRight(DroppingGems droppingGems){
        return false;
    }
}
