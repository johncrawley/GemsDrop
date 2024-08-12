package com.jcrawleydev.gemsdrop.service.rotation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jcrawleydev.gemsdrop.gem.Gem;
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
        int[] columnHeights = new int[]{0,0,1,7,1,7,0};
        GemGrid gemGrid = new MockGemGrid(columnHeights);
        int numberOfColumns = columnHeights.length;
        maxColumnIndex = numberOfColumns -1;

        rotationChecker = new RotationChecker(gemGrid);
        droppingGems = new DroppingGems(14, numberOfColumns);
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
        droppingGems.setColumn(4);
        assertCanRotate();
        System.out.println("lowest gem position: " + droppingGems.getLowestHeight());
        droppingGems.drop();
        for(Gem gem: droppingGems.get()){
            System.out.println("gem depth after drop: " + gem.getDepth());
        }
    }


    private void assertCanRotate(){
        assertTrue(rotationChecker.canRotate(droppingGems));
    }

}
