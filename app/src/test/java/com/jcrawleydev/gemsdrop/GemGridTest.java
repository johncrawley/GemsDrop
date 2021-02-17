package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgrid.Evaluator;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;
import com.jcrawleydev.gemsdrop.gemgroup.Utils;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.jcrawleydev.gemsdrop.gemgroup.GemGroup.Orientation.HORIZONTAL;

import static com.jcrawleydev.gemsdrop.gemgroup.GemGroup.Orientation.VERTICAL;
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
    private final int INITIAL_POSITION = 5;
    private Evaluator evaluator;


    @Before
    public void setup(){

        gemGrid = new GemGrid(NUMBER_OF_COLUMNS, NUMBER_OF_ROWS);
        assertTrue(gemGrid.isEmpty());
        evaluator = new Evaluator(gemGrid, 3);
    }


    @Test
    public void containsAddedGems(){
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        assertEquals(3, gemGrid.gemCount());
        assertFalse(gemGrid.isEmpty());
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        assertEquals(6, gemGrid.gemCount());
    }


    @Test
    public void canReportCorrectColumnHeights(){
        assertEquals(NUMBER_OF_COLUMNS, gemGrid.getColumnHeights().size());

        assertColumnHeights(0,0,0,0,0,0,0,0,0,0);

        addHorizontalGems(2, BLUE, GREEN, RED);
        assertColumnHeights(0,1,1,1,0,0,0,0,0,0);

        addHorizontalGems(3, BLUE, GREEN, RED);
        assertColumnHeights(0,1,2,2,1,0,0,0,0,0);

        addGems(0, VERTICAL, YELLOW, GREEN, RED);
        assertColumnHeights(3,1,2,2,1,0,0,0,0,0);

    }

    private void assertColumnHeights(Integer ... heights){

        List<Integer> expectedHeights = Arrays.asList(heights);
        List<Integer> columnHeights = gemGrid.getColumnHeights();
        assertList(expectedHeights, columnHeights);
    }

    private void assertList(List<?> expected, List<?> provided){
        assertEquals(expected.size(), provided.size());
        for(int i = 0; i< provided.size(); i++){
            assertEquals(" item at index: " + i + " doesn't match up", expected.get(i), provided.get(i));
        }
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

        evaluateAndDeleteMarkedGems();
        assertEquals(0, gemGrid.gemCount());
        assertTrue(gemGrid.isEmpty());

        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        addGems(INITIAL_POSITION, HORIZONTAL, RED, BLUE, YELLOW);
        evaluateAndDeleteMarkedGems();
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
        evaluateAndDeleteMarkedGems();
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
        evaluateAndDeleteMarkedGems();
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


    @Test
    public void canEvaluateReverseDiagonals(){
        int maxPosition = this.NUMBER_OF_COLUMNS - 2;
        int minPosition = 2;

        addHorizontalGems(maxPosition, BLUE, GREEN, YELLOW);
        addHorizontalGems(maxPosition, GREEN, YELLOW, RED);
        addHorizontalGems(maxPosition, YELLOW, BLUE, BLUE);

        assertGridBeforeAndAfter(3,
                 "[ _ _ _ _ _ _ _ Y B B ] " +
                            "[ _ _ _ _ _ _ _ G Y R ] " +
                            "[ _ _ _ _ _ _ _ B G Y ] ",

                "[ _ _ _ _ _ _ _ _ _ _ ] " +
                         "[ _ _ _ _ _ _ _ G B B ] " +
                         "[ _ _ _ _ _ _ _ B G R ] "
        );

        gemGrid.clear();

        addHorizontalGems(minPosition, BLUE, GREEN, YELLOW);
        addHorizontalGems(minPosition, GREEN, YELLOW, RED);
        addHorizontalGems(minPosition, YELLOW, BLUE, BLUE);

        assertGridBeforeAndAfter(3,
                "[ _ Y B B _ _ _ _ _ _ ] " +
                        "[ _ G Y R _ _ _ _ _ _ ] " +
                        "[ _ B G Y _ _ _ _ _ _ ] ",

                "[ _ _ _ _ _ _ _ _ _ _ ] " +
                        "[ _ G B B _ _ _ _ _ _ ] " +
                        "[ _ B G R _ _ _ _ _ _ ] "
        );

        gemGrid.clear();
        minPosition = 1;
        addHorizontalGems(minPosition, BLUE, GREEN, YELLOW);
        addHorizontalGems(minPosition, GREEN, YELLOW, RED);
        addHorizontalGems(minPosition, YELLOW, BLUE, BLUE);

        assertGridBeforeAndAfter(3,
                 "[ Y B B _ _ _ _ _ _ _ ] " +
                            "[ G Y R _ _ _ _ _ _ _ ] " +
                            "[ B G Y _ _ _ _ _ _ _ ] ",

                "[ _ _ _ _ _ _ _ _ _ _ ] " +
                        "[ G B B _ _ _ _ _ _ _ ] " +
                        "[ B G R _ _ _ _ _ _ _ ] "
        );


    }

    @Test
    public void canEvaluateTopHalfReverseDiagonals(){
        int maxPosition = this.NUMBER_OF_COLUMNS - 2;
        int minPosition = 2;
        addHorizontalGems(maxPosition, RED, GREEN, BLUE);
        addHorizontalGems(maxPosition, YELLOW, BLUE, YELLOW);
        addHorizontalGems(maxPosition, GREEN, YELLOW, GREEN);
        addHorizontalGems(maxPosition, YELLOW, RED, GREEN);

        assertGridBeforeAndAfter(4,

                "[ _ _ _ _ _ _ _ Y R G ] " +
                        "[ _ _ _ _ _ _ _ G Y G ] " +
                        "[ _ _ _ _ _ _ _ Y B Y ] " +
                        "[ _ _ _ _ _ _ _ R G B ] ",

                "[ _ _ _ _ _ _ _ _ _ _ ] " +
                        "[ _ _ _ _ _ _ _ G R G ] " +
                        "[ _ _ _ _ _ _ _ Y B G ] " +
                        "[ _ _ _ _ _ _ _ R G B ] "
        );

    }


    @Test
    public void canAddSingleGem(){
        Gem gem = new Gem(BLUE);
        int position = 3;
        gemGrid.add(gem, position);
        assertEquals(1, gemGrid.gemCount());
        assertEquals(1,gemGrid.getColumnHeight(position));

    }

    @Test
    public void canGetAllGems(){

        assertEquals(0, gemGrid.getAllGems().size());
        addRandomGems();
        assertEquals(3, gemGrid.getAllGems().size());

        gemGrid.clear();

        addGems(1, VERTICAL, RED, BLUE, GREEN);
        addGems(2, VERTICAL, RED, YELLOW, GREEN);
        addGems(3, VERTICAL, RED, GREEN, BLUE);
        evaluateAndDeleteMarkedGems();
        assertEquals(6, gemGrid.gemCount());
        //assertEquals(6, gemGrid.getAllGems().size());

    }

    private void addRandomGems(){
        addGems(getRandomPosition(), getRandomOrientation(), getRandomColor(), getRandomColor(), getRandomColor());
    }

    private int getRandomPosition(){
        return ThreadLocalRandom.current().nextInt(NUMBER_OF_COLUMNS);
    }

    private GemGroup.Orientation getRandomOrientation(){
        if(ThreadLocalRandom.current().nextBoolean()){
            return HORIZONTAL;
        }
        return VERTICAL;
    }

    private Gem.Color getRandomColor(){
        List<Gem.Color> colors = Arrays.asList(RED,BLUE,GREEN,YELLOW);
        return colors.get(ThreadLocalRandom.current().nextInt(colors.size()));
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

    private void addHorizontalGems(int position, Gem.Color c1, Gem.Color c2, Gem.Color c3){
        addGems(position, HORIZONTAL, c1, c2, c3);
    }


    private void addGems(int position, GemGroup.Orientation orientation, Gem.Color color1, Gem.Color color2, Gem.Color color3){
        GemGroup gemGroup = Utils.createGemGroup(position, orientation, color1, color2, color3);
        gemGrid.add(gemGroup);
    }



    private void assertGridBeforeAndAfter(int modifiedRows, String beforeGrid, String afterGrid){
        String expectedGrid = buildGridWith(modifiedRows,beforeGrid);
        assertEquals(expectedGrid, gemGrid.toString());
        evaluateAndDeleteMarkedGems();
        expectedGrid = buildGridWith(modifiedRows, afterGrid);
        assertEquals(expectedGrid, gemGrid.toString());

    }

    private void assertGemsBeforeAndAfterEval(int expectedAmountBefore, int expectedAmountAfter){
        assertEquals(expectedAmountBefore, gemGrid.gemCount());
        evaluateAndDeleteMarkedGems();
        assertEquals(expectedAmountAfter, gemGrid.gemCount());

    }




    private void evaluateAndDeleteMarkedGems(){
        evaluator.evaluate();
        evaluator.deleteMarkedGems();
    }


}
