package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gem.Gem;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jcrawleydev.gemsdrop.GemGroup.Orientation.HORIZONTAL;

import static com.jcrawleydev.gemsdrop.GemGroup.Orientation.VERTICAL;
import static com.jcrawleydev.gemsdrop.gem.Gem.Color.RED;
import static com.jcrawleydev.gemsdrop.gem.Gem.Color.BLUE;
import static com.jcrawleydev.gemsdrop.gem.Gem.Color.YELLOW;
import static com.jcrawleydev.gemsdrop.gem.Gem.Color.GREEN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GemGridTest {

    // attempts to place gems outside boundaries should throw exceptions
    // diagonal high-to-low test
    // large join
    // multiple joins in a single go
    // chain reactions

    private GemGrid gemGrid;
    private final int NUMBER_OF_COLUMNS = 10;
    private final int NUMBER_OF_ROWS = 8;
    private final int GEMS_PER_GROUP = 3;
    private final int INITIAL_POSITION = 5;


    @Before
    public void setup(){

        gemGrid = new GemGrid(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS, GEMS_PER_GROUP);
        assertTrue(gemGrid.isEmpty());
    }


    @Test
    public void containsAddedGems(){
        GemGroup gemGroup = createGemGroup(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        assertEquals(3, gemGrid.gemCount());
        assertFalse(gemGrid.isEmpty());
        gemGroup = createGemGroup(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.add(gemGroup);
        assertEquals(6, gemGrid.gemCount());
    }


    @Test
    public void gemsAreAddedToTheCorrectPosition(){
        addGems(INITIAL_POSITION, HORIZONTAL,  RED, BLUE, GREEN);
        String expectedRows = buildGridWith(1,"[ _ _ _ _ R B G _ _ _ ] ");
        assertEquals(expectedRows, gemGrid.toString());

        addGems(1, HORIZONTAL, GREEN, RED, GREEN);
        expectedRows = buildGridWith( 1, "[ G R G _ R B G _ _ _ ] ");
        assertEquals(expectedRows, gemGrid.toString());

        addGems(2, HORIZONTAL, BLUE, BLUE, GREEN);
        expectedRows = buildGridWith(2,
                "[ _ B B _ _ _ _ _ _ _ ] " +
                        "[ G R G G R B G _ _ _ ] ");

        assertEquals(expectedRows, gemGrid.toString());
    }


    @Test
    public void canEvaluateGemRows(){
        addGems(INITIAL_POSITION, HORIZONTAL, BLUE, BLUE, BLUE);
        assertEquals(3, gemGrid.gemCount());
        assertFalse(gemGrid.isEmpty());

        gemGrid.evaluate();
        assertEquals(0, gemGrid.gemCount());
        assertTrue(gemGrid.isEmpty());

        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        gemGrid.evaluate();
        assertEquals(6, gemGrid.gemCount());
    }


    @Test
    public void canEvaluateGemColumns(){
        //adding a single vertical column with 3 gems of different colours should cause no elimination
        addGems(INITIAL_POSITION, VERTICAL, BLUE, GREEN, BLUE);
        assertGemsBeforeAndAfterEval(3,3);
        gemGrid.clear();

        // adding a single vertical column with 3 gems of the same colour should eliminate them
        addGems(INITIAL_POSITION, VERTICAL, BLUE, BLUE, BLUE);
        assertGemsBeforeAndAfterEval(3,0);

        //adding 3 stacked rows, so that the first 2 columns should be removed
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE,YELLOW);
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, GREEN);
        gemGrid.evaluate();
        assertEquals(3, gemGrid.gemCount());
    }


    @Test
    public void canEvaluateGemHorizontal(){
        //adding a single vertical column with 3 gems of different colours should cause no elimination
        addGems(INITIAL_POSITION, VERTICAL, BLUE, GREEN, BLUE);
        assertGemsBeforeAndAfterEval(3,3);
        gemGrid.clear();

        // adding a single vertical column with 3 gems of the same colour should eliminate them
        addGems(INITIAL_POSITION, VERTICAL, BLUE, BLUE, BLUE);
        assertGemsBeforeAndAfterEval(3,0);

        //adding 3 stacked rows, so that the first 2 columns should be removed
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE,YELLOW);
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, GREEN);
        gemGrid.evaluate();
        assertEquals(3, gemGrid.gemCount());
    }


    @Test
    public void canEvaluateDiagonals(){
        addGems(1, HORIZONTAL, BLUE, GREEN, RED);
        addGems(2, HORIZONTAL, BLUE, GREEN, RED);
        addGems(3, HORIZONTAL, BLUE, YELLOW, YELLOW);

        assertGridBeforeAndAfter(3,
                "[ _ _ B _ _ _ _ _ _ _ ] " +
                          "[ _ B G Y _ _ _ _ _ _ ] "+
                          "[ B G R R Y _ _ _ _ _ ] ",

                "[ _ _ _ _ _ _ _ _ _ _ ] " +
                         "[ _ _ G Y _ _ _ _ _ _ ] "+
                         "[ _ G R R Y _ _ _ _ _ ] "
        );

        gemGrid.clear();

        addGems(1, HORIZONTAL, BLUE, GREEN, RED);
        addGems(1, HORIZONTAL, RED, YELLOW, GREEN);
        addGems(2, HORIZONTAL, RED, BLUE, RED);
        addGems(3, HORIZONTAL, RED, GREEN, GREEN);

        assertGridBeforeAndAfter(4,
                "[ _ _ R _ _ _ _ _ _ _ ] " +
                          "[ _ R B _ _ _ _ _ _ _ ] " +
                          "[ R Y G G _ _ _ _ _ _ ] "+
                          "[ B G R R G _ _ _ _ _ ] ",

                "[ _ _ _ _ _ _ _ _ _ _ ] " +
                         "[ _ _ B _ _ _ _ _ _ _ ] " +
                         "[ _ Y G G _ _ _ _ _ _ ] "+
                         "[ B G R R G _ _ _ _ _ ] "
        );
    }


    private String buildGridWith(int modifiedRows, String rowsStr){
        int emptyRows = NUMBER_OF_ROWS - modifiedRows;
        StringBuilder str = new StringBuilder();
        for(int i=0; i< emptyRows; i++){
            str.append("[ _ _ _ _ _ _ _ _ _ _ ] ");
        }
        str.append(rowsStr);
        return str.toString();
    }


    private void addGems(int position, GemGroup.Orientation orientation, Gem.Color color1, Gem.Color color2, Gem.Color color3){
        GemGroup gemGroup = createGemGroup(position, orientation, color1, color2, color3);
        gemGrid.add(gemGroup);
    }


    private void assertGridBeforeAndAfter(int modifiedRows, String beforeGrid, String afterGrid){
        String expectedGrid = buildGridWith(modifiedRows,beforeGrid);
        assertEquals(expectedGrid, gemGrid.toString());
        gemGrid.evaluate();
        expectedGrid = buildGridWith(modifiedRows, afterGrid);
        assertEquals(expectedGrid, gemGrid.toString());

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
        return new GemGroup(initialPosition, orientation, gems);

    }

}
