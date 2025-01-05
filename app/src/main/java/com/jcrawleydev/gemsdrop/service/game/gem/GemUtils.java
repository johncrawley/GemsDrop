package com.jcrawleydev.gemsdrop.service.game.gem;
import com.jcrawleydev.gemsdrop.service.game.GridProps;

public class GemUtils {


    public static boolean isGemAdjacentToColumn(Gem gem, int columnHeight, GridProps gridProps){
        return false;
    }


    public static int getBottomHeightOf(Gem gem, GridProps gridProps){
        return gem.getContainerPosition();
    }
}
