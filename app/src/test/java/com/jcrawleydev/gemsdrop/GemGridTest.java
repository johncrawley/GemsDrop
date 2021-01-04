package com.jcrawleydev.gemsdrop;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GemGridTest {


    private GemGrid gemGrid;
    private final int NUMBER_OF_COLUMNS = 10;

    private final int GEMS_PER_GROUP = 3;

    @Before
    public void setup(){
        gemGrid = new GemGrid(NUMBER_OF_COLUMNS, GEMS_PER_GROUP);
    }

    @Test
    public void containsAddedGems(){
        assertTrue(gemGrid.isEmpty());
        Gem gem1 = new Gem(Gem.Color.BLUE);
        Gem gem2 = new Gem(Gem.Color.GREEN);
        Gem gem3 = new Gem(Gem.Color.YELLOW);
        List<Gem> gems = Arrays.asList(gem1, gem2, gem3);
        final int INITIAL_POSITION = 5;
        GemGroup gemGroup = new GemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, gems);
        assertTrue(gemGrid.isEmpty());
        gemGrid.add(gemGroup);
        assertEquals(gems.size(), gemGrid.gemCount());


    }


}
