package com.jcrawleydev.gemsdrop.service.validation;

import com.jcrawleydev.gemsdrop.game.GridProps;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.grid.GemGrid;

import java.util.Collections;
import java.util.List;
import java.util.Set;

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
    public List<List<Gem>> getGemColumns(){ return null; }

    @Override
    public List<Gem> getGems() {
        return Collections.emptyList();
    }

    @Override
    public int getGemCount() {
        return 0;
    }

    @Override
    public void addIfConnecting(Gem gem) {

    }

    @Override
    public Set<Long> getMarkedGemIdsFromTouching(Gem wonderGem) {
        return Collections.emptySet();
    }

    @Override
    public List<Gem> gravityDropGemsOnePosition() {
        return Collections.emptyList();
    }

    @Override
    public int removeMarkedGems() {
        return 0;
    }

    @Override
    public void printColumnHeights() {

    }

    @Override
    public boolean exceedsMaxHeight() {
        return false;
    }

    @Override
    public void addRow(List<Gem> gems) {

    }
}
