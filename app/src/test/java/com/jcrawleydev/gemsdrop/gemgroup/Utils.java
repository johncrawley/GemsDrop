package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemColor;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

import java.util.Arrays;
import java.util.List;

public class Utils {


    public static GemGroup createGemGroup(GemGrid gemGrid, int initialPosition, GemGroup.Orientation orientation, GemColor c1, GemColor c2, GemColor c3, int borderWidth){
        Gem gem1 = new Gem(c1);
        Gem gem2 = new Gem(c2);
        Gem gem3 = new Gem(c3);
        List<Gem> gems = Arrays.asList(gem1,gem2, gem3);
        int gemWidth = 150;
        return GemGroup.Builder.newInstance()
                .gems(gems)
                .initialPosition(initialPosition)
                .initialY(5)
                .orientation(GemGroup.Orientation.VERTICAL)
                .gemWidth(gemWidth)
                .floorY(2000)
                .borderWidth(borderWidth)
                .build();
    }
}
