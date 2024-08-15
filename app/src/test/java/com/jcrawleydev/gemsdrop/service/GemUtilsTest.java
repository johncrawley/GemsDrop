package com.jcrawleydev.gemsdrop.service;

import static org.junit.Assert.assertEquals;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.gem.GemPosition;

import org.junit.Test;

public class GemUtilsTest {

    private GridProps gridProps = new GridProps(14, 7, 2);

    @Test
    public void canCovertDepthToHeight(){
        assertDepthConversion(10, 0, 10);
        assertDepthConversion( 10, 1, 10);
        assertDepthConversion(10, 5, 8);
        assertDepthConversion(10, 19, 1);

    }


    private void assertDepthConversion(int numberOfRows, int depth, int expectedHeight){
        int result = GemUtils.convertDepthToHeight(depth, numberOfRows);
        assertEquals( expectedHeight, result);
    }


    @Test
    public void canDetermineIfGemIsAdjacentToColumn(){
        assertGemColumnAdjacency(20, 1, 10, true);
        assertGemColumnAdjacency(19, 1, 10, true);
        assertGemColumnAdjacency(18, 1, 10, false);
        assertGemColumnAdjacency(18, 2, 10, true);
    }


    private void assertGemColumnAdjacency(int gemDepth, int columnHeight, int numberOfRows, boolean expectedResult){
        Gem gem = new Gem(GemColor.BLUE, GemPosition.BOTTOM,gemDepth);
        assertEquals(expectedResult, GemUtils.isGemAdjacentToColumn(gem, columnHeight, gridProps));
    }

}
