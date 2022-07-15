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
    private final int floorY;
    public enum DetailedOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }
    private final GemRotator gemRotator;
    private boolean wasUpdated;
    private final GemGroupDropper gemGroupDropper;
    private boolean isFirstDrop = true;
    private final GemGrid gemGrid;
    private int[] dropPositions;
    private final float dropValue;
    private final int FLOOR_POSITION = 1;


    public GemGroup(GemGrid gemGrid, int initialPosition, float initialY, Orientation orientation, List<Gem> gems, float gemWidth, float dropValue, int floorY, int borderWidth){
        log("************************************** floor Entered GemGroup()");
        this.gemGrid = gemGrid;
        this.xPosition = initialPosition;
        this.gems = new ArrayList<>(gems);
        this.gemWidth = gemWidth;
        this.dropValue = dropValue;
        this.floorY = floorY;
        assignXYFrom(borderWidth, initialPosition, initialY);
        setupMiddleYPosition();
        this.reversedOrderGems = new ArrayList<>(gems);
        Collections.reverse(reversedOrderGems);
        this.orientation = orientation;
        this.gemGroupDropper = new GemGroupDropper(this, gemWidth);//gemGroupDropper;
        if(orientation == Orientation.VERTICAL){
            detailedOrientation = DetailedOrientation.TOP_TO_BOTTOM;
        }
        setupDropPositions();
        this.gemRotator = new GemRotator(this, gemWidth);
        gemRotator.setGemCoordinates(this);
    }


    private void setupDropPositions(){
        int initialGemPosition = middleYPosition;
        setupMiddleYPosition();
        dropPositions = new int[middleYPosition + 1];
        log("floor Y : " + floorY);
        float lowestGemY = floorY - gemWidth;
        float currentY = -dropValue;
        for(int i=0; i< initialGemPosition; i++){
            currentY += dropValue;
            int position = i+1;
            dropPositions[i] = (int)(lowestGemY - currentY);
            log("y for position  " + position + " : "  + dropPositions[i]);
        }
    }

    private int getYForPosition(int position){
        return dropPositions[position -1];
    }


    private void assignXYFrom(int borderWidth, int initialPosition, float initialY){
        this.x = borderWidth + (initialPosition * gemWidth) + gemWidth /2f;
        this.y = initialY;
        y+= getYRemainderFromFloor();
        y+=3;
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
        middleYPosition = Math.max(middleYPosition, FLOOR_POSITION);
        log("floor, decrementMiddleYPosition(), now at: " + middleYPosition);
    }


    public void dropBy(int dropIncrement){
        int verticalPositionAdjustment = isVertical() ? 1 : 0;
        y = getYForPosition(middleYPosition + verticalPositionAdjustment );
        wasUpdated = true;
    }


    public void setXPosition(int xPosition){
        this.xPosition = xPosition;
    }


    public void rotate(){
        gemRotator.rotate();
        wasUpdated = true;
    }

    private void log(String msg){
        System.out.println("^^^ GemGroup : " + msg);
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
        log("£££££££££ Entered setGemsInvisible()");
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


    public void drop(){
        if(isFirstDrop){
            setGemsVisible();
            isFirstDrop = false;
        }
        gemGroupDropper.drop();
    }


    public void enableQuickDrop(){
        gemGroupDropper.enableQuickDrop();
    }


    public boolean haveAllGemsSettled(){
        int i=0;
        for(Gem gem: gems){
            if(gem.isVisible()){
                log("haveAllGemsSettled() gem at index : " + i + " is visible");
                return false;
            }
            i++;
        }
        return true;
    }


    public float getBottomPosition(){
        if( orientation == Orientation.HORIZONTAL){
            return middleYPosition;
        }
        return (middleYPosition + (getNumberOfGems()) /2f) -1;
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
        this.middleYPosition = (int)((floorY - y) / gemWidth) -1;
    }


    private float getYRemainderFromFloor(){
        return y == 0 ? 0 : floorY % y;
    }


    private List<Gem> copyOf(List<Gem> gems){
        List<Gem> copiedList = new ArrayList<>(gems.size());
        for(Gem gem: gems){
            copiedList.add(gem.clone());
        }
        return copiedList;
    }


}
