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
    private int x,y, xPosition, gemWidth, middleYPosition, floorY;
    public enum DetailedOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }
    private GemRotater gemRotater;
    private boolean wasUpdated;
    private GemGroupDropper gemGroupDropper;


    public GemGroup(int initialPosition, int initialY, Orientation orientation, List<Gem> gems, int gemWidth,  int floorY){
        this.xPosition = initialPosition;
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
        gemGroupDropper = new GemGroupDropper(this, gemWidth);
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


    public int getXPosition(){
        return xPosition;
    }


    public int getBaseXPosition(){
        if(isVertical()){
            return xPosition;
        }
        return xPosition - getNumberOfGems() /2;
    }


    public int getEndXPosition(){
        if(isVertical()){
            return xPosition;
        }
        return xPosition + getNumberOfGems() /2;
    }


    public Orientation getOrientation(){
        return orientation;
    }


    public boolean isVertical(){
        return this.orientation == Orientation.VERTICAL;
    }


    public void decrementPosition(){
        x -= gemWidth;
        xPosition--;
        wasUpdated = true;
    }

    public void decrementMiddleYPosition(){
        middleYPosition--;
    }

    public void increaseYBy(int dropIncrement){
        y += dropIncrement;
        wasUpdated = true;
    }


    public void incrementPosition(){
        x += gemWidth;
        xPosition++;
        wasUpdated = true;
    }


    public void setXPosition(int xPosition){
        this.xPosition = xPosition;
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


    public void rotateToVertical(){
        this.orientation = Orientation.VERTICAL;
    }


    public void rotateToHorizontal(){
        this.orientation = Orientation.HORIZONTAL;
    }


    public List<Gem> getGems(){
        if(detailedOrientation == DetailedOrientation.TOP_TO_BOTTOM || detailedOrientation == DetailedOrientation.FIRST_TO_LAST){
           return gems;
        }
        return reversedOrderGems;
    }


    // NB gems if vertical orientation, a gem group will be printed top-to-bottom
    //  but the same gem group will be added bottom-to-top to a grid column
    public List<Gem> getCopyOfGemsToAddToGrid(){
        return copyOf(getGridGems());
    }


    public List<Gem> getGridGems(){
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
        gemGroupDropper.drop();
    }

    public void enableQuickDrop(){
        gemGroupDropper.enableQuickDrop();
    }


    public boolean haveAllGemsSettled(){
        for(Gem gem: gems){
            if(gem.isVisible()){
                return false;
            }
        }
        return true;
    }


    public int getBottomPosition(){
        if( orientation == Orientation.HORIZONTAL){
            return middleYPosition + 1;
        }
        return (middleYPosition + getNumberOfGems() /2) -1;
    }


    public List<Integer> getGemPositions(){
        List<Integer> positions = new ArrayList<>();
        int leftMostPosition = xPosition - getNumberOfGems() / 2;
        for(int i=0; i< gems.size(); i++){
            positions.add(leftMostPosition + i);
        }
        return positions;
    }


    private void setupMiddleYPosition(){
        this.middleYPosition = ((floorY - y) / gemWidth) -1;
    }



    private List<Gem> copyOf(List<Gem> gems){
        List<Gem> copiedList = new ArrayList<>(gems.size());
        for(Gem gem: gems){
            copiedList.add(gem.clone());
        }
        return copiedList;
    }




}
