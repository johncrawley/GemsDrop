package com.jcrawleydev.gemsdrop.service.game.grid;

import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.List;
import java.util.Set;

public interface GemGrid {

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

}
