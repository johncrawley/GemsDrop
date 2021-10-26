package com.jcrawleydev.gemsdrop.gemgroup;


import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemPaintOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GemGroupFactory {

    private final int NUMBER_OF_GEMS;
    private final int gemsInitialY;
    private final int initialColumnPosition;
    private final List<Gem.Color> colors;
    private final int floorY;
    private final int GEM_WIDTH;
    private final int borderWidth;
    private final GemPaintOptions gemPaintOptions;
    private Random random;


    private GemGroupFactory(int numberOfGems, int initialPosition, int gemsInitialY, int gemWidth, int floorY, int borderWidth){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.initialColumnPosition = initialPosition;
        this.gemsInitialY = gemsInitialY;
        colors = Arrays.asList(Gem.Color.RED, Gem.Color.BLUE, Gem.Color.PURPLE, Gem.Color.GREEN, Gem.Color.YELLOW);
        this.floorY = floorY;
        this.GEM_WIDTH = gemWidth;
        this.borderWidth = borderWidth;
        gemPaintOptions = new GemPaintOptions(gemWidth);
    }


    public GemGroup createGemGroup(){
        List<Gem> gems = new ArrayList<>();
         random = new Random(System.currentTimeMillis());
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(getRandomColor(), gemPaintOptions.getGemPaint());
            gem.setInvisible();
            gems.add(gem);
        }
        return new GemGroup(initialColumnPosition, gemsInitialY, GemGroup.Orientation.VERTICAL, gems, GEM_WIDTH, floorY, borderWidth);
    }


    private Gem.Color getRandomColor(){
        int index = random.nextInt(colors.size());
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
