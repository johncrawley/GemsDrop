package com.jcrawleydev.gemsdrop.game.gem;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.DARK_RED;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.DEEP_BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.RED;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.YELLOW;

import static org.junit.Assert.assertEquals;

import com.jcrawleydev.gemsdrop.game.level.GemOccurrence;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.List;

public class GemColorStoreTest {


    private GemColorStore gemColorStore;

    @Before
    public void init(){
        gemColorStore = new GemColorStore();
    }

    @Test
    public void canSaveNormalGemColors(){
        var gemOccurrences = List.of(new GemOccurrence(BLUE, 0),
                new GemOccurrence(RED, 0),
                new GemOccurrence(YELLOW, 0));

        assertNumberOfCurrentGemColors(0);
        gemColorStore.assign(gemOccurrences);
        assertNumberOfCurrentGemColors(3);
        gemColorStore.clear();
        assertNumberOfCurrentGemColors(0);
    }


    @Test
    public void deferredGemColorsGetAddedLater(){
        var gemOccurrences = List.of(new GemOccurrence(BLUE, 0),
                new GemOccurrence(DARK_RED, 3),
                new GemOccurrence(DEEP_BLUE, 4));

        gemColorStore.assign(gemOccurrences);

        assertNumberOfCurrentGemColors(1);
        gemColorStore.updateDropCount();
        gemColorStore.updateDropCount();
        gemColorStore.updateDropCount();

        assertNumberOfCurrentGemColors(2);
        gemColorStore.updateDropCount();
        assertNumberOfCurrentGemColors(3);
    }


    private void assertNumberOfCurrentGemColors(int expectedSize){
        assertListSize(gemColorStore.getCurrentGemColors(), expectedSize);
    }


    private void assertListSize(Collection<?> collection, int expectedSize){
        assertEquals(expectedSize, collection.size());
    }

}
