package com.jcrawleydev.gemsdrop.control;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;

public class GemControls {

    private GemGrid gemGrid;
    private GemGroup gemGroup;
    private boolean isActivated = true;


    public GemControls(GemGroup gemGroup, GemGrid gemGrid){
        this.gemGroup = gemGroup;
        this.gemGrid = gemGrid;
    }

    public GemControls(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }

    public void setGemGroup(GemGroup gemGroup){
        this.gemGroup = gemGroup;
    }


    public void deactivate(){
        isActivated = false;
    }

    public void reactivate(){
        isActivated = true;
    }

    public void moveLeft(){
        if(shouldSkipAction()){
            return;
        }
        int minPosition = getMinPosition();
        if(gemGroup.getPosition() <= minPosition || aGemColumnIsToTheLeft()){
            return;
        }
        gemGroup.decrementPosition();
    }

    private boolean aGemColumnIsToTheLeft(){
        int colIndex = gemGroup.getBasePosition() -1;
        if(colIndex < 0){
            return false;
        }
        return isColumnTallerThanLowestFallingGem(colIndex);
    }

    private boolean aGemColumnIsToTheRight(){
        int colIndex = gemGroup.getEndPosition() + 1;
        if(colIndex > gemGrid.getNumberOfColumns()){
            return false;
        }
        return isColumnTallerThanLowestFallingGem(colIndex);
    }

    private boolean isColumnTallerThanLowestFallingGem(int colIndex){
        return gemGrid.getColumnHeight(colIndex) >= gemGroup.getBottomPosition();
    }


    public void moveRight(){
        if(shouldSkipAction()){
            return;
        }
        int maxPosition = gemGrid.getNumberOfColumns() -1;

        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            maxPosition -= gemGroup.getNumberOfGems() /2;
        }

        if(gemGroup.getPosition() >= maxPosition || aGemColumnIsToTheRight()){
            return;
        }
        gemGroup.incrementPosition();
    }


    public void rotate(){
        if(shouldSkipAction()){
            return;
        }
        if(isVerticalAndAtAnEdge() || isGemGroupAdjacentToColumns()){
            return;
        }
        gemGroup.rotate();
    }


    private boolean isVerticalAndAtAnEdge(){
        boolean isAtAnEdge = gemGroup.getPosition() == getMinPosition() || gemGroup.getPosition() == gemGrid.getNumberOfColumns() -1;
        return isAtAnEdge && gemGroup.getOrientation() == GemGroup.Orientation.VERTICAL;
    }


    private boolean isGemGroupAdjacentToColumns(){
        return isGemGroupToTheLeftOfColumn() || isGemGroupToTheRightOfColumn();
    }


    private boolean isGemGroupToTheRightOfColumn(){
        int columnIndex = gemGroup.getPosition() + 1;
        if(columnIndex >= gemGrid.getNumberOfColumns()){
            return false;
        }
        return doesColumnHeightMeetLowestGem(columnIndex);
    }


    private boolean isGemGroupToTheLeftOfColumn(){
        int columnIndex = gemGroup.getPosition() -1;
        if(columnIndex < 0){
            return false;
        }
        return doesColumnHeightMeetLowestGem(columnIndex);
    }


    public boolean doesColumnHeightMeetLowestGem(int colIndex){
        return gemGrid.getColumnHeight(colIndex) >= gemGroup.getBottomPosition() + 1 + gemGroup.getNumberOfGems() /2;
    }


    private int getMinPosition(){
        int minPosition = 0;
        if(gemGroup.getOrientation() == GemGroup.Orientation.HORIZONTAL){
            minPosition = gemGroup.getNumberOfGems() / 2;
        }
        return minPosition;
    }


    private boolean shouldSkipAction(){
        return gemGroup == null || !isActivated;
    }


}
