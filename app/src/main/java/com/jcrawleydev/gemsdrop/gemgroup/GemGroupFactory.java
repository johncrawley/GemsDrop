package com.jcrawleydev.gemsdrop.gemgroup;


import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gem.GemPaintOptions;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GemGroupFactory {

    private final int NUMBER_OF_GEMS;
    private final float gemsInitialY;
    private final int initialColumnPosition;
    private final List<Gem.Color> colors;
    private final int floorY;
    private final float gemWidth;
    private final int borderWidth;
    private Random random;
    private final GemGrid gemGrid;
    private final float dropValue;
    private int initialMiddleYPosition;


    private GemGroupFactory(GemGrid gemGrid, int numberOfGems, int initialPosition, float gemsInitialY, float gemWidth, float dropValue, int floorY, int borderWidth){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.gemGrid = gemGrid;
        this.initialColumnPosition = initialPosition;
        this.gemsInitialY = gemsInitialY;
        colors = Arrays.asList(Gem.Color.RED, Gem.Color.BLUE, Gem.Color.PURPLE, Gem.Color.GREEN, Gem.Color.YELLOW);
        this.floorY = floorY;
        this.gemWidth = gemWidth;
        this.dropValue = dropValue;
        this.borderWidth = borderWidth;
        setupMiddleYPosition();
    }


    private void setupMiddleYPosition(){
        this.initialMiddleYPosition = (int)((floorY - gemsInitialY) / gemWidth);
        System.out.println("GemGroupFactory.setupMiddleYPosition() : Initial Y position: " + initialMiddleYPosition);
    }



    public GemGroup createGemGroup(){
        List<Gem> gems = new ArrayList<>();
         random = new Random(System.currentTimeMillis());
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(getRandomColor());
            gem.setInvisible();
            gems.add(gem);
        }

        return GemGroup.Builder.newInstance()
                .gems(gems)
                .initialPosition(initialColumnPosition)
                .initialY(gemsInitialY)
                .orientation(GemGroup.Orientation.VERTICAL)
                .gemWidth(gemWidth)
                .initialMiddleYPosition(initialMiddleYPosition)
                .borderWidth(borderWidth)
                .build();
    }


    private Gem.Color getRandomColor(){
        int index = random.nextInt(colors.size());
        return colors.get(index);
    }


    public static class Builder{

        public Builder(){}

        private int numberOfGems, initialPosition, floorY, borderWidth;
        private float dropValue, gemsInitialY, gemWidth;
        private GemGrid gemGrid;

        public Builder withInitialY(float initialY){
            this.gemsInitialY = initialY;
            return this;
        }

        public Builder withNumberOfGems(int numberOfGems){
            this.numberOfGems = numberOfGems;
            return this;
        }

        public Builder withInitialPosition(int initialPosition){
            this.initialPosition = initialPosition;
            return this;
        }

        public Builder withGemWidth(float gemWidth){
            this.gemWidth = gemWidth;
            return this;
        }


        public Builder dropValue(float dropValue){
            this.dropValue = dropValue;
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

        public Builder withGemGrid(GemGrid gemGrid){
            this.gemGrid = gemGrid;
            return this;
        }


        public GemGroupFactory build(){
            return new GemGroupFactory(gemGrid, numberOfGems, initialPosition, gemsInitialY, gemWidth, dropValue, floorY, borderWidth);
        }

    }
}
