package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.service.game.GridProps;
import com.jcrawleydev.gemsdrop.service.game.gem.Gem;
import com.jcrawleydev.gemsdrop.service.game.grid.GemGrid;

import java.util.List;

public class MockGemGrid implements GemGrid {

    private final int[] columnHeights;
    private final GridProps gridProps;

    public MockGemGrid(GridProps gridProps, int ... columnHeights){
        this.gridProps = gridProps;
        this.columnHeights = columnHeights;
    }


    @Override
    public int getColumnHeightAt(int columnIndex){
        return columnHeights[columnIndex] * gridProps.depthPerDrop();
    }


    @Override
    public int getNumberOfColumns(){
        return columnHeights.length;
    }


    @Override
    public List<List<Gem>> getGemColumns(){ return null; }
}
