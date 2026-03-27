package com.jcrawleydev.gemsdrop.game.grid;

import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
    Adds gems to the grid before a level starts
 */
public class GridAdder {

    private final Map<String, GemColor> gemColorMap = new HashMap<>();

    public GridAdder(){
        populateGemColorMap();
    }


    public void addTo(GemGrid gemGrid, List<List<GemColor>> grid){
        for(var rowOfColors : grid){
            var gemRow = rowOfColors.stream()
                    .map(Gem::new)
                    .collect(Collectors.toList()); //ignore warning, requires min API34
            gemGrid.addRow(gemRow);
        }
        gemGrid.printColumnHeights();
    }


    private void populateGemColorMap(){
        Arrays.stream(GemColor.values())
                .forEach( gc -> gemColorMap.put(gc.toString(), gc));
    }

}
