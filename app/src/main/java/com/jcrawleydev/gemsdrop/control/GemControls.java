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
        if(gemGroup.getPosition() <= 0){
            return;
        }
        gemGroup.decrementPosition();
    }

    public void moveRight(){
        gemGroup.incrementPosition();
    }



}
