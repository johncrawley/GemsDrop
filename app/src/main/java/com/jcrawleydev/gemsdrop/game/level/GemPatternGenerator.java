package com.jcrawleydev.gemsdrop.game.level;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.DEEP_BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.GREEN;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.PURPLE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.RED;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.YELLOW;


import com.jcrawleydev.gemsdrop.game.grid.GemGrid;
import com.jcrawleydev.gemsdrop.game.grid.GridProps;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;
import com.jcrawleydev.gemsdrop.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.game.grid.GridEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/*
    This class generates lists of numbers that represent starting grid patterns.
    a good way to use it is to instantiate it in the LevelFactory and  call generate
    for each number of starting rows that you want.

    Then you have the manual task of copying the log output into a raw resource and formatting so that
    each number is on a separate line.
 */

public class GemPatternGenerator {

    private final GridEvaluator gridEvaluator = new GridEvaluator();
    private Random random = new Random(System.nanoTime());
    private GemGrid gemGrid;
    private final List<GemRowsAndStr> gemRowsAndStrs = new ArrayList<>(100);
    private final List<GemRowsAndStr> outputRows = new ArrayList<>(100);
    private record NumberGem(int index, GemColor gemColor) {}
    private record GemRowsAndStr(List<List<Gem>> gemRows, String str){}


    public void generate(int rows, int columns) {
        initGridAndEvaluator(rows, columns);
        generateRows(rows, columns);
        evalAndCreateOutputRows(rows);
        print(outputRows);
    }


    private void evalAndCreateOutputRows(int rows){
        for (var gemRowAndStr : gemRowsAndStrs) {
           if(areNoGemsConnectingIn(gemRowAndStr, rows)){
               outputRows.add(gemRowAndStr);
           }
        }
    }


    private boolean areNoGemsConnectingIn(GemRowsAndStr gemRowAndStr, int numberOfRows){
        gemGrid.clear();
        for(var row : gemRowAndStr.gemRows()){
            gemGrid.addRow(row);
        }
        gridEvaluator.init(gemGrid.getGemColumns(), numberOfRows);
        return gridEvaluator.evaluateGemGrid().length == 0;
    }


    private void generateRows(int rows, int columns){
        gemRowsAndStrs.clear();
        for(int i = 0; i < 300; i++){
            gemRowsAndStrs.add(generateResult(rows, columns));
        }
    }


    private void initGridAndEvaluator(int numberOfRows, int numberOfColumns){
        random = new Random(System.nanoTime());
        var gridProps = new GridProps(numberOfRows, numberOfColumns, 2);
        gemGrid = new GemGridImpl(gridProps);
        gridEvaluator.init(gemGrid.getGemColumns(), gridProps.numberOfRows());
    }


    private void print(List<GemRowsAndStr> gemRowsAndStrs){
        for(var gemRowAndStr : gemRowsAndStrs){
            System.out.print(" ");
            System.out.print(gemRowAndStr.str());
        }
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }


    private GemRowsAndStr generateResult(int numberOfRowsToFill, int numberOfColumns){
        var str = new StringBuilder();
        var gemRows = new ArrayList<List<Gem>>();
        for (int currentRow = 0; currentRow < numberOfRowsToFill; currentRow++) {
            generateRandomRowAndAddTo(gemRows, str, numberOfColumns);
        }
        return new GemRowsAndStr(gemRows, str.toString());
    }


    private void generateRandomRowAndAddTo(ArrayList<List<Gem>> gemRows, StringBuilder str, int numberOfColumns){
        var row = new ArrayList<Gem>();
        gemRows.add(row);
        for (int currentColumn = 0; currentColumn < numberOfColumns; currentColumn++) {
            var numberGem = getRandomNumberGem();
            str.append(numberGem.index());
            row.add(new Gem(numberGem.gemColor()));
        }
    }


    private NumberGem getRandomNumberGem() {
        var gemColors = List.of(BLUE, GREEN, YELLOW, RED, PURPLE, DEEP_BLUE);
        var index = random.nextInt(gemColors.size());
        return new NumberGem(index, gemColors.get(index));
    }
}
