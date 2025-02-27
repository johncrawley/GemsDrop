package com.jcrawleydev.gemsdrop.view.fragments.game;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.game.gem.GemColor;

import java.util.HashMap;
import java.util.Map;

public class ImageMap {

    private final Map<Integer, Integer> map;

    public ImageMap(){
        map = new HashMap<>();
        for(var gemColor : GemColor.values()){
            put(gemColor, gemColor.resourceId);
        }
    }


    private void put(GemColor gemColor, int drawableId){
        map.put(gemColor.ordinal(), drawableId);
    }


    public int getDrawableIdFor(int gemColorId){
        Integer id = map.get(gemColorId);
        return id == null ? R.drawable.jewel_blue : id;
    }

}
