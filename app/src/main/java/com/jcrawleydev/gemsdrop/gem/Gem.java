package com.jcrawleydev.gemsdrop.gem;

import android.graphics.Bitmap;

import com.jcrawleydev.gemsdrop.view.DrawItem;

import androidx.annotation.NonNull;

public class Gem implements DrawItem {

    public enum Color {
        BLUE("B"),
        RED("R"),
        GREEN("G"),
        YELLOW("Y");

        Color(String str){
            this.str = str;
        }
        public String str;

        @NonNull
        public String toString(){
            return str;
        }


    }

    protected Color color;
    private boolean deletionCandidateFlag = false;
    private boolean markedForDeletion = false;
    private int x,y;
    private Bitmap bitmap;


    public Gem(Color color){
        this.color = color;
    }

    public Color getColor(){
        return color;
    }

    @Override
    public int getX(){
        return x;
    }

    public void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }


    @Override
    public Bitmap getBitmap(){
        return this.bitmap;
    }


    @Override
    public int getY(){
        return y;
    }


    public void setX(int x){
        this.x = x;
    }


    public void setY(int y){
        this.y = y;
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
