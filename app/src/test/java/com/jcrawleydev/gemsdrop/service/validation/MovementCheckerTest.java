package com.jcrawleydev.gemsdrop.service.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.service.DroppingGems;
import com.jcrawleydev.gemsdrop.service.GridProps;

import org.junit.Before;
import org.junit.Test;

public class MovementCheckerTest {

    private final GridProps gridProps = new GridProps(7, 14, 2);
    private MovementChecker movementChecker;
    private DroppingGems droppingGems;

    @Before
    public void init(){
        movementChecker = new MovementChecker(gridProps);
        droppingGems = new DroppingGems(gridProps);
        droppingGems.create();
    }


    @Test
    public void canDetectBoundaries(){
        droppingGems.setColumn(1);
        assertLeftMovement(true);
        droppingGems.moveLeft();
        assertLeftMovement(false);

        int lastColumnIndex = gridProps.numberOfColumns() - 1;
        droppingGems.setColumn(lastColumnIndex - 1);
        assertRightMovement(true);
        droppingGems.moveRight();
        assertRightMovement(false);
    }


    private void assertLeftMovement(boolean expectedResult){
        assertEquals(expectedResult, movementChecker.canMoveLeft(droppingGems));
    }


    private void assertRightMovement(boolean expectedResult){
        assertEquals(expectedResult, movementChecker.canMoveRight(droppingGems));
    }
}
