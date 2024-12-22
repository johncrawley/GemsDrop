package com.jcrawleydev.gemsdrop.service.game;

public record GridProps(int numberOfRows, int numberOfColumns, int depthPerDrop) {

    public int numberOfPositions(){
        return numberOfRows * depthPerDrop;
    }
}
