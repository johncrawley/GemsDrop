package com.jcrawleydev.gemsdrop.service;

import static org.junit.Assert.assertEquals;


import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.gem.GemColor;
import com.jcrawleydev.gemsdrop.service.game.gem.GemGroupPosition;
import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.gem.GemUtils;

import org.junit.Test;

public class GemUtilsTest {

    private GridProps gridProps = new GridProps(14, 7, 2);


    @Test
    public void canDetermineIfGemIsAdjacentToColumn(){
        assertGemColumnAdjacency(20, 1, 10, true);
        assertGemColumnAdjacency(19, 1, 10, true);
        assertGemColumnAdjacency(18, 1, 10, false);
        assertGemColumnAdjacency(18, 2, 10, true);
    }


    private void assertGemColumnAdjacency(int gemDepth, int columnHeight, int numberOfRows, boolean expectedResult){
        Gem gem = new Gem(GemColor.BLUE, GemGroupPosition.BOTTOM, gemDepth);
        assertEquals(expectedResult, GemUtils.isGemAdjacentToColumn(gem, columnHeight, gridProps));
    }

}
