package com.jcrawleydev.gemsdrop.instructions;

import com.jcrawleydev.gemsdrop.game.gem.Gem;

import java.util.List;

public interface InstructionsView {

    void createGems(List<Gem> gems);
    void updateGems();
}
