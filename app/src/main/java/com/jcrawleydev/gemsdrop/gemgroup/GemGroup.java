package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;
import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemGroup {

    private final List<Gem> gems;
    private final List<Gem> reversedOrderGems;
    private Orientation orientation;
    private DetailedOrientation detailedOrientation = DetailedOrientation.FIRST_TO_LAST;
    private float x,y;
    private int xPosition, middleYPosition;
    private final float gemWidth;
    public enum DetailedOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }
    private final GemRotator gemRotator;
    private boolean wasUpdated;
    private boolean isFirstDrop = true;
    private boolean isQuickDropEnabled;


    public GemGroup(GemGroup.Builder builder){
        this.xPosition = builder.initialPosition;
        this.gems = new ArrayList<>(builder.gems);
        this.gemWidth = builder.gemWidth;
        this.middleYPosition = builder.initialMiddleYPosition;
        assignXYFrom(builder.borderWidth, builder.initialPosition, builder.initialY);
        this.reversedOrderGems = new ArrayList<>(gems);
        Collections.reverse(reversedOrderGems);
        this.orientation = builder.orientation;
        if(orientation == Orientation.VERTICAL){
            detailedOrientation = DetailedOrientation.TOP_TO_BOTTOM;
        }
        this.gemRotator = new GemRotator(this, gemWidth);
        gemRotator.setGemCoordinates(this);
    }



    private void assignXYFrom(int borderWidth, int initialPosition, float initialY){
        this.x = borderWidth + (initialPosition * gemWidth) + gemWidth /2f;
        this.y = initialY;
    }


    public void setGemsVisible(){
        for(Gem gem: gems){
            gem.setVisible();
        }
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



    public void incrementPosition(){
        x += gemWidth;
        xPosition++;
        wasUpdated = true;
    }


    public void decrementPosition(){
        x -= gemWidth;
        xPosition--;
        wasUpdated = true;
    }


    public void decrementMiddleYPosition(){
        middleYPosition--;
        if(isVertical()){
            return;
        }
        int floorPosition = 1;
        middleYPosition = Math.max(middleYPosition, floorPosition);
    }


    public void dropBy(float dropIncrement){
        y += dropIncrement;
        wasUpdated = true;
    }


    public void dropBy(){
        setGemsVisibleOnFirstDrop();
        float dropFactor = 0.5f;
        y+= (gemWidth * dropFactor);
       // decrementMiddleYPosition();
        wasUpdated = true;
    }


    private void setGemsVisibleOnFirstDrop(){
        if(isFirstDrop){
            setGemsVisible();
            isFirstDrop = false;
        }
    }


    public void setXPosition(int xPosition){
        this.xPosition = xPosition;
    }


    public void rotate(){
        gemRotator.rotate();
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


    public float getX(){
        return x;
    }


    public float getY(){
        return y;
    }


    public void enableQuickDrop(){
        isQuickDropEnabled = true;
    }


    public  boolean isQuickDropEnabled(){
        return isQuickDropEnabled;
    }


    public boolean haveAllGemsSettled(){
        for(Gem gem: gems){
            if(gem.isVisible()){
                return false;
            }
        }
        return true;
    }


    public float getBottomPosition(){
        return middleYPosition;
    }


    public List<Integer> getGemPositions(){
        List<Integer> positions = new ArrayList<>();
        int leftMostPosition = xPosition - getNumberOfGems() / 2;
        for(int i=0; i< gems.size(); i++){
            positions.add(leftMostPosition + i);
        }
        return positions;
    }


    private List<Gem> copyOf(List<Gem> gems){
        List<Gem> copiedList = new ArrayList<>(gems.size());
        for(Gem gem: gems){
            copiedList.add(gem.clone());
        }
        return copiedList;
    }


    public static class Builder{
        GemGrid gemGrid;
        int initialPosition;
        float initialY;
        Orientation orientation = Orientation.VERTICAL;
        List<Gem> gems;
        float gemWidth;
        int floorY;
        int borderWidth;
        int initialMiddleYPosition;

        public static Builder newInstance(){
            return new Builder();
        }


        public GemGroup.Builder gemGrid(GemGrid gemGrid){
            this.gemGrid = gemGrid;
            return this;
        }


        public GemGroup.Builder initialPosition(int initialPosition){
            this.initialPosition = initialPosition;
            return this;
        }


        public GemGroup.Builder orientation(Orientation orientation){
            this.orientation = orientation;
            return this;
        }


        public GemGroup.Builder gems(List<Gem> gems){
            this.gems = gems;
            return this;
        }


        public GemGroup.Builder gemWidth(float gemWidth){
            this.gemWidth = gemWidth;
            return this;
        }


        public GemGroup.Builder initialMiddleYPosition(int initialMiddleYPosition){
            this.initialMiddleYPosition = initialMiddleYPosition;
            return this;
        }


        public GemGroup.Builder floorY(int floorY){
            this.floorY = floorY;
            return this;
        }


        public GemGroup.Builder initialY(float initialY){
            this.initialY = initialY;
            return this;
        }


        public GemGroup.Builder borderWidth(int borderWidth){
            this.borderWidth = borderWidth;
            return this;
        }


        public GemGroup build(){
            return new GemGroup(this);
        }
    }

}
