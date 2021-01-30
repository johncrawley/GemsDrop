package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GemGroupFactory {

    private  int NUMBER_OF_GEMS;
    private  int GEMS_INITIAL_X;
    private  int GEMS_INITIAL_Y;
    private  int INITIAL_POSITION;
    private  List<Gem.Color> colors;
    private  int floorY;

    private GemGroupFactory(int numberOfGems, int initialPosition, int gemsInitialX, int gemsInitialY, int gemWidth, int floorY){
        this.NUMBER_OF_GEMS = numberOfGems;
        this.INITIAL_POSITION = initialPosition;
        this.GEMS_INITIAL_X = gemsInitialX;
        this.GEMS_INITIAL_Y = gemsInitialY;
        colors = Arrays.asList(Gem.Color.values());
        this.floorY = floorY;
    }

    public GemGroup createGemGroup(){

        List<Gem> gems = new ArrayList<>();
        for(int i=0; i< NUMBER_OF_GEMS; i++){
            Gem gem = new Gem(getRandomColor());
            gems.add(gem);
        }
        return new GemGroup(INITIAL_POSITION, GEMS_INITIAL_X, GEMS_INITIAL_Y, GemGroup.Orientation.VERTICAL, gems, floorY);
    }



    private Gem.Color getRandomColor(){
        int index = ThreadLocalRandom.current().nextInt(colors.size());

        return colors.get(index);
    }


    public static class Builder{

        public Builder(){}


       // public GemGroupFactory(int numberOfGems, int initialPosition, int gemsInitialX, int gemsInitialY, int gemWidth, int floorY){
           private int numberOfGems, initialPosition, gemsInitialX, gemsInitialY, gemWidth, floorY;

        public Builder withInitialCoords(int initialX, int initialY){
            this.gemsInitialX = initialX;
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

        public Builder withFloorAt(int floorY){
            this.floorY = floorY;
            return this;
        }

        public GemGroupFactory build(){
            return new GemGroupFactory(numberOfGems, initialPosition, gemsInitialX, gemsInitialY, gemWidth, floorY);
        }

    }
}
