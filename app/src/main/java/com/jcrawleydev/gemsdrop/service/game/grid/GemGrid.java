package com.jcrawleydev.gemsdrop.service.game.grid;

import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.List;

public interface GemGrid {

    int getHeightOfColumn(int columnIndex);
    int getNumberOfColumns();
    List<List<Gem>> getGemColumns();
}
