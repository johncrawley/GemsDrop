package com.jcrawleydev.gemsdrop.service.game.level;

import static com.jcrawleydev.gemsdrop.service.game.gem.GemColor.*;

import com.jcrawleydev.gemsdrop.R;

import java.util.Set;

public class LevelFactory {


    GameLevel gameLevel1 = new GameLevel(1, R.drawable.background_pattern_1, 150, 100, Set.of(BLUE, RED, YELLOW, GREEN, PURPLE));

    public GameLevel getLevel(int levelNumber){
        return gameLevel1;
    }



    private void setupLevel1(){
        var gems = Set.of(BLUE, RED, YELLOW, GREEN, PURPLE);
        var startingGrid = "GYGYGYG BRBRBRB YPYPYPY";
        gameLevel1 = new GameLevel(1, R.drawable.background_pattern_1, 150, 100, gems, startingGrid );


    }

}
