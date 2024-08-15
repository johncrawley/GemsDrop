package com.jcrawleydev.gemsdrop.service;

public record GridProps(int numberOfRows, int numberOfColumns, int depthPerDrop) {

    public int numberOfDepths(){
        return numberOfRows * depthPerDrop;
    }
}
