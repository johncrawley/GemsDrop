package com.jcrawleydev.gemsdrop.gemgroup;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GemGroup {

    private List<Gem> gems;
    private List<Gem> reversedOrderGems;
    private int position;
    private Orientation orientation;
    private int x,y;
    private TrueOrientation trueOrientation = TrueOrientation.FIRST_TO_LAST;
    private int dropIncrement;
    private int gemWidth = 150;
    private int middleYPosition;
    private int floorY;
    float dropMultiple = 0f;

    private enum TrueOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }
    private GemRotater gemRotater;

    private Map<TrueOrientation, TrueOrientation> nextTrueOrientation;

    public GemGroup(int initialPosition, int initialX, int initialY, Orientation orientation, List<Gem> gems, int floorY){
        this.position = initialPosition;
        this.gems = new ArrayList<>(gems);
        this.floorY = floorY;
        this.x = (initialPosition * gemWidth) + gemWidth /2;
        this.y = initialY;
        setupMiddleYPosition();
        this.reversedOrderGems = new ArrayList<>(gems);
        Collections.reverse(reversedOrderGems);
        this.orientation = orientation;
        if(orientation == Orientation.VERTICAL){
            trueOrientation = TrueOrientation.TOP_TO_BOTTOM;
        }
        setupTrueOrientation();
        this.gemRotater = new GemRotater(this, gemWidth);
        gemRotater.setGemCoordinates(this);
    }


    private void setupMiddleYPosition(){
        this.middleYPosition = ((floorY - y) / gemWidth) -1;
    }


    public void setGemWidth(int width){
        this.gemWidth = width;
    }


    private void setupTrueOrientation(){
        nextTrueOrientation = new HashMap<>(4);
        nextTrueOrientation.put(TrueOrientation.BOTTOM_TO_TOP, TrueOrientation.FIRST_TO_LAST);
        nextTrueOrientation.put(TrueOrientation.FIRST_TO_LAST, TrueOrientation.TOP_TO_BOTTOM);
        nextTrueOrientation.put(TrueOrientation.TOP_TO_BOTTOM, TrueOrientation.LAST_TO_FIRST);
        nextTrueOrientation.put(TrueOrientation.LAST_TO_FIRST, TrueOrientation.BOTTOM_TO_TOP);

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
        this.x -= gemWidth;
        this.position--;
    }

    public void incrementPosition(){
        this.x += gemWidth;
        this.position++;
    }

    public void rotate(){
        orientation = orientation == Orientation.VERTICAL ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        trueOrientation = nextTrueOrientation.get(trueOrientation);
        gemRotater.rotate();
    }



    public List<Gem> getGems(){
        if(trueOrientation == TrueOrientation.TOP_TO_BOTTOM || trueOrientation == TrueOrientation.FIRST_TO_LAST){
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
        if(trueOrientation == TrueOrientation.TOP_TO_BOTTOM){
                return reversedOrderGems;
        }
        return gems;
    }


    public void setGemsInvisible(){
        for(Gem gem: gems){
            gem.setInvisible();
        }
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

    private float currentDropIncrement = 0f;

    public void drop(){
        currentDropIncrement += dropMultiple;
        while(currentDropIncrement >= 1){
            currentDropIncrement -= 1;
            middleYPosition --;
        }
        y += dropIncrement;
        //log("dropping gemGroup, current middle Y position: " + middleYPosition + " currentDropIncrement: " + currentDropIncrement);
    }

    public int getBottomPosition(){
        if( orientation == Orientation.HORIZONTAL){
            return middleYPosition + 1;
        }
        return (middleYPosition + getNumberOfGems() /2) -1;
    }

    private void log(String msg){
        System.out.println("GemGroup: " + msg);
    }



}
