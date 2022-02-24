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
        int initialY = -100;
        int gemWidth = 50;

        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(initialY)
                .withGemWidth(gemWidth)
                .withNumberOfGems(numberOfGems)
                .withInitialPosition(0)
                .withFloorAt(300)
                .build();
        GemGroup gemGroup= gemGroupFactory.createGemGroup();
        assertEquals(initialY, gemGroup.getY() );

    }


    private void assertNumberOfGemsCreated(int numberOfGems){

        gemGroupFactory = new GemGroupFactory.Builder()
                .withInitialY(50)
                .withGemWidth(150)
                .withNumberOfGems(numberOfGems)
                .withInitialPosition(0)
                .withFloorAt(300)
                .build();
        GemGroup gemGroup = gemGroupFactory.createGemGroup();
        assertEquals(numberOfGems, gemGroup.getGems().size());
    }

}
