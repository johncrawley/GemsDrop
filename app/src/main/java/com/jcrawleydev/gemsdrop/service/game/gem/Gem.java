package com.jcrawleydev.gemsdrop.service.game.gem;

public class Gem {


    protected GemColor color;
    private boolean deletionCandidateFlag = false;
    private boolean markedForDeletion = false;
    private int column;
    private GemGroupPosition gemGroupPosition;
    private boolean isAddedToGrid;

    /*
         container position refers to the position of the bottom of the gem,
            so the lowest possible value is 0.
     */
    private int containerPosition;
    private final long id;

    public Gem(GemColor color){
        this(color, GemGroupPosition.BOTTOM, 0);
    }


    public Gem(GemColor color, GemGroupPosition gemGroupPosition, int initialContainerPosition){
        this.color = color;
        this.gemGroupPosition = gemGroupPosition;
        this.containerPosition = initialContainerPosition;
        this.column = 3;
        this.id = System.nanoTime();
    }


    public void markAsAddedToGrid(){
        isAddedToGrid = true;
    }


    public boolean isAlreadyAddedToTheGrid(){
        return isAddedToGrid;
    }


    public void rotateClockwise(){
        gemGroupPosition = switch(gemGroupPosition){
            case TOP     -> GemGroupPosition.RIGHT;
            case RIGHT   -> GemGroupPosition.BOTTOM;
            case BOTTOM  -> GemGroupPosition.LEFT;
            case LEFT    -> GemGroupPosition.TOP;
            case CENTRE  -> GemGroupPosition.CENTRE;
        };
        containerPosition += gemGroupPosition.getClockwiseContainerPositionOffset();
        column += gemGroupPosition.getClockwiseColumnOffset();
    }


    public void moveLeft(){
        column--;
    }


    public void moveRight(){
        column++;
    }


    public void moveUp(){ containerPosition++;}


    public void moveDown(){ containerPosition--;}


    public void setColumn(int column){
        this.column = column;
    }


    public long getId(){
        return id;
    }


    public int getColumn(){
        return column;
    }


    public void setContainerPosition(int containerPosition){
        this.containerPosition = containerPosition;
    }


    public int getContainerPosition(){
        return containerPosition;
    }


    public void decrementContainerPosition(){
         containerPosition--;
    }


    public void setGrey(){
        this.color = GemColor.GREY;
    }


    public GemColor getColor(){
        return color;
    }

    public int getColorId(){
        return color.ordinal();
    }


    public void setMarkedForDeletion(){
        if(deletionCandidateFlag){
            markedForDeletion = true;
        }
    }


    public boolean isMarkedForDeletion(){
        return this.markedForDeletion;
    }


    public boolean isNotSameColorAs(Gem otherGem){

        if(otherGem == null || otherGem instanceof NullGem ){
            return true;
        }
        if(getColor() == null){
            return true;
        }
        if(otherGem.getColor() == null){
            return true;
        }
        return this instanceof NullGem || this.getColor() != otherGem.getColor();
    }


    public void setDeleteCandidateFlag(){
        deletionCandidateFlag = true;
    }


    public void resetDeleteCandidateFlag(){
        deletionCandidateFlag = false;
    }


}
