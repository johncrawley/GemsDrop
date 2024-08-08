package com.jcrawleydev.gemsdrop.gem;

public enum GemPosition {
    TOP(0,-2),
    RIGHT(1, 0),
    BOTTOM(0,2),
    LEFT(-1, 0),
    CENTRE(0,0);

    private final int columnOffset, heightOffset;

    GemPosition(int columnOffset, int heightOffset){
        this.columnOffset = columnOffset;
        this.heightOffset = heightOffset;
    }

    public int getColumnOffset(){
        return columnOffset;
    }

    public int getHeightOffset(){
        return heightOffset;
    }
}