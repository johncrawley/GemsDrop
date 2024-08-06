package com.jcrawleydev.gemsdrop.view.fragments.game;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.gem.GemColor;

import java.util.HashMap;
import java.util.Map;

public class ImageMap {

    private Map<Integer, Integer> map;

    public ImageMap(){
        map = new HashMap<>();
        put(GemColor.GREEN, R.drawable.jewel_green);
        put(GemColor.BLUE, R.drawable.jewel_blue);
        put(GemColor.YELLOW, R.drawable.jewel_yellow);
        put(GemColor.RED, R.drawable.jewel_red);
        put(GemColor.GREY, R.drawable.jewel_grey);
    }


    private void put(GemColor gemColor, int drawableId){
        map.put(gemColor.ordinal(), drawableId);
    }


    public int getDrawableIdFor(int gemColorId){
        Integer id = map.get(gemColorId);
        return id == null ? R.drawable.jewel_blue : id;
    }




}
