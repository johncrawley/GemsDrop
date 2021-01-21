package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GemGroupFactory {

    private final int NUMBER_OF_GEMS;
    private final int GEMS_INITIAL_X;
    private final int GEMS_INITIAL_Y;
    private final int INITIAL_POSITION;
    private final List<Gem.Color> colors;


    public GemGroupFactory(int numberOfGems, int initialPosition, int gemsInitialX, int gemsInitialY, int gemWidth){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.INITIAL_POSITION = initialPosition;
        this.GEMS_INITIAL_X = gemsInitialX;
        this.GEMS_INITIAL_Y = gemsInitialY;
        colors = Arrays.asList(Gem.Color.values());
    }

    public GemGroupFactory(int numberOfGems, int gemsInitialX, int gemsInitialY, int gemWidth){
        this(numberOfGems,1,gemsInitialX, gemsInitialY, gemWidth);
    }


    public GemGroup createGemGroup(){

        List<Gem> gems = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(getRandomColor());
            gems.add(gem);
        }
        return new GemGroup(1, GEMS_INITIAL_X, GEMS_INITIAL_Y, GemGroup.Orientation.VERTICAL, gems);
    }



    private Gem.Color getRandomColor(){
        int index = ThreadLocalRandom.current().nextInt(colors.size());
        return colors.get(index);
    }

}
