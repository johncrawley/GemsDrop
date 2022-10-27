package com.jcrawleydev.gemsdrop.control;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;
import com.jcrawleydev.gemsdrop.gemgroup.Utils;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GemControlsTest {

    private static GemGroupFactory gemGroupFactory;
    private static GemGrid gemGrid;
    private GemControls gemControls;
    private GemGroup gemGroup;
    private final static int numberOfGemsPerGroup = 3;
    private int rightmostIndexForHorizontal = 0;
    private final int initialPosition = 5;
    private static int borderWidth;

    @BeforeClass
    public static void initialSetup(){
        gemGrid = new GemGrid(8, 12);
        int gemWidth = 150;
        borderWidth = gemWidth/2;
        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(16)
                .withGemWidth(gemWidth)
                .withNumberOfGems(numberOfGemsPerGroup)
                .withInitialPosition(0)
                .withFloorAt(1300)
                .withBorderWidth(borderWidth)
                .build();
    }


    @Before
    public void setup(){
        gemGroup = gemGroupFactory.createGemGroup();
        gemControls = new GemControls(gemGroup, gemGrid);
        gemGroup.setXPosition(initialPosition);
        gemGrid.clear();
        rightmostIndexForHorizontal = (gemGrid.getNumberOfColumns() -1) - gemGroup.getNumberOfGems() /2;
    }


    @Test
    public void canMoveGemGroupLeftAndRight(){
        gemGroup.setXPosition(initialPosition);
        gemControls.moveLeft();
        assertEquals(initialPosition -1, gemGroup.getXPosition() );
        gemControls.moveLeft();
        assertEquals(initialPosition -2, gemGroup.getXPosition() );

        gemControls.moveRight();
        assertEquals(initialPosition -1 , gemGroup.getXPosition() );
        gemControls.moveRight();
        assertEquals(initialPosition, gemGroup.getXPosition());
        gemControls.moveRight();
        assertEquals(initialPosition + 1, gemGroup.getXPosition());
    }


    @Test
    public void cannotMoveLeftIfVerticalAndAtFirstPosition(){

        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setXPosition(0);
        gemControls.moveLeft();
        assertPosition(0);
    }


    @Test
    public void cannotMoveLeftIfHorizontalAndFirstGemIsAtLeftEdge(){
        int horizontalLeftLimit = numberOfGemsPerGroup /2;
        gemGroup.rotateToHorizontal();
        gemGroup.setXPosition(horizontalLeftLimit);
        gemControls.moveLeft();
        assertPosition(horizontalLeftLimit);
    }


    @Test(timeout = 500)
    public void cannotMoveRightIfVerticalAndAtLastPosition(){
        gemGroup.rotateToVertical();
        int rightmostIndexForVertical = gemGrid.getNumberOfColumns() -1;
        gemGroup.setXPosition(rightmostIndexForVertical);
        gemControls.moveRight();
        assertPosition(rightmostIndexForVertical);
    }


    @Test(timeout = 1000)
    public void cannotMoveRightIfHorizontalAndAtLastPosition(){
        gemGroup.rotateToHorizontal();
        gemGroup.setXPosition(rightmostIndexForHorizontal);
        gemControls.moveRight();
        assertPosition(rightmostIndexForHorizontal);
    }


    @Test
    public void canRotateInDefaultPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setXPosition(5);
        gemControls.rotate();
        assertEquals(GemGroup.Orientation.HORIZONTAL, gemGroup.getOrientation());
    }


    @Test
    public void cannotRotateIfVerticalInFirstPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setXPosition(0);
        gemControls.rotate();
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
    }


    @Test
    public void cannotRotateIfVerticalInLastPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        moveVerticalToRightEdge();
        gemControls.rotate();
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
    }


    @Test(timeout = 1000)
    public void cannotRotateIfVerticalAndColumnTooCloseToTheRight(){
        int columnIndex = 3;
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setXPosition(columnIndex -1);
        addVerticalGemsToGridAtPosition(columnIndex);
        // the already-placed gems should be in y position 0,1,2
        // if the dropping gemGroups lowest gem is adjacent to position 2, it should still be possible to rotate the gem group, as rotations are clockwise
        //  but when the lowest gem in the dropping gemGroup is at position 1,
        //   then the top gem in the dropping group could not rotate without colliding with the top gem in the stationary column
        dropGemGroupTo(1);
        gemControls.rotate();
        assertVerticalOrientation();
    }


    @Test(timeout = 1000)
    public void canRotateIfVerticalAndCloseToAColumn(){
        int columnIndex = 3;
        int safeDistance = 2;
        gemGroup.rotateToVertical();
        gemGroup.setXPosition(columnIndex + safeDistance);
        addVerticalGemsToGridAtPosition(columnIndex);
        addVerticalGemsToGridAtPosition(columnIndex);
        dropGemGroupTo(5);
        assertRotationToHorizontal();

        gemGroup.setXPosition(columnIndex - safeDistance);
        gemGroup.rotateToVertical();
        assertRotationToHorizontal();
    }


    @Test(timeout = 1000)
    public void canRotateIfAdjacentAndAboveAColumn(){
        int columnIndex = 3;
        int distance = 1;
        gemGroup.rotateToVertical();
        gemGroup.setXPosition(columnIndex - distance);
        addVerticalGemsToGridAtPosition(columnIndex);
        addVerticalGemsToGridAtPosition(columnIndex);
        dropGemGroupTo(6);
        assertRotationToHorizontal();
    }


    private void assertRotationToHorizontal(){
        gemControls.rotate();
        assertEquals(GemGroup.Orientation.HORIZONTAL, gemGroup.getOrientation());
    }


    @Test(timeout = 1000)
    public void cannotRotateIfVerticalAndColumnTooCloseToTheLeft(){
        int columnIndex = 3;
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setXPosition(columnIndex + 1);

        addVerticalGemsToGridAtPosition(columnIndex);
        dropGemGroupTo(2);
        gemControls.rotate();
        assertVerticalOrientation();
    }


    @Test
    public void cannotMoveLeftIfColumnToTheLeft(){
        int dropPosition = 5;
        gemGroup.setXPosition(dropPosition);
        addVerticalGemsToGridAtPosition(dropPosition -1);
        dropGemGroupTo(4);
        gemControls.moveLeft();
        assertPosition(dropPosition-1);
        gemControls.moveRight();
        assertPosition(dropPosition);
        dropGemGroupTo(3);
        gemControls.moveLeft();
        assertPosition(dropPosition);
    }


    @Test(timeout = 1000)
    public void cannotMoveRightIfColumnToTheRight(){
        int dropPosition = 5;
        gemGroup.setXPosition(dropPosition);
        addVerticalGemsToGridAtPosition(dropPosition +1);
        dropGemGroupTo(4);
        gemControls.moveRight();
        assertPosition(dropPosition +1);
        gemControls.moveLeft();
        dropGemGroupTo(3);
        gemControls.moveRight();
        assertPosition(dropPosition);
    }


    private void assertVerticalOrientation(){
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
    }


    private void dropGemGroupTo(int yBottomPosition){
        while(gemGroup.getRealBottomPosition() > yBottomPosition){
            gemGroup.drop();
        }
    }


    private void addVerticalGemsToGridAtPosition(int position){
        GemGroup gemGroupToAdd = Utils.createGemGroup(gemGrid, position, GemGroup.Orientation.VERTICAL, Gem.Color.RED, Gem.Color.BLUE, Gem.Color.GREEN, borderWidth);
        gemGrid.add(gemGroupToAdd);
    }


    private void moveRightTillAtPosition(int position){
        while(gemGroup.getXPosition() < position){
            gemControls.moveRight();
        }
        assertPosition(position);
    }


    private void moveVerticalToRightEdge(){
        moveRightTillAtPosition(gemGrid.getNumberOfColumns()-1);
    }


    private void moveLeftTillAtPosition(int position){
        while(gemGroup.getXPosition() > position){
            gemControls.moveLeft();
        }
        assertPosition(position);
    }


    private void assertPosition(int expected){
        assertEquals(expected, gemGroup.getXPosition());
    }


    private void changeAndAssertOrientation(GemGroup.Orientation orientation){
        if(gemGroup.getOrientation() != orientation){
            gemGroup.rotate();
        }
        assertEquals(orientation, gemGroup.getOrientation());
    }


}
