package com.jcrawleydev.gemsdrop.service.game.grid;

import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.List;

public interface GemGrid {

    int getColumnHeightAt(int columnIndex);
    int getNumberOfColumns();
    List<List<Gem>> getGemColumns();
    void addIfConnecting(Gem gem);
    void addIfConnecting(Gem bottomGem, Gem middleGem, Gem topGem);
    void removeMarkedGems();
}
