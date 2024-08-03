package com.jcrawleydev.gemsdrop.gem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.jcrawleydev.gemsdrop.view.DrawItem;
import com.jcrawleydev.gemsdrop.view.item.DrawableItem;

public class Gem implements DrawItem, DrawableItem, Cloneable {


    protected GemColor color;
    private boolean deletionCandidateFlag = false;
    private boolean markedForDeletion = false;
    private float x,y;
    private Bitmap bitmap;
    private boolean visible;
    private int column, position;
    private final long id;


    public Gem(GemColor color){
        this.color = color;
        this.visible = true;
        this.position = -2;
        this.id = System.nanoTime();
    }

    public void setColumn(int column){
        this.column = column;
    }


    public long getId(){
        return id;
    }


    public int getColumn(){
        return column;
    }


    public void setPosition(int position){
        this.position = position;
    }


    public void incPosition(){
        position++;
    }


    public int getPosition(){
        return position;
    }


    public void setGrey(){
        this.color = GemColor.GREY;
    }


    public GemColor getColor(){
        return color;
    }

    @Override
    public float getX(){
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
    public float getY(){
        return y;
    }


    public void setX(float x){
        this.x = x;
    }


    public void setY(float y){
        this.y = y;
    }



    public void setXY(float x, float y){
        setX(x);
        setY(y);
    }


    public void incY(float value){this.y += value;}


    public void setMarkedForDeletion(){
        if(deletionCandidateFlag){
            markedForDeletion = true;
        }
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


    @Override
    public void draw(Canvas canvas, Paint paint) {
        if(!isVisible()){
            return;
        }
        canvas.drawBitmap(bitmap, x, y, paint);
    }


    public void setInvisible(){
        this.visible = false;
    }


    public void setVisible(){
        this.visible = true;
    }


    @Override
    public Gem clone(){
        Gem gem = new Gem(GemColor.BLUE);
        try{
            gem = (Gem) super.clone();
        }catch(CloneNotSupportedException e){
            String message = e.getMessage();
            System.out.println("error cloning gem: " + message);
        }
        return gem;
    }
}
