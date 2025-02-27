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
        var gemColors = Set.of(TEMP1, TEMP2, YELLOW, GREEN, PURPLE);
        var startingGrid = List.of("G Y G Y G Y G",  "B R B R B R B", "Y P Y P Y P Y");
        var specialGemConditions = new SpecialGemConditions(8, 25, 5);
        gameLevel1 = new GameLevel(1,
                R.drawable.background_pattern_1,
                150,
                100,
                gemColors,
                specialGemConditions,
                startingGrid );
    }

}
