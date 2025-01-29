package com.jcrawleydev.gemsdrop.service.game.level;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.service.game.gem.GemColor;

import java.util.Set;

public class LevelFactory {


    GameLevel gameLevel1 = new GameLevel(1, R.drawable.background_pattern_1, 150, 100, Set.of(GemColor.BLUE, GemColor.RED, GemColor.YELLOW, GemColor.GREEN, GemColor.PURPLE));

    public GameLevel getLevel(int levelNumber){
        return gameLevel1;
    }
}
