package com.jcrawleydev.gemsdrop.gemgroup;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GemGroupTest {

    GemGroup gemGroup;

    @Before
    public void init(){
        GemGroupFactory gemGroupFactory = new GemGroupFactory(3, 50,50, 150);
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
}
