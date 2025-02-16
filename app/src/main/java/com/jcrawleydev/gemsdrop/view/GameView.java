package com.jcrawleydev.gemsdrop.view;



import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.List;

public interface GameView {

    void createGems(List<Gem> gems);
    void updateGems(List<Gem> gems);
    void updateGemsColors(List<Gem> gems);
    void wipeOut(long[] markedGemIds);
    void updateScore(int score);
}
