package com.jcrawleydev.gemsdrop.game.grid;

public record GridProps(int numberOfRows, int numberOfColumns, int depthPerDrop) {

    public int numberOfPositions(){
        return numberOfRows * depthPerDrop;
    }
}
