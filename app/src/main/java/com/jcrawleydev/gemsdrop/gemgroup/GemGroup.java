package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemGroup {

    private List<Gem> gems;
    private List<Gem> reversedOrderGems;
    private Orientation orientation;
    private DetailedOrientation detailedOrientation = DetailedOrientation.FIRST_TO_LAST;
    private int x,y, position, dropIncrement, gemWidth, middleYPosition, floorY;
    private float dropMultiple = 0f;
    private float currentDropIncrement = 0f;
    public enum DetailedOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }
    private GemRotater gemRotater;
    private boolean wasUpdated;


    public GemGroup(int initialPosition, int initialY, Orientation orientation, List<Gem> gems, int gemWidth,  int floorY){
        this.position = initialPosition;
        this.gems = new ArrayList<>(gems);
        this.gemWidth = gemWidth;
        this.floorY = floorY;
        this.x = (initialPosition * gemWidth) + gemWidth /2;
        this.y = initialY;
        setupMiddleYPosition();
        this.reversedOrderGems = new ArrayList<>(gems);
        Collections.reverse(reversedOrderGems);
        this.orientation = orientation;
        if(orientation == Orientation.VERTICAL){
            detailedOrientation = DetailedOrientation.TOP_TO_BOTTOM;
        }
        this.gemRotater = new GemRotater(this, gemWidth);
        gemRotater.setGemCoordinates(this);
    }


    private void setupMiddleYPosition(){
        this.middleYPosition = ((floorY - y) / gemWidth) -1;
    }


    public void setGemWidth(int width){
        this.gemWidth = width;
    }


    public boolean wasUpdated(){
        return wasUpdated;
    }


    public void setUpdated(boolean updated){
        this.wasUpdated = updated;
    }


    public void setDropMultiple(float dropMultiple){
        this.dropMultiple = dropMultiple;
        this.dropIncrement = (int)(gemWidth * dropMultiple);
    }

    public int getPosition(){
        return position;
    }


    public Orientation getOrientation(){
        return orientation;
    }


    public void decrementPosition(){
        x -= gemWidth;
        position--;
        wasUpdated = true;
    }

    public void incrementPosition(){
        x += gemWidth;
        position++;
        wasUpdated = true;
    }

    public void setPosition(int position){
        this.position = position;
    }

    public void rotate(){
        gemRotater.rotate();
        wasUpdated = true;
    }

    public void setDetailedOrientation(DetailedOrientation trueOrientation){
        detailedOrientation = trueOrientation;
    }


    public DetailedOrientation getDetailedOrientation(){
        return detailedOrientation;
    }


    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
    }


    public List<Gem> getGems(){
        if(detailedOrientation == DetailedOrientation.TOP_TO_BOTTOM || detailedOrientation == DetailedOrientation.FIRST_TO_LAST){
           return gems;
        }
        return reversedOrderGems;
    }

    // NB gems if vertical orientation, a gem group will be printed top-to-bottom
    //  but the same gem group will be added bottom-to-top to a grid column
    public List<Gem> getGemsToAddToGrid(){
        return copyOf(getGridGems());
    }


    private List<Gem> getGridGems(){
        if(orientation == Orientation.HORIZONTAL){
            return getGems();
        }
        if(detailedOrientation == DetailedOrientation.TOP_TO_BOTTOM){
                return reversedOrderGems;
        }
        return gems;
    }


    public void setGemsInvisible(){
        for(Gem gem: gems){
            gem.setInvisible();
        }
        wasUpdated = true;
    }


    private List<Gem> copyOf(List<Gem> gems){
        List<Gem> copiedList = new ArrayList<>(gems.size());
        for(Gem gem: gems){
            copiedList.add(gem.clone());
        }
        return copiedList;
    }


    public int getNumberOfGems(){
        return gems.size();
    }


    public int getX(){
        return x;
    }


    public int getY(){
        return y;
    }


    public void drop(){
        currentDropIncrement += dropMultiple;
        while(currentDropIncrement >= 1){
            currentDropIncrement -= 1;
            middleYPosition --;
        }
        y += dropIncrement;
        wasUpdated = true;
    }


    public int getBottomPosition(){
        if( orientation == Orientation.HORIZONTAL){
            return middleYPosition + 1;
        }
        return (middleYPosition + getNumberOfGems() /2) -1;
    }


    public List<Integer> getGemPositions(){
        List<Integer> positions = new ArrayList<>();
        int leftMostPosition = position - getNumberOfGems() / 2;
        for(int i=0; i< gems.size(); i++){
            positions.add(leftMostPosition + i);
        }
        return positions;
    }

}
