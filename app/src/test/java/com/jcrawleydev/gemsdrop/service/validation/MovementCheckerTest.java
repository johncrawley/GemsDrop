package com.jcrawleydev.gemsdrop.service.validation;

import static org.junit.Assert.assertEquals;

import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;
import com.jcrawleydev.gemsdrop.game.GridProps;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.utils.MovementChecker;

import org.junit.Before;
import org.junit.Test;

public class MovementCheckerTest {

    private final GridProps gridProps = new GridProps(14, 7, 2);
    private MovementChecker movementChecker;
    private DroppingGems droppingGems;
    private GemGrid gemGrid;

    @Before
    public void init(){
        gemGrid = new MockGemGrid(gridProps, 0,0,0,9,0,0,0);
        movementChecker = new MovementChecker(gemGrid, gridProps);
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


    @Test
    public void cannotMovePastObstructingGemColumns(){
        droppingGems.setColumn(1);
        Utils.dropToAboveGridRow(droppingGems, 8);
        assertRightMovement(true);
        droppingGems.moveRight();
        assertRightMovement(false);

        droppingGems.setColumn(5);
        assertLeftMovement(true);
        droppingGems.moveLeft();
        assertLeftMovement(false);

    }



    private void assertLeftMovement(boolean expectedResult){
        assertEquals(expectedResult, movementChecker.canMoveLeft(droppingGems));
    }


    private void assertRightMovement(boolean expectedResult){
        assertEquals(expectedResult, movementChecker.canMoveRight(droppingGems));
    }
}
