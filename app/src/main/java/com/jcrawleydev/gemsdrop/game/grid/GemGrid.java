package com.jcrawleydev.gemsdrop.game.grid;

import com.jcrawleydev.gemsdrop.game.gem.Gem;

import java.util.List;
import java.util.Set;

public interface GemGrid {

    void init();
    int getColumnHeightAt(int columnIndex);
    List<List<Gem>> getGemColumns();
    List<Gem> getGems();
    int getGemCount();
    void addIfConnecting(Gem gem);
    Set<Long> getMarkedGemIdsFromTouching(Gem wonderGem);
    List<Gem> gravityDropGemsOnePosition();
    int removeMarkedGems();
    void printColumnHeights();
    boolean exceedsMaxHeight();
    void addRow(List<Gem> gems);
    void addGemAt(int columnIndex, Gem gem);
    void removeTopGemFrom(int columnIndex);
    void clear();

}
