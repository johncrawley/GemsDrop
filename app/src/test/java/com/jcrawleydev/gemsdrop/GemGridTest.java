package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gem.Gem;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GemGridTest {


    private GemGrid gemGrid;
    private final int NUMBER_OF_COLUMNS = 10;

    private final int GEMS_PER_GROUP = 3;
    private final Gem.Color BLUE = Gem.Color.BLUE;
    private final Gem.Color RED = Gem.Color.RED;
    private final Gem.Color YELLOW = Gem.Color.YELLOW;

    private final int INITIAL_POSITION = 5;

    @Before
    public void setup(){
        gemGrid = new GemGrid(NUMBER_OF_COLUMNS, GEMS_PER_GROUP);
    }

    @Test
    public void containsAddedGems(){
        assertTrue(gemGrid.isEmpty());
        GemGroup gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        assertTrue(gemGrid.isEmpty());
        gemGrid.add(gemGroup);
        assertEquals(3, gemGrid.gemCount());
        assertFalse(gemGrid.isEmpty());
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        assertEquals(6, gemGrid.gemCount());

    }


    @Test
    public void canEvaluateGemRows(){
        assertTrue(gemGrid.isEmpty());
        GemGroup gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, BLUE, BLUE, BLUE);
        gemGrid.add(gemGroup);
        assertEquals(3, gemGrid.gemCount());
        assertFalse(gemGrid.isEmpty());

        gemGrid.evaluate();
        assertEquals(0, gemGrid.gemCount());
        assertTrue(gemGrid.isEmpty());


        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        gemGrid.evaluate();
        assertEquals(6, gemGrid.gemCount());


    }


    private GemGroup createGemGroup(int initialPosition, GemGroup.Orientation orientation, Gem.Color c1, Gem.Color c2, Gem.Color c3){
        Gem gem1 = new Gem(c1);
        Gem gem2 = new Gem(c2);
        Gem gem3 = new Gem(c3);
        List<Gem> gems = Arrays.asList(gem1,gem2, gem3);
        return new GemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, gems);

    }

}
