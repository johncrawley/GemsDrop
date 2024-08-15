package com.jcrawleydev.gemsdrop.gem;

public enum GemPosition {
    TOP(0,-2),
    RIGHT(1, 0),
    BOTTOM(0,2),
    LEFT(-1, 0),
    CENTRE(0,0);

    private final int columnOffset, depthOffset;

    GemPosition(int columnOffset, int depthOffset){
        this.columnOffset = columnOffset;
        this.depthOffset = depthOffset;
    }

    public int getColumnOffset(){
        return columnOffset;
    }

    public int getDepthOffset(){
        return depthOffset;
    }
}