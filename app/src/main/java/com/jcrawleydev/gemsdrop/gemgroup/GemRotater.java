package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemRotater {


    private final int GEM_WIDTH;
    private final int HALF_WIDTH;
    private List<Gem> gems;
    private int numberOfGems;
    private GemGroup gemGroup;
    private Map<GemGroup.DetailedOrientation, GemGroup.DetailedOrientation> nextDetailedOrientation;


    public GemRotater(GemGroup gemGroup, int gemWidth){
         GEM_WIDTH = gemWidth;
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
