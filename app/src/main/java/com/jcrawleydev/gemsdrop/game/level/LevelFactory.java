package com.jcrawleydev.gemsdrop.game.level;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.*;

import android.annotation.SuppressLint;
import android.content.Context;

import com.jcrawleydev.gemsdrop.R;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class LevelFactory {

    private GameLevel gameLevel1;
    private final Context context;

    public LevelFactory(Context context) {
        this.context = context;
        initGridList();
        setupLevel1();
    }


    public GameLevel getLevel(int levelNumber) {
        setupLevel1();
        return gameLevel1;
    }


    private void setupLevel1() {
        var possibleColorsOfFallingGems = Set.of(
                new GemOccurrence(BLUE, 0),
                new GemOccurrence(RED, 0),
                new GemOccurrence(YELLOW, 0),
                new GemOccurrence(GREEN, 0),
                new GemOccurrence(PURPLE, 0),
                new GemOccurrence(DEEP_BLUE, 20),
                new GemOccurrence(ORANGE, 50),
                new GemOccurrence(LIGHT_PINK, 80));

        var specialGemConditions = new SpecialGemConditions(5, 25, 15);
        List<GemColor> startingColors = List.of(BLUE, GREEN, RED, YELLOW, PURPLE);

        gameLevel1 = new GameLevel(1,
                R.drawable.background_pattern_1,
                350,
                110,
                possibleColorsOfFallingGems,
                specialGemConditions,
                generateRandomGridRows(startingColors));
    }


    private final List<List<Integer>> gridIndexes = new ArrayList<>();


    @SuppressLint("NewApi")
    private void initGridList() {
        var gridList = getListFromResource(context, R.raw.gem_grid_patterns);

        for (var str : gridList) {
            var list = Arrays.stream(str.split(""))
                    .map(Integer::parseInt).collect(Collectors.toList());
            gridIndexes.add(list);
        }
    }


    private List<List<GemColor>> generateRandomGridRows(List<GemColor> startingColors) {
        List<List<GemColor>> gemColors = new ArrayList<>();
        int numberOfGemsPerRow = 7;
        var gridIndexes = getRandomGridIndexes();
        var possibleColors = new ArrayList<>(startingColors);
        Collections.shuffle(possibleColors);

        var row = new ArrayList<GemColor>();
        for (var index : gridIndexes) {
            row.add(possibleColors.get(index));
            if (row.size() == numberOfGemsPerRow) {
                gemColors.add(row);
                row = new ArrayList<>();
            }
        }
        return gemColors;
    }


    private List<Integer> getRandomGridIndexes() {
        var random = new Random(System.nanoTime());
        int randomIndex = random.nextInt(gridIndexes.size());
        return gridIndexes.get(randomIndex);
    }


    public List<String> getListFromResource(Context context, int rawResId) {
        List<String> lines = new ArrayList<>();

        try (var is = context.getResources().openRawResource(rawResId);
             var reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line.trim());
            }
        } catch (IOException e) {
            var msg = e.getMessage();
            System.out.println("IOException opening list from resource: " + msg);
        }

        return lines;
    }

}
