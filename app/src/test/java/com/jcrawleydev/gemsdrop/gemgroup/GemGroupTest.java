package com.jcrawleydev.gemsdrop.gemgroup;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GemGroupTest {

    GemGroup gemGroup;

    @Before
    public void init(){
        GemGroupFactory gemGroupFactory = new GemGroupFactory.Builder()
                .withNumberOfGems(3)
                .withInitialPosition(5)
                .withInitialY(150)
                .withFloorAt(1000)
                .withGemWidth(150)
                .build();

        gemGroup = gemGroupFactory.createGemGroup();
    }

    @Test
    public void canRotate(){
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
        gemGroup.rotate();
        assertEquals(GemGroup.Orientation.HORIZONTAL, gemGroup.getOrientation());
        gemGroup.rotate();
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
    }


    @Test
    public void canSetPosition(){
        int adjustedPosition = 3;
        gemGroup.setXPosition(adjustedPosition);
        assertEquals(adjustedPosition, gemGroup.getXPosition());

    }

}
