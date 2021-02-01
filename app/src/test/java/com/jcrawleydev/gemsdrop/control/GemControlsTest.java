package com.jcrawleydev.gemsdrop.control;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

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
    private static int gemWidth = 150;

    int rightmostIndexForHorizontal = 0;

    @BeforeClass
    public static void initialSetup(){
        gemGrid = new GemGrid(8, 12);
        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(150)
                .withGemWidth(gemWidth)
                .withNumerOfGems(numberOfGemsPerGroup)
                .withInitialPosition(0)
                .withFloorAt(300)
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
