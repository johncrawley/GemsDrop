package com.jcrawleydev.gemsdrop.control;

import com.jcrawleydev.gemsdrop.gemgrid.GemGrid;
import com.jcrawleydev.gemsdrop.gemgroup.GemGroup;

public class GemControls {

    private final GemGrid gemGrid;
    private GemGroup gemGroup;
    private boolean isActivated = true;


    public GemControls(GemGroup gemGroup, GemGrid gemGrid){
        this.gemGroup = gemGroup;
        this.gemGrid = gemGrid;
    }

    public GemControls(GemGrid gemGrid){
        this.gemGrid = gemGrid;
    }


    public void set(GemGroup gemGroup){
        this.gemGroup = gemGroup;
    }


    public void deactivate(){
        isActivated = false;
    }


    public void reactivate(){
        isActivated = true;
    }


    public void enableQuickDrop(){
        if(isGemGroupNullOrDeactivated()){
            return;
        }
        deactivate();
        gemGroup.enableQuickDrop();
    }


    public void moveLeft(){
        if(isGemGroupNullOrDeactivated()){
            return;
        }
        if(gemGroup.getXPosition() <= gemGroup.getMinPosition() || aGemColumnIsToTheLeft()){
            return;
        }
        gemGroup.decrementPosition();
    }


    public void moveRight(){
        if(ifCanMoveRight()){
            gemGroup.incrementPosition();
        }
    }


    private boolean ifCanMoveRight(){
        return !(isGemGroupNullOrDeactivated() || isGemGroupAtMaxPosition() || aGemColumnIsToTheRight());
    }


    private boolean isGemGroupAtMaxPosition(){
        return gemGroup.getXPosition() >= getMaxPosition();
    }


    private int getMaxPosition(){
        int horizontalOffset = gemGroup.isVertical() ? 0 : gemGroup.getNumberOfGems() / 2;
        return gemGrid.getNumberOfColumns() - (1 + horizontalOffset);
    }


    public void rotate(){
        if(isGemGroupNullOrDeactivated()){
            return;
        }
        if(isVerticalAndAtAnEdge() || isGemGroupAdjacentToColumns()){
            return;
        }
        gemGroup.rotate();
    }


    private boolean aGemColumnIsToTheLeft(){
        int colIndex = gemGroup.getBaseXPosition() -1;
        if(colIndex < 0){
            return false;
        }
        return isColumnTallerThanLowestFallingGem(colIndex);
    }


    private boolean aGemColumnIsToTheRight(){
        int colIndex = gemGroup.getEndXPosition() + 1;
        if(colIndex > gemGrid.getNumberOfColumns()){
            return false;
        }
        return isColumnTallerThanLowestFallingGem(colIndex);
    }


    private boolean isColumnTallerThanLowestFallingGem(int colIndex){
        return gemGrid.doesColumnHeightMeetLowestGem(colIndex, gemGroup);
    }


    private boolean isVerticalAndAtAnEdge(){
        boolean isAtAnEdge = gemGroup.getXPosition() == gemGroup.getMinPosition() || gemGroup.getXPosition() == gemGrid.getNumberOfColumns() -1;
        return isAtAnEdge && gemGroup.isVertical();
    }


    private boolean isGemGroupAdjacentToColumns(){
        return isGemGroupToTheLeftOfColumn() || isGemGroupToTheRightOfColumn();
    }


    private boolean isGemGroupToTheLeftOfColumn(){
        if(gemGroup.getEndXPosition() >= gemGrid.getNumberOfColumns()){
            return false;
        }
        return doesColumnHeightMeetMiddleGem(gemGroup.getEndXPosition());
    }


    private boolean isGemGroupToTheRightOfColumn(){
       int columnIndex = gemGroup.getXPosition() - 1;
       if(columnIndex < 0){
            return false;
        }
       return gemGrid.doesColumnHeightMeetLowestGem(columnIndex, gemGroup);
    }


    private boolean doesColumnHeightMeetMiddleGem(int colIndex){
        return gemGrid.doesColumnHeightMeetMiddleGem(colIndex, gemGroup);
    }


    private boolean isGemGroupNullOrDeactivated(){
        return gemGroup == null || !isActivated;
    }

}
