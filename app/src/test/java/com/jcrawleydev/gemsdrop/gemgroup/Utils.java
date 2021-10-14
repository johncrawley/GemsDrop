package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.Arrays;
import java.util.List;

public class Utils {


    public static GemGroup createGemGroup(int initialPosition, GemGroup.Orientation orientation, Gem.Color c1, Gem.Color c2, Gem.Color c3, int borderWidth){
        Gem gem1 = new Gem(c1);
        Gem gem2 = new Gem(c2);
        Gem gem3 = new Gem(c3);
        List<Gem> gems = Arrays.asList(gem1,gem2, gem3);
        int gemWidth = 150;
        return new GemGroup(initialPosition,0, orientation, gems, gemWidth,1000, borderWidth );

    }
}
