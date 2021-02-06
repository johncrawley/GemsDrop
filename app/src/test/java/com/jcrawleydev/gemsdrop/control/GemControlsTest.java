package com.jcrawleydev.gemsdrop.control;

import com.jcrawleydev.gemsdrop.GemGrid;
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
    private static int numberOfGemsPerGroup = 3;
    private int rightmostIndexForHorizontal = 0;

    @BeforeClass
    public static void initialSetup(){
        gemGrid = new GemGrid(8, 12);
        int gemWidth = 150;
        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(16)
                .withGemWidth(gemWidth)
                .withNumerOfGems(numberOfGemsPerGroup)
                .withInitialPosition(0)
                .withFloorAt(1300)
                .build();
    }


    @Before
    public void setup(){
        gemGroup = gemGroupFactory.createGemGroup();
        gemControls = new GemControls(gemGroup, gemGrid);

        rightmostIndexForHorizontal = (gemGrid.getNumberOfColumns() -1) - gemGroup.getNumberOfGems() /2;
    }


    @Test
    public void canMoveGemGroupLeftAndRight(){
        int initialPosition = gemGroup.getPosition();
        gemControls.moveLeft();
        assertEquals(initialPosition -1, gemGroup.getPosition() );
        gemControls.moveLeft();
        assertEquals(initialPosition -2, gemGroup.getPosition() );

        gemControls.moveRight();
        assertEquals(initialPosition -1 , gemGroup.getPosition() );
        gemControls.moveRight();
        assertEquals(initialPosition, gemGroup.getPosition());
        gemControls.moveRight();
        assertEquals(initialPosition + 1, gemGroup.getPosition());
    }


    @Test
    public void cannotMoveLeftIfVerticalAndAtFirstPosition(){

        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        moveLeftTillAtPosition(0);
        gemControls.moveLeft();
        assertPosition(0);
    }


    @Test
    public void cannotMoveLeftIfHorizontalAndFirstGemIsAtLeftEdge(){
        changeAndAssertOrientation(GemGroup.Orientation.HORIZONTAL);

        int horizontalLeftLimit = numberOfGemsPerGroup /2;
        moveLeftTillAtPosition(horizontalLeftLimit);
        gemControls.moveLeft();
        assertPosition(horizontalLeftLimit);
    }


    @Test
    public void cannotMoveRightIfVerticalAndAtLastPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        int rightmostIndexForVertical = gemGrid.getNumberOfColumns() -1;
        moveRightTillAtPosition(rightmostIndexForVertical);
        gemControls.moveRight();
        assertPosition(rightmostIndexForVertical);
    }


    @Test
    public void cannotMoveRightIfHorizontalAndAtLastPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.HORIZONTAL);
        moveRightTillAtPosition(rightmostIndexForHorizontal);
        gemControls.moveRight();
        assertPosition(rightmostIndexForHorizontal);
    }


    @Test
    public void canRotateInDefaultPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemControls.rotate();
        assertEquals(GemGroup.Orientation.HORIZONTAL, gemGroup.getOrientation());

    }


    @Test
    public void cannotRotateIfVerticalInFirstPosition(){
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        moveLeftTillAtPosition(0);
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
        int dropPosition = 3;
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setPosition(dropPosition);
        addVerticalGemsToGridAtPosition(dropPosition+1);
        // the already-placed gems should be in y position 0,1,2
        // if the dropping gemGroups lowest gem is adjacent to position 2, it should still be possible to rotate the gem group, as rotations are clockwise
        //  but when the lowest gem in the dropping gemGroup is at position 1,
        //   then the top gem in the dropping group could not rotate without colliding with the top gem in the stationary column
        dropGemGroupTo(1);
        gemControls.rotate();
        assertVerticalOrientation();
    }


    @Test(timeout = 1000)
    public void cannotRotateIfVerticalAndColumnTooCloseToTheLeft(){
        int dropPosition = 3;
        changeAndAssertOrientation(GemGroup.Orientation.VERTICAL);
        gemGroup.setPosition(dropPosition);

        addVerticalGemsToGridAtPosition(dropPosition -1);
        dropGemGroupTo(1);
        gemControls.rotate();
        assertVerticalOrientation();
    }


    @Test
    public void cannotMoveLeftIfColumnToTheLeft(){
        int dropPosition = 5;
        gemGroup.setPosition(dropPosition);
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
        gemGroup.setPosition(dropPosition);
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


    private void dropGemGroupTo(int yPosition){
        while(gemGroup.getBottomPosition() > yPosition){
            gemGroup.drop();
        }
    }


    private void addVerticalGemsToGridAtPosition(int position){
        GemGroup gemGroupToAdd = Utils.createGemGroup(position, GemGroup.Orientation.VERTICAL, Gem.Color.RED, Gem.Color.BLUE, Gem.Color.GREEN);
        gemGrid.add(gemGroupToAdd);
    }


    private void moveRightTillAtPosition(int position){
        while(gemGroup.getPosition() < position){
            gemControls.moveRight();
        }
        assertPosition(position);
    }


    private void moveVerticalToRightEdge(){
        moveRightTillAtPosition(gemGrid.getNumberOfColumns()-1);
    }


    private void moveLeftTillAtPosition(int position){
        while(gemGroup.getPosition() > position){
            gemControls.moveLeft();
        }
        assertPosition(position);
    }


    private void assertPosition(int expected){
        assertEquals(expected, gemGroup.getPosition());
    }


    private void changeAndAssertOrientation(GemGroup.Orientation orientation){
        if(gemGroup.getOrientation() != orientation){
            gemGroup.rotate();
        }
        assertEquals(orientation, gemGroup.getOrientation());
    }


}
