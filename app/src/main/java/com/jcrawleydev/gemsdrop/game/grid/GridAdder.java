package com.jcrawleydev.gemsdrop.game.grid;

import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    Adds gems to the grid before a level starts
 */
public class GridAdder {

    private final Map<String, GemColor> gemColorMap = new HashMap<>();

    public GridAdder(){
        populateGemColorMap();
    }

    public void addTo(GemGrid gemGrid, List<String> gemRows){
        for(var row : gemRows){
            gemGrid.addRow(parseGemsFrom(row));

        }
    }


    private void populateGemColorMap(){
        Arrays.stream(GemColor.values()).forEach( gc -> gemColorMap.put(gc.toString(), gc));
    }


    public List<Gem> parseGemsFrom(String gemRowStr){
        var gemRow = new ArrayList<Gem>();
        var gemStrings = gemRowStr.split(" ");
        for(var str : gemStrings){
           var gemColor = gemColorMap.getOrDefault(str, GemColor.BLUE);
           gemRow.add(new Gem(gemColor));
        }
        return gemRow;
    }
}
