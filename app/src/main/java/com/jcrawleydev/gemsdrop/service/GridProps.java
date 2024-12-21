package com.jcrawleydev.gemsdrop.service;

public record GridProps(int numberOfRows, int numberOfColumns, int depthPerDrop) {

    public int numberOfPositions(){
        return numberOfRows * depthPerDrop;
    }
}
