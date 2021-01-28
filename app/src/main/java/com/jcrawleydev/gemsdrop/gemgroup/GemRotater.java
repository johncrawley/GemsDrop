package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.List;

public class GemRotater {


    private final int GEM_WIDTH;
    private final int HALF_WIDTH;
    private List<Gem> gems;
    private int numberOfGems;
    private GemGroup gemGroup;


    public GemRotater(GemGroup gemGroup, int gemWidth){
         GEM_WIDTH = gemWidth;
         HALF_WIDTH = gemWidth /2;
         this.gemGroup = gemGroup;
         this.gems = gemGroup.getGems();
         numberOfGems = gems.size();
    }


    public void setGemCoordinates(GemGroup gemGroup){
        this.gemGroup = gemGroup;
        gems = gemGroup.getGems();
        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL) {
            assignHorizontalXCoordinatesToGems();
            assignHorizontalYCoordinatesToGems();
            return;
        }
        assignVerticalXCoordinatesToGems();
        assignVerticalYCoordinatesToGems();
    }


    public void rotate(){
            setGemCoordinates(gemGroup);
    }


    private void assignVerticalXCoordinatesToGems(){
        int xOffset = - HALF_WIDTH;
        for(Gem gem : gems){
            gem.setX(xOffset);
        }
    }


    private void assignVerticalYCoordinatesToGems(){
        int initialY = - HALF_WIDTH - (numberOfGems / 2 * GEM_WIDTH);
        for(int i = 0; i < numberOfGems; i++){
            int y = initialY + (i * GEM_WIDTH);
            gems.get(i).setY(y);
        }
    }


    private void assignHorizontalYCoordinatesToGems(){
        int yOffset = - HALF_WIDTH;
        for(Gem gem: gems){
            gem.setY(yOffset);
        }
    }


    private void assignHorizontalXCoordinatesToGems(){
        int initialX = - HALF_WIDTH - (numberOfGems / 2 * GEM_WIDTH);
        for(int i = 0; i < numberOfGems; i++){
            int x = initialX + (i * GEM_WIDTH);
            gems.get(i).setX(x);
        }
    }


}
