package com.jcrawleydev.gemsdrop.game.level;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.*;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class LevelFactory {

    private GameLevel gameLevel1, tempLevel;

    public LevelFactory(){
        setupLevel1();
    }


    public GameLevel getLevel(int levelNumber){
        return gameLevel1;
    }


    private void setupLevel1(){
        var possibleColorsOfFallingGems = Set.of(
                new GemOccurrence(BLUE,0),
                new GemOccurrence(RED, 0),
                new GemOccurrence(YELLOW, 0),
                new GemOccurrence(GREEN, 0),
                new GemOccurrence(PURPLE, 0),
                new GemOccurrence(DEEP_BLUE, 20),
                new GemOccurrence(DARK_RED, 50),
                new GemOccurrence(LIGHT_PINK, 80));

        var startingGrid = new ArrayList<List<GemColor>>();
        addToGrid(startingGrid, GREEN, DEEP_BLUE, GREEN, DEEP_BLUE, GREEN, DEEP_BLUE, GREEN);
        addToGrid(startingGrid, BLUE, RED, BLUE, RED, BLUE, RED, BLUE);
        addToGrid(startingGrid, PURPLE, YELLOW, PURPLE, YELLOW, PURPLE, YELLOW, PURPLE);
        addToGrid(startingGrid, GREEN, RED, BLUE, GREEN, RED, BLUE, GREEN);



        var specialGemConditions = new SpecialGemConditions(5, 25, 15);
        gameLevel1 = new GameLevel(1,
                R.drawable.background_pattern_2,
                350,
                100,
                possibleColorsOfFallingGems,
                specialGemConditions,
                startingGrid );
    }


    private void addToGrid(List<List<GemColor>> grid, GemColor... gemColors){
        grid.add(Arrays.asList(gemColors));
    }


}
