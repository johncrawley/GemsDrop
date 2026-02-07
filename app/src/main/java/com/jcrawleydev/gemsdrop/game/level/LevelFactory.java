package com.jcrawleydev.gemsdrop.game.level;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.*;

import com.jcrawleydev.gemsdrop.R;

import java.util.List;
import java.util.Set;

public class LevelFactory {

    private GameLevel gameLevel1, tempLevel;

    public LevelFactory(){
        setupLevel1();
        setupLevelTemp();
    }


    public GameLevel getLevel(int levelNumber){
        return gameLevel1;
    }


    private void setupLevel1(){
        var possibleColorsOfFallingGems = Set.of(BLUE, RED, YELLOW, GREEN, PURPLE);
        var startingGrid = List.of("G Y G Y G Y G",  "B R B R B R B", "Y P Y P Y P Y");
        var specialGemConditions = new SpecialGemConditions(8, 25, 5);
        gameLevel1 = new GameLevel(1,
                R.drawable.background_pattern_1,
                150,
                100,
                possibleColorsOfFallingGems,
                specialGemConditions,
                startingGrid );
    }


    private void setupLevelTemp(){
        var possibleColorsOfFallingGems = Set.of(BLUE, GREEN);
        var startingGrid = List.of("B B G B B G B",  "G G B G G B G", "B G B G B G B");
        var specialGemConditions = new SpecialGemConditions(8, 25, 5);
        tempLevel = new GameLevel(1,
                R.drawable.background_pattern_1,
                150,
                100,
                possibleColorsOfFallingGems,
                specialGemConditions,
                startingGrid );
    }

}
