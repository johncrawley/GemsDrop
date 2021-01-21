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

    @BeforeClass
    public static void initialSetup(){
        int numberOfGemsPerGroup = 3;
        gemGrid = new GemGrid(8, 12, numberOfGemsPerGroup);
        gemGroupFactory = new GemGroupFactory(numberOfGemsPerGroup,0,0,150);

    }

    @Before
    public void setup(){
        gemGroup = gemGroupFactory.createGemGroup();
        gemControls = new GemControls(gemGroup, gemGrid);
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
    public void cantRotateIfVerticalAndAtFirstPosition(){

        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            gemGroup.rotate();
        }
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
        while(gemGroup.getPosition() > 0){
            gemControls.moveLeft();
        }
        assertEquals(0, gemGroup.getPosition());
        gemControls.moveLeft();
        assertEquals(0,gemGroup.getPosition());

    }



}
