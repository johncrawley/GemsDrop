package com.jcrawleydev.gemsdrop.service.rotation;

import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

public class MockGemGrid implements GemGrid {

    private final int[] columnHeights;

    public MockGemGrid(int ... columnHeights){
        this.columnHeights = columnHeights;
    }

    @Override
    public int getHeightAtColumn(int columnIndex){
        return columnHeights[columnIndex];
    }


    @Override
    public int getNumberOfColumns(){
        return columnHeights.length;
    }
}
