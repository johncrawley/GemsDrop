package com.jcrawleydev.gemsdrop;

import com.jcrawleydev.gemsdrop.gem.Gem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GemGroup {

    private List<Gem> gems;
    private List<Gem> reversedOrderGems;
    private int position;
    private Orientation orientation;
    private TrueOrientation trueOrientation = TrueOrientation.FIRST_TO_LAST;

    private enum TrueOrientation { FIRST_TO_LAST, TOP_TO_BOTTOM, LAST_TO_FIRST, BOTTOM_TO_TOP }
    public enum Orientation { HORIZONTAL, VERTICAL }

    public GemGroup(int initialPosition, Orientation orientation, List<Gem> gems){
        this.position = initialPosition;
        this.gems = new ArrayList<>(gems);
        this.reversedOrderGems = new ArrayList<>(gems);
        Collections.reverse(reversedOrderGems);
        this.orientation = orientation;
        if(orientation == Orientation.VERTICAL){
            trueOrientation = TrueOrientation.TOP_TO_BOTTOM;
        }

    }

    public int getPosition(){
        return position;
    }

    public Orientation getOrientation(){
        return this.orientation;
    }

    public List<Gem> getGems(){
        if(trueOrientation == TrueOrientation.TOP_TO_BOTTOM || trueOrientation == TrueOrientation.LAST_TO_FIRST){
           return reversedOrderGems;
        }
        return gems;
    }

}
