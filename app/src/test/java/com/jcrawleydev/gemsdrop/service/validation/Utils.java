package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.game.gem.DroppingGems;

public class Utils {


    public static void dropToAboveGridRow(DroppingGems droppingGems, int targetGridRow){
        int targetHeight = targetGridRow * 2;
        while(droppingGems.getLowestGemPosition() > targetHeight){
            droppingGems.moveDown();
        }
    }

}
