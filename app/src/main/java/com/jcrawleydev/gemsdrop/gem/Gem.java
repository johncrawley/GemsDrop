package com.jcrawleydev.gemsdrop.gem;

import android.graphics.Bitmap;

import com.jcrawleydev.gemsdrop.view.DrawItem;

import androidx.annotation.NonNull;

public class Gem implements DrawItem, Cloneable {

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
    private boolean visible;


    public Gem(Color color){
        this.color = color;
        this.visible = true;
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


    public void setXY(int x, int y){
        setX(x);
        setY(y);
    }


    public void setY(int y){
        this.y = y;
    }


    public void incY(int value){this.y += value;}


    public void setMarkedForDeletion(){
        if(deletionCandidateFlag){
            markedForDeletion = true;
        }
    }

    public boolean isDeletionCandidateFlagSet(){
        return deletionCandidateFlag;
    }

    public boolean isMarkedForDeletion(){
        return this.markedForDeletion;
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

    @Override
    public boolean isVisible(){
        return this.visible;
    }

    public void setInvisible(){
        this.visible = false;
    }

    public void setVisible(){
        this.visible = true;
    }


    @Override
    public Gem clone(){
        Gem gem= new Gem(Color.BLUE);
        try{
            gem = (Gem) super.clone();
        }catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
        return gem;
    }
}
