package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemRotator  {


    private final float gemWidth;
    private final float HALF_WIDTH;
    private List<Gem> gems;
    private final int numberOfGems;
    private GemGroup gemGroup;
    private Map<GemGroup.DetailedOrientation, GemGroup.DetailedOrientation> nextDetailedOrientation;


    public GemRotator(GemGroup gemGroup, float gemWidth){
         this.gemWidth = gemWidth;
         HALF_WIDTH = gemWidth /2;
         this.gemGroup = gemGroup;
         this.gems = gemGroup.getGems();
         numberOfGems = gems.size();
         setupDetailedOrientation();
    }


    private void setupDetailedOrientation(){
        nextDetailedOrientation = new HashMap<>(4);
        nextDetailedOrientation.put(GemGroup.DetailedOrientation.BOTTOM_TO_TOP, GemGroup.DetailedOrientation.FIRST_TO_LAST);
        nextDetailedOrientation.put(GemGroup.DetailedOrientation.FIRST_TO_LAST, GemGroup.DetailedOrientation.TOP_TO_BOTTOM);
        nextDetailedOrientation.put(GemGroup.DetailedOrientation.TOP_TO_BOTTOM, GemGroup.DetailedOrientation.LAST_TO_FIRST);
        nextDetailedOrientation.put(GemGroup.DetailedOrientation.LAST_TO_FIRST, GemGroup.DetailedOrientation.BOTTOM_TO_TOP);
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
        adjustOrientation();
        adjustTrueOrientation();
        setGemCoordinates(gemGroup);
    }


    private void adjustOrientation(){
        GemGroup.Orientation rotatedOrientation = gemGroup.getOrientation() == GemGroup.Orientation.VERTICAL ?
                GemGroup.Orientation.HORIZONTAL : GemGroup.Orientation.VERTICAL;
        gemGroup.setOrientation(rotatedOrientation);
    }


    private void adjustTrueOrientation(){
        GemGroup.DetailedOrientation current = gemGroup.getDetailedOrientation();
        gemGroup.setDetailedOrientation(nextDetailedOrientation.get(current));

    }

    private void assignVerticalXCoordinatesToGems(){
        float xOffset = - HALF_WIDTH;
        for(Gem gem : gems){
            gem.setX(xOffset);
        }
    }


    private void assignVerticalYCoordinatesToGems(){
        float initialY = - HALF_WIDTH - (numberOfGems / 2f * gemWidth);
        for(int i = 0; i < numberOfGems; i++){
            float y = initialY + (i * gemWidth);
            gems.get(i).setY(y);
        }
    }


    private void assignHorizontalYCoordinatesToGems(){
        float yOffset = - HALF_WIDTH;
        for(Gem gem: gems){
            gem.setY(yOffset);
        }
    }


    private void assignHorizontalXCoordinatesToGems(){
        float initialX = - HALF_WIDTH - (numberOfGems / 2 * gemWidth);
        for(int i = 0; i < numberOfGems; i++){
            float x = initialX + (i * gemWidth);
            gems.get(i).setX(x);
        }
    }


}
