package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GemGroupFactory {

    private final int NUMBER_OF_GEMS;
    private final int INITIAL_X;
    private final int INITIAL_Y;
    private final List<Gem.Color> colors;


    public GemGroupFactory(int numberOfGems, int initialX, int initialY, int gemWidth){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.INITIAL_X = initialX;
        this.INITIAL_Y = initialY;
        colors = Arrays.asList(Gem.Color.values());
    }


    public GemGroup createGemGroup(){

        List<Gem> gems = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(getRandomColor());
            gems.add(gem);
        }
        return new GemGroup(1, INITIAL_X,INITIAL_Y, GemGroup.Orientation.VERTICAL, gems);
    }



    private Gem.Color getRandomColor(){
        int index = ThreadLocalRandom.current().nextInt(colors.size());
        return colors.get(index);
    }

}
