package com.jcrawleydev.gemsdrop.service.validation;

import static com.jcrawleydev.gemsdrop.service.validation.Utils.dropToAboveGridRow;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.service.DroppingGems;
import com.jcrawleydev.gemsdrop.service.GemUtils;
import com.jcrawleydev.gemsdrop.service.GridProps;
import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

import org.junit.Before;
import org.junit.Test;

public class RotationCheckerTest {


    private RotationChecker rotationChecker;
    private DroppingGems droppingGems;
    private int maxColumnIndex;
    private final GridProps gridProps = new GridProps(14,7,2);


    @Before
    public void init(){
        int[] columnHeights = new int[]{0,0,7,0,0,7,0};
        GemGrid gemGrid = new MockGemGrid(gridProps, columnHeights);
        int numberOfColumns = columnHeights.length;
        maxColumnIndex = numberOfColumns -1;
        rotationChecker = new RotationChecker(gemGrid, gridProps);
        droppingGems = new DroppingGems(gridProps);
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
        dropToAboveGridRow(droppingGems, 7);
        assertCanRotate();
        droppingGems.drop();
        assertCannotRotate();
    }


    @Test
    public void cannotRotateWhenVerticalAndToTheLeftOfAColumn(){
        droppingGems.setColumn(4); // just before the second column of height 7
        assertCanRotate();
        dropToAboveGridRow(droppingGems, 7);
        assertEquals(16, GemUtils.getBottomHeightOf(droppingGems.getCentreGem(), gridProps));
        assertCanRotate();
        droppingGems.drop(); // the lowest dropping gem is now half a gem beneath the top of the column
        assertCanRotate();
        droppingGems.drop(); // the lowest dropping gem is now adjacent to the top gem in the column
        assertCanRotate();
        droppingGems.drop(); // the middle dropping gem is now half a gem beneath the top of the column
        assertCannotRotate();
    }


    @Test
    public void cannotRotateWhenHorizontalAndAboveColumns(){
        droppingGems.rotate();
        assertCanRotate();
        droppingGems.setColumn(2); //above the first column 7 gems high
        dropToAboveGridRow(droppingGems, 8);
        assertCanRotate();
        droppingGems.drop();
        assertCannotRotate();

        droppingGems.rotate();
        droppingGems.setColumn(5);
        droppingGems.rotate();
        assertCannotRotate();
    }


    private void assertCanRotate(){
        assertTrue(rotationChecker.canRotate(droppingGems));
    }


    private void assertCannotRotate(){
        assertFalse(rotationChecker.canRotate(droppingGems));
    }

}
