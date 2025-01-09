package com.jcrawleydev.gemsdrop.view;



import com.jcrawleydev.gemsdrop.service.game.gem.Gem;

import java.util.List;

public interface GameView {

    void updateGems(List<Gem> gems);
    void wipeOut(long[] markedGemIds);
    void freeFall(long[] gemIds);
}
