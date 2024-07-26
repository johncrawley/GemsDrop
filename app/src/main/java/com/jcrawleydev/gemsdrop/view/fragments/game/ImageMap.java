package com.jcrawleydev.gemsdrop.view.fragments.game;

import com.jcrawleydev.gemsdrop.R;

import java.util.HashMap;
import java.util.Map;

public class ImageMap {

    private Map<ItemType, Integer> map;

    public ImageMap(){
        map = new HashMap<>();
        map.put(ItemType.GREEN_GEM, R.drawable.jewel_green);
        map.put(ItemType.BLUE_GEM, R.drawable.jewel_blue);
        map.put(ItemType.YELLOW_GEM, R.drawable.jewel_yellow);
        map.put(ItemType.RED_GEM, R.drawable.jewel_red);
        map.put(ItemType.GREY_GEM, R.drawable.jewel_grey);
    }
}
