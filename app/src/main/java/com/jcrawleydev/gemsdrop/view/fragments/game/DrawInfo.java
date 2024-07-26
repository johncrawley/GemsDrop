package com.jcrawleydev.gemsdrop.view.fragments.game;

public class DrawInfo {

    private float x, y;
    private final long id;
    private int width, height;
    private final ItemType itemType;
    private boolean isScheduledForRemoval;
    private float rotationIncrement;
    private float rotation;
    private boolean isRotating;
    private boolean isDestroyed;
    private boolean isOutOfBounds;

    public DrawInfo(ItemType itemType, long id){
        this(itemType, id, false);
    }


    public DrawInfo(ItemType itemType, long id, boolean isRotating){
        this.itemType = itemType;
        this.id = id;
        this.isRotating = isRotating;
    }


    public void setXY(float x, float y){
        this.x = x;
        this.y = y;
    }


    public void incrementRotation(){
        if(isRotating){
            rotation += rotationIncrement;
            rotation %= 360;
        }
    }


    public void markAsDestroyed(){
        isDestroyed = true;
    }


    public void markAsOutOfBounds(){
        isOutOfBounds = true;
    }


    public boolean isDestroyed(){
        return isDestroyed;
    }

    public boolean isOutOfBounds(){
        return isOutOfBounds;
    }

    public boolean shouldBeRemoved(){ return isDestroyed || isOutOfBounds;}

    public float getCurrentRotation(){
        return rotation;
    }


    public boolean isScheduledForRemoval(){
        return isScheduledForRemoval;
    }


    public void scheduleForRemoval(){
        isScheduledForRemoval = true;
    }


    public void setDimensions(int width, int height){
        this.width = width;
        this.height = height;
    }


    public float getX(){
        return x;
    }


    public float getY(){
        return y;
    }


    public int getWidth(){
        return width;
    }


    public int getHeight(){
        return height;
    }


    public long getId(){
        return id;
    }


    public ItemType getItemType(){
        return itemType;
    }


}
