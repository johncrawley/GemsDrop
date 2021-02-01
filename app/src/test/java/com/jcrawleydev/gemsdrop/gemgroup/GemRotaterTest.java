package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class GemRotaterTest {


    private final int GEM_WIDTH = 150;
    private final int NUMBER_OF_GEMS = 3;
    private final GemGroup.Orientation INITIAL_ORIENTATION = GemGroup.Orientation.VERTICAL;
    private GemGroup gemGroup;
    private GemRotater gemRotater;
    private List<Gem> gems;

    @Before
    public void init(){
        gemGroup = new GemGroupFactory.Builder()
                .withNumerOfGems(NUMBER_OF_GEMS)
                .withInitialPosition(8)
                .withInitialY(50)
                .withFloorAt(1000)
                .withGemWidth(GEM_WIDTH)
                .build().createGemGroup();

        gems = gemGroup.getGems();
        gemRotater = new GemRotater(gemGroup, GEM_WIDTH);
    }

    @Test
    public void canAssignInitialCoordinates(){

        gemRotater.setGemCoordinates(gemGroup);
        int initialExpectedY =  - (GEM_WIDTH /2) - ( NUMBER_OF_GEMS /2 * GEM_WIDTH );
        for(int i=0; i < NUMBER_OF_GEMS; i++){
            Gem gem = gems.get(i);
            assertEquals("Gem index : " + i + " has an unexpected x coordinate..." , -GEM_WIDTH /2, gem.getX());
            assertEquals("Gem index : " + i + " has an unexpected y coordinate..." ,  initialExpectedY +  i * (GEM_WIDTH), gem.getY());
        }

    }


    @Test
    public void coordinatesAreAdjustedDuringRotation(){

        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());
        gemRotater.rotate();
        assertEquals(GemGroup.Orientation.HORIZONTAL, gemGroup.getOrientation());


        int baseline =  (GEM_WIDTH /2);// + ( NUMBER_OF_GEMS /2 * GEM_WIDTH );

        for(int i=0; i < NUMBER_OF_GEMS; i++){
            Gem gem = gems.get(i);
            assertEquals("Gem index : " + i + " has an unexpected x coordinate..." ,  baseline -  i * (GEM_WIDTH), gem.getX());
            assertEquals("Gem index : " + i + " has an unexpected y coordinate..." , -GEM_WIDTH /2, gem.getY());
        }

        gemRotater.rotate();
        assertEquals(GemGroup.Orientation.VERTICAL, gemGroup.getOrientation());

        for(int i=0; i < NUMBER_OF_GEMS; i++){
            assertEquals("Gem index : " + i + " has an unexpected x coordinate for vertical orientation.." ,  -GEM_WIDTH /2, gems.get(i).getX());
            assertEquals("Gem index : " + i + " has an unexpected y coordinate for vertical orientation.." , baseline -  i * (GEM_WIDTH), gems.get(i).getY());
        }

    }



}
