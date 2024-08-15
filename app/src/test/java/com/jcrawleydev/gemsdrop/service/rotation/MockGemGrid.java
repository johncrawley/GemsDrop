package com.jcrawleydev.gemsdrop.service.rotation;

import com.jcrawleydev.gemsdrop.service.GridProps;
import com.jcrawleydev.gemsdrop.service.grid.GemGrid;

public class MockGemGrid implements GemGrid {

    private final int[] columnHeights;
    private final GridProps gridProps;

    public MockGemGrid(GridProps gridProps, int ... columnHeights){
        this.gridProps = gridProps;
        this.columnHeights = columnHeights;
    }


    @Override
    public int getHeightOfColumn(int columnIndex){
        return columnHeights[columnIndex] * gridProps.depthPerDrop();
    }


    @Override
    public int getNumberOfColumns(){
        return columnHeights.length;
    }
}
