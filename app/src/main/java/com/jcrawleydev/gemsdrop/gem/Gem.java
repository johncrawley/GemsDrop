package com.jcrawleydev.gemsdrop.gem;

public class Gem {

    public enum Color { BLUE, RED, GREEN, YELLOW, EMPTY}

    private Color color;
    private boolean deletionCandidateFlag = false;
    private boolean markedForDeletion = false;

    public Gem(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    public void setMarkedForDeletion(){
        if(deletionCandidateFlag){
            markedForDeletion = true;
        }
    }
    public boolean isMarkedForDeletion(){
        return markedForDeletion;
    }

    public boolean isNotSameColorAs(Gem otherGem){
        return this instanceof NullGem || this.getColor() != otherGem.getColor();
    }

    public void setDeleteCandidateFlag(){
        deletionCandidateFlag = true;
    }

    public void resetDeleteCandidateFlag(){
        deletionCandidateFlag = false;
    }
}
