package com.jcrawleydev.gemsdrop.score;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GemCountTrackerTest {

    private GemCountTracker gemCountTracker;
    private GemGrid gemGrid;
    private GemGroupFactory gemGroupFactory;
    private int numberOfGems = 3;

    @Before
    public void init(){
        gemGrid = new GemGrid(10,10);
        gemCountTracker = new GemCountTracker(gemGrid);
        gemGroupFactory = new GemGroupFactory.Builder()
                .withFloorAt(1000)
                .withInitialPosition(5)
                .withNumerOfGems( numberOfGems)
                .withGemWidth(150)
                .withInitialY(100)
                .build();
    }


    @Test
    public void canKeepTrackOfMultipleChanges(){
        assertTrackerHas(0);
        addSomeGemsToGrid();


    }


    private void assertTrackerHas(int difference){
        assertEquals(difference, gemCountTracker.getDifference());
    }

    private void addSomeGemsToGrid(){
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        gemGrid.add(gemGroup);
    }
}
