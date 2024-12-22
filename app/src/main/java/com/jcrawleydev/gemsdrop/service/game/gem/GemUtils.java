package com.jcrawleydev.gemsdrop.service.game.gem;
import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.GridProps;

public class GemUtils {

    public static int convertContainerPositionToGridHeight(int depth, int numberOfRows){
        return numberOfRows - (depth / 2);
    }


    public static int getBottomHeightOf(Gem gem, GridProps gridProps){
        return gridProps.numberOfPositions() - gem.getBottomDepth();
    }


    public static boolean isGemAdjacentToColumn(Gem gem, int columnHeight, GridProps gridProps){
        int gemBottomHeight =  getBottomHeightOf(gem, gridProps);
        return gemBottomHeight < columnHeight;
    }


}
