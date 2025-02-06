package com.jcrawleydev.gemsdrop.service.game.grid;

import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.List;

public interface GemGrid {

    int getColumnHeightAt(int columnIndex);
    List<List<Gem>> getGemColumns();
    List<Gem> getGems();
    void addIfConnecting(Gem gem);
    void removeMarkedGems();
    void printColumnHeights();
    boolean exceedsMaxHeight();
    void addRow(List<Gem> gems);

}
