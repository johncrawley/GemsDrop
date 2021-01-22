package com.jcrawleydev.gemsdrop.control;

import com.jcrawleydev.gemsdrop.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;

public class GemControls {

    private GemGrid gemGrid;
    private GemGroup gemGroup;


    public GemControls(GemGroup gemGroup, GemGrid gemGrid){
        this.gemGroup = gemGroup;
        this.gemGrid = gemGrid;
    }


    public void moveLeft(){
        int minPosition = getMinPosition();
        if(gemGroup.getPosition() <= minPosition){
            return;
        }
        gemGroup.decrementPosition();
    }


    public void moveRight(){
        int maxPosition = gemGrid.getNumberOfColumns() -1;

        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            maxPosition -= gemGroup.getNumberOfGems() /2;
        }

        if(gemGroup.getPosition() >= maxPosition){
            return;
        }
        gemGroup.incrementPosition();
    }


    public void rotate(){
        if(isVerticalAndAtAnEdge()){
            return;
        }
        gemGroup.rotate();

    }

    private boolean isVerticalAndAtAnEdge(){

        boolean isAtAnEdge = gemGroup.getPosition() == getMinPosition() || gemGroup.getPosition() == gemGrid.getNumberOfColumns() -1;
        return isAtAnEdge && gemGroup.getOrientation() == GemGroup.Orientation.VERTICAL;
    }


    private int getMinPosition(){
        int minPosition = 0;
        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            minPosition = gemGroup.getNumberOfGems() / 2;
        }
        return minPosition;
    }



}
