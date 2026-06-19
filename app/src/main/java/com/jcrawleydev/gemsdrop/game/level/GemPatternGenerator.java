package com.jcrawleydev.gemsdrop.game.level;

import static com.jcrawleydev.gemsdrop.game.gem.GemColor.BLUE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.GREEN;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.PURPLE;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.RED;
import static com.jcrawleydev.gemsdrop.game.gem.GemColor.YELLOW;


import com.jcrawleydev.gemsdrop.game.grid.GridProps;
import com.jcrawleydev.gemsdrop.game.gem.Gem;
import com.jcrawleydev.gemsdrop.game.gem.GemColor;
import com.jcrawleydev.gemsdrop.game.grid.GemGridImpl;
import com.jcrawleydev.gemsdrop.game.grid.GridEvaluator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GemPatternGenerator {

    private final GridEvaluator gridEvaluator = new GridEvaluator();
    private Random random = new Random(System.nanoTime());
    private record NumberGem(int index, GemColor gemColor) {}
    private record GemRowsAndStr(List<List<Gem>> gemCols, String str){}


    public void generate(int rows, int columns) {
        random = new Random(System.nanoTime());
        var gridProps = new GridProps(20, 7, 2);
        var gemGrid = new GemGridImpl(gridProps);
        gridEvaluator.init(gemGrid.getGemColumns(), gridProps.numberOfRows());
        var list = new ArrayList<GemRowsAndStr>();

        for(int i = 0; i < 100; i++){
            list.add(generateResult(rows, columns));
        }

        for (var item : list) {
            gemGrid.clear();
            for(var gemCol : item.gemCols()){
                gemGrid.addRow(gemCol);
            }
            gridEvaluator.init(gemGrid.getGemColumns(), 7);
        }
        print(list);
    }


    private void print(List<GemRowsAndStr> gemRowsAndStrs){

        for(var gemRowAndStr : gemRowsAndStrs){
            System.out.println(gemRowAndStr.str());
        }
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
    }


    private GemRowsAndStr generateResult(int numberOfRowsToFill, int numberOfColumns){
        var str = new StringBuilder();
        var gemRows = new ArrayList<List<Gem>>();
        for (int currentRow = 0; currentRow < numberOfRowsToFill; currentRow++) {

            var gemCol = new ArrayList<Gem>();
            gemRows.add(gemCol);
            for (int currentColumn = 0; currentColumn < numberOfColumns; currentColumn++) {
                var numberGem = getRandomNumberGem();
                str.append(numberGem.index());
                gemCol.add(new Gem(numberGem.gemColor()));
            }
        }
        return new GemRowsAndStr(gemRows, str.toString());
    }


    private NumberGem getRandomNumberGem() {
        var gemColors = List.of(BLUE, GREEN, YELLOW, RED, PURPLE);
        var index = random.nextInt(gemColors.size());
        return new NumberGem(index, gemColors.get(index));
    }
}
