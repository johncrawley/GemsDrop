package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.List;

public class GemGroupFactory {

    private final int NUMBER_OF_GEMS;
    private final int INITIAL_X;
    private final int INITIAL_Y;

    public GemGroupFactory(int numberOfGems, int initialX, int initialY, int gemWidth){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.INITIAL_X = initialX;
        this.INITIAL_Y = initialY;
    }


    public GemGroup createGemGroup(){

        List<Gem> gems = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(Gem.Color.BLUE);
            gems.add(gem);
        }

        return new GemGroup(1, INITIAL_X,INITIAL_Y, GemGroup.Orientation.VERTICAL, gems);
    }


}
