package com.jcrawleydev.gemsdrop.service.game.level;

import static com.jcrawleydev.gemsdrop.service.game.gem.GemColor.*;

import com.jcrawleydev.gemsdrop.R;

import java.util.List;
import java.util.Set;

public class LevelFactory {

    private GameLevel gameLevel1;

    public LevelFactory(){
        setupLevel1();
    }


    public GameLevel getLevel(int levelNumber){
        return gameLevel1;
    }


    private void setupLevel1(){
        var gems = Set.of(BLUE, RED, YELLOW, GREEN, PURPLE);
        var startingGrid = List.of("G Y G Y G Y G",  "B R B R B R B", "Y P Y P Y P Y");
        gameLevel1 = new GameLevel(1, R.drawable.background_pattern_1, 150, 100, gems, startingGrid );
    }

}
