package com.jcrawleydev.gemsdrop.service.game.level;

import com.jcrawleydev.gemsdrop.R;

public class LevelFactory {


    GameLevel gameLevel1 = new GameLevel(1, R.drawable.background_pattern_1, 150, 100);

    public GameLevel getLevel(int levelNumber){
        return gameLevel1;
    }
}
