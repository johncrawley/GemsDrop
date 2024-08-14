package com.jcrawleydev.gemsdrop.service.rotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.service.DroppingGems;
import com.jcrawleydev.gemsdrop.service.RotationChecker;
import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

import org.junit.Before;
import org.junit.Test;

public class RotationCheckerTest {


    private RotationChecker rotationChecker;
    private DroppingGems droppingGems;
    private int maxColumnIndex;


    @Before
    public void init(){
        int[] columnHeights = new int[]{0,0,7,0,0,7,0};
        GemGrid gemGrid = new MockGemGrid(columnHeights);
        int numberOfColumns = columnHeights.length;
        maxColumnIndex = numberOfColumns -1;

        int NUMBER_OF_ROWS = 14;
        rotationChecker = new RotationChecker(gemGrid, NUMBER_OF_ROWS);
        droppingGems = new DroppingGems(NUMBER_OF_ROWS, numberOfColumns);
        droppingGems.create();
    }


    @Test
    public void cannotRotateWhenVerticalAndAtEdgePositions(){
        droppingGems.setColumn(0);
        assertFalse(rotationChecker.canRotate(droppingGems));

        droppingGems.setColumn(1);
        assertTrue(rotationChecker.canRotate(droppingGems));

        droppingGems.setColumn(6);
        assertFalse(rotationChecker.canRotate(droppingGems));
    }


    @Test
    public void canRotateWhenHorizontalAndAtEdgePositions(){
        droppingGems.rotate();
        while(droppingGems.getLeftmostColumn() > 0){
            droppingGems.moveLeft();
        }
        assertEquals(0, droppingGems.getLeftmostColumn());
        assertCanRotate();


        while(droppingGems.getRightmostColumn() < maxColumnIndex){
            droppingGems.moveRight();
        }
        assertEquals(maxColumnIndex, droppingGems.getRightmostColumn());
        assertCanRotate();
    }


    @Test
    public void cannotRotateWhenVerticalAndToTheRightOfAColumn(){
        droppingGems.setColumn(3); // just after the column of height 7
        assertCanRotate();
        System.out.println("lowest gem position: " + droppingGems.getLowestHeight());
        dropToDepth(14);
        assertCanRotate();
        droppingGems.drop();
        assertCannotRotate();
    }


    private void log(String msg){
        System.out.println("^^^ RotationCheckerTest: " + msg);
    }



    private void dropToDepth(int depth){
        while(droppingGems.getBottomDepth() < depth){
            droppingGems.drop();
        }
    }


    private void assertCanRotate(){
        assertTrue(rotationChecker.canRotate(droppingGems));
    }


    private void assertCannotRotate(){
        assertFalse(rotationChecker.canRotate(droppingGems));
    }

}
