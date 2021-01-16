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

    private enum TrueOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }

    private Map<TrueOrientation, TrueOrientation> nextTrueOrientation;

    public GemGroup(int initialPosition, int initialX, int initialY, Orientation orientation, List<Gem> gems){
        this.position = initialPosition;
        this.gems = new ArrayList<>(gems);
        this.x = initialX;
        this.y = initialY;
        this.reversedOrderGems = new ArrayList<>(gems);
        Collections.reverse(reversedOrderGems);
        this.orientation = orientation;
        if(orientation == Orientation.VERTICAL){
            trueOrientation = TrueOrientation.TOP_TO_BOTTOM;
        }
        setupTrueOrientation();
    }

    private void setupTrueOrientation(){
        nextTrueOrientation = new HashMap<>(4);
        nextTrueOrientation.put(TrueOrientation.BOTTOM_TO_TOP, TrueOrientation.FIRST_TO_LAST);
        nextTrueOrientation.put(TrueOrientation.FIRST_TO_LAST, TrueOrientation.TOP_TO_BOTTOM);
        nextTrueOrientation.put(TrueOrientation.TOP_TO_BOTTOM, TrueOrientation.LAST_TO_FIRST);
        nextTrueOrientation.put(TrueOrientation.LAST_TO_FIRST, TrueOrientation.BOTTOM_TO_TOP);

    }

    public void setDropIncrement(int dropIncrement){
        this.dropIncrement = dropIncrement;
    }

    public int getPosition(){
        return position;
    }


    public Orientation getOrientation(){
        return orientation;
    }


    public void rotate(){
        orientation = orientation == Orientation.VERTICAL ? Orientation.HORIZONTAL : Orientation.VERTICAL;
        trueOrientation = nextTrueOrientation.get(trueOrientation);
    }


    public List<Gem> getGems(){
        if(trueOrientation == TrueOrientation.TOP_TO_BOTTOM || trueOrientation == TrueOrientation.FIRST_TO_LAST){
           return gems;
        }
        return reversedOrderGems;
    }


    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void drop(){
        y+=dropIncrement;
    }



}
