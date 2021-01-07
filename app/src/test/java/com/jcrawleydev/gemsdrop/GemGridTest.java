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
    private final int NUMBER_OF_ROWS = 8;

    private final int GEMS_PER_GROUP = 3;
    private final Gem.Color BLUE = Gem.Color.BLUE;
    private final Gem.Color RED = Gem.Color.RED;
    private final Gem.Color YELLOW = Gem.Color.YELLOW;
    private final Gem.Color GREEN = Gem.Color.GREEN;

    private final int INITIAL_POSITION = 5;

    @Before
    public void setup(){

        gemGrid = new GemGrid(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS, GEMS_PER_GROUP);
        assertTrue(gemGrid.isEmpty());
    }

    @Test
    public void containsAddedGems(){
        GemGroup gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        assertEquals(3, gemGrid.gemCount());
        assertFalse(gemGrid.isEmpty());
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        assertEquals(6, gemGrid.gemCount());

    }

    @Test
    public void gemsAreAddedToTheCorrectPosition(){
        GemGroup gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, GREEN);
        gemGrid.add(gemGroup);
        String expectedColors = withEmptyRows(NUMBER_OF_ROWS - 1) +
                                "[ 0 0 0 0 R B G 0 0 0 ] ";

        assertEquals(expectedColors, gemGrid.toString());

    }

    private String withEmptyRows(int number){
        StringBuilder str = new StringBuilder();
        for(int i=0; i< number; i++){
            str.append("[ 0 0 0 0 0 0 0 0 0 0 ] ");
        }
        return str.toString();
    }


    @Test
    public void canEvaluateGemRows(){
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


    @Test
    public void canEvaluateGemColumns(){
        //adding a single vertical column with 3 gems of different colours should cause no elimination
        GemGroup gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.VERTICAL, BLUE, GREEN, BLUE);
        gemGrid.add(gemGroup);
        assertGemsBeforeAndAfterEval(3,3);
        gemGrid.clear();

        // adding a single vertical column with 3 gems of the same colour should eliminate them
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.VERTICAL, BLUE, BLUE, BLUE);
        gemGrid.add(gemGroup);
        assertGemsBeforeAndAfterEval(3,0);


        //adding 3 stacked rows, so that the first 2 columns should be removed
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        gemGroup = createGemGroup(INITIAL_POSITION, GemGroup.Orientation.HORIZONTAL, RED, BLUE, GREEN);
        gemGrid.add(gemGroup);

        gemGrid.evaluate();
        assertEquals(3, gemGrid.gemCount());
    }


    private void assertGemsBeforeAndAfterEval(int expectedAmountBefore, int expectedAmountAfter){
        assertEquals(expectedAmountBefore, gemGrid.gemCount());
        gemGrid.evaluate();
        assertEquals(expectedAmountAfter, gemGrid.gemCount());

    }


    private GemGroup createGemGroup(int initialPosition, GemGroup.Orientation orientation, Gem.Color c1, Gem.Color c2, Gem.Color c3){
        Gem gem1 = new Gem(c1);
        Gem gem2 = new Gem(c2);
        Gem gem3 = new Gem(c3);
        List<Gem> gems = Arrays.asList(gem1,gem2, gem3);
        return new GemGroup(INITIAL_POSITION, orientation, gems);

    }

}
