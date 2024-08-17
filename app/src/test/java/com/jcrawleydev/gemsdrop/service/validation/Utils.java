package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.service.DroppingGems;

public class Utils {


    public static void dropToAboveGridRow(DroppingGems droppingGems, int targetGridRow){
        int targetHeight = targetGridRow * 2;
        while(droppingGems.getBottomHeight() > targetHeight){
            droppingGems.drop();
        }
    }

}
