package com.jcrawleydev.gemsdrop.view.fragments.game;



import com.jcrawleydev.gemsdrop.game.gem.Gem;

import java.util.List;

public interface GameView {

    void createGems(List<Gem> gems);
    void updateGems(List<Gem> gems);
    void updateGemsColors(List<Gem> gems);
    void wipeOut(long[] markedGemIds);
    void updateScore(int score);
    void showGameOverAnimation();
    void showHighScores();
}
