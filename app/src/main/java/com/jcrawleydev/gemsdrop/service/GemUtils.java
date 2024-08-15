package com.jcrawleydev.gemsdrop.service;
import com.jcrawleydev.gemsdrop.gem.Gem;

public class GemUtils {

    //Gem Depth is from the top, height it in the gem grid, and is from the bottom
    // i.e. at height 0, the gem depth should be 26, if there are 14 rows
    public static int convertDepthToHeight(int depth, int numberOfRows){
        return numberOfRows - (depth / 2);
    }


    public static int getBottomHeightOf(Gem gem, GridProps gridProps){
        return gridProps.numberOfDepths() - gem.getBottomDepth();
    }


    public static boolean isGemAdjacentToColumn(Gem gem, int columnHeight, GridProps gridProps){
        int gemBottomHeight =  getBottomHeightOf(gem, gridProps);
        return gemBottomHeight < columnHeight;
    }


}
