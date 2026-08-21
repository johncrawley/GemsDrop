package com.jcrawleydev.gemsdrop.instructions;

import com.jcrawleydev.gemsdrop.game.gem.Gem;

import java.util.List;

public interface InstructionsView {

    void setClickBounds(float xStart, float yStart, float xEnd, float yEnd);
    void createGems(List<Gem> gems);
    void updateGems();
}
