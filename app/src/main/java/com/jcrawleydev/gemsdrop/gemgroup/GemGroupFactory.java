package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemPaintOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GemGroupFactory {

    private final int NUMBER_OF_GEMS;
    private final int GEMS_INITIAL_Y;
    private final int INITIAL_POSITION;
    private final List<Gem.Color> colors;
    private final int floorY;
    private final int GEM_WIDTH;
    private final int borderWidth;
    private final GemPaintOptions gemPaintOptions;

    private GemGroupFactory(int numberOfGems, int initialPosition, int gemsInitialY, int gemWidth, int floorY, int borderWidth){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.INITIAL_POSITION = initialPosition;
        this.GEMS_INITIAL_Y = gemsInitialY;
        colors = Arrays.asList(Gem.Color.values());
        this.floorY = floorY;
        this.GEM_WIDTH = gemWidth;
        this.borderWidth = borderWidth;
        gemPaintOptions = new GemPaintOptions(gemWidth);
    }

    public GemGroup createGemGroup(){
        List<Gem> gems = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(getRandomColor(), gemPaintOptions.getGemPaint());
            gems.add(gem);
        }
        return new GemGroup(INITIAL_POSITION, GEMS_INITIAL_Y, GemGroup.Orientation.VERTICAL, gems, GEM_WIDTH, floorY, borderWidth);
    }



    private Gem.Color getRandomColor(){
        int index = ThreadLocalRandom.current().nextInt(colors.size());

        return colors.get(index);
    }


    public static class Builder{

        public Builder(){}

       private int numberOfGems, initialPosition, gemsInitialY, gemWidth, floorY, borderWidth;

        public Builder withInitialY(int initialY){
            this.gemsInitialY = initialY;
            return this;
        }

        public Builder withNumerOfGems(int numberOfGems){
            this.numberOfGems = numberOfGems;
            return this;
        }

        public Builder withInitialPosition(int initialPosition){
            this.initialPosition = initialPosition;
            return this;
        }

        public Builder withGemWidth(int gemWidth){
            this.gemWidth = gemWidth;
            return this;
        }


        public Builder withBorderWidth(int borderWidth){
            this.borderWidth = borderWidth;
            return this;
        }

        public Builder withFloorAt(int floorY){
            this.floorY = floorY;
            return this;
        }

        public GemGroupFactory build(){
            return new GemGroupFactory(numberOfGems, initialPosition, gemsInitialY, gemWidth, floorY, borderWidth);
        }

    }
}
