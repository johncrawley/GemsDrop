package com.jcrawleydev.gemsdrop.game.gem;

import com.jcrawleydev.gemsdrop.game.level.GemOccurrence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemColorStore {

    private final List<GemColor> gemColors = new ArrayList<>();
    private final Map<Integer, GemColor> laterGemColors = new HashMap<>();
    private int dropCount = 0;


    public void assign(Collection<GemOccurrence> gemOccurrences){
        for(var gemOccurrence : gemOccurrences){
            assignGemColorFrom(gemOccurrence);
        }
    }


    private void assignGemColorFrom(GemOccurrence gemOccurrence){
        if(gemOccurrence.gemsDropped() == 0){
            gemColors.add(gemOccurrence.gemColor());
        }
        else{
            laterGemColors.put(gemOccurrence.gemsDropped(), gemOccurrence.gemColor());
        }
    }


    public void updateDropCount(){
        if(laterGemColors.containsKey(++dropCount)){
            gemColors.add(laterGemColors.get(dropCount));
        }
    }


    public List<GemColor> getCurrentGemColors(){
        return new ArrayList<>(gemColors);
    }


    public void clear(){
        gemColors.clear();
        dropCount = 0;
    }

}
