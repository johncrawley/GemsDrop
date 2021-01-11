package com.jcrawleydev.gemsdrop.gemgroup;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GemGroupFactoryTest {

    private GemGroupFactory gemGroupFactory;
    @Test
    public void correctNumberOfGemsAreCreated(){

        assertNumberOfGemsCreated(4);
        assertNumberOfGemsCreated(3);
    }

    @Test
    public void correctStartingCoordinatesAreGivenToGems(){
        int numberOfGems = 4;
        int initialX = 300;
        int initialY = -100;
        int gemWidth = 50;

        gemGroupFactory = new GemGroupFactory(numberOfGems, initialX, initialY, gemWidth);
        GemGroup gemGroup= gemGroupFactory.createGemGroup();
        assertEquals(initialX, gemGroup.getX() );
        assertEquals(initialY, gemGroup.getY() );

    }


    private void assertNumberOfGemsCreated(int numberOfGems){
        gemGroupFactory = new GemGroupFactory(numberOfGems, 0,0, 50);
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        assertEquals(numberOfGems, gemGroup.getGems().size());

    }

}
