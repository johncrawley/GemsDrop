package com.jcrawleydev.gemsdrop.score;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroupFactory;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GemCountTrackerTest {

    private GemCountTracker gemCountTracker;
    private GemGrid gemGrid;
    private GemGroupFactory gemGroupFactory;
    private Evaluator evaluator;
    private int numberOfGems = 3;

    @Before
    public void init(){
        final int MATCH_NUMBER = 3;
        gemGrid = new GemGrid(10,10);
        gemCountTracker = new GemCountTracker(gemGrid);
        evaluator = new Evaluator(gemGrid, MATCH_NUMBER);
    }


    @Test
    public void canKeepTrackOfMultipleChanges(){
        assertTrackerHas(0);
        gemCountTracker.startTracking();
        addSingleGem();
        assertTrackerHas(0);
        addSingleGem();
        assertTrackerHas(0);
        addSingleGem();
        assertTrackerHas(0);
        gemCountTracker.startTracking();
        assertTrackerHas(0);
        evaluator.evaluate();
        evaluator.deleteMarkedGemsOLD();
        assertTrackerHas(3);

    }


    private void assertTrackerHas(int difference){
        assertEquals(difference, gemCountTracker.getDifference());
    }

    private void addSingleGem(){
        gemGrid.add(new Gem(GemColor.GREEN), 5);
    }
}
