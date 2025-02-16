package com.jcrawleydev.gemsdrop.service.game.gem;

/*
    the position offsets only work for gem groups of size 3
 */

public enum GemGroupPosition {
    TOP(0,-2,1, 2),
    RIGHT(1, 0, 1, -2),
    BOTTOM(0,2, -1, -2),
    LEFT(-1, 0, -1, 2),
    CENTRE(0,0, 0, 0),
    NONE(0,0, 0, 0);

    private final int columnOffset, depthOffset, clockwiseColumnOffset, clockwiseContainerPositionOffset;

    GemGroupPosition(int columnOffset, int depthOffset, int clockwiseColumnOffset, int clockwiseContainerPositionOffset){
        this.columnOffset = columnOffset;
        this.depthOffset = depthOffset;
        this.clockwiseColumnOffset = clockwiseColumnOffset;
        this.clockwiseContainerPositionOffset = clockwiseContainerPositionOffset;
    }

    public int getColumnOffset(){
        return columnOffset;
    }

    public int getDepthOffset(){
        return depthOffset;
    }

    public int getClockwiseColumnOffset() { return clockwiseColumnOffset; }

    public int getClockwiseContainerPositionOffset() { return clockwiseContainerPositionOffset; }
}